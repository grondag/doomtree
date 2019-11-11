/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package grondag.doomtree.packet;

import java.util.Random;

import grondag.doomtree.DoomTree;
import grondag.doomtree.entity.Explodinator;
import grondag.doomtree.entity.Explodinator.ExplosionFX;
import grondag.doomtree.entity.WalkerAttackGoal;
import grondag.doomtree.entity.WalkerEntity;
import grondag.doomtree.registry.DoomParticles;
import grondag.doomtree.registry.DoomSounds;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public enum WalkerPulseS2C {
	;

	public static void send(World world, WalkerEntity from, Vec3d to, Explodinator explosion) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(from.getEntityId());
		buf.writeDouble(to.x);
		buf.writeDouble(to.y);
		buf.writeDouble(to.z);
		explosion.toBuffer(buf);

		final Vec3d middle = new Vec3d((from.x + to.x) * 0.5, (from.y + to.y) * 0.5, (from.z + to.z) * 0.5);
		final double radius = MathHelper.sqrt(middle.squaredDistanceTo(from.x, from.y, from.z)) + 64;

		PlayerStream.around(world, middle, radius).forEach(p -> {
			final PacketByteBuf playerBuf = new PacketByteBuf(Unpooled.buffer());
			playerBuf.writeBytes(buf, 0, buf.readableBytes());
			final Vec3d v = explosion.getAffectedPlayers().get(p);
			playerBuf.writeFloat(v == null ? 0 : (float) v.x);
			playerBuf.writeFloat(v == null ? 0 : (float) v.y);
			playerBuf.writeFloat(v == null ? 0 : (float) v.z);
			final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, playerBuf);
			ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, packet);
		});
	}

	public static void handle(PacketContext context, PacketByteBuf buf) {
		final World world = context.getPlayer().world;
		final WalkerEntity walker = (WalkerEntity) world.getEntityById(buf.readVarInt());
		final Vec3d to = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		final Explodinator explosion = Explodinator.get().setWorld(world).fromBuffer(buf);
		final float vx = buf.readFloat();
		final float vy = buf.readFloat();
		final float vz = buf.readFloat();

		if (context.getTaskQueue().isOnThread()) {
			handleInner(context.getPlayer(), walker, to, explosion, vx, vy, vz);
		} else {
			context.getTaskQueue().execute(() -> handleInner(context.getPlayer(), walker, to, explosion, vx, vy, vz));
		}
	}

	private static void handleInner(PlayerEntity player, WalkerEntity walker, Vec3d to, Explodinator explosion, float pvx, float pvy, float pvz) {
		if (player == null || player.world == null || walker == null) {
			return;
		}

		final World world = player.world;

		final Vec3d from = new Vec3d(walker.x, walker.y + walker.getStandingEyeHeight() + WalkerAttackGoal.FIRE_HEIGHT_OFFSET, walker.z);
		final double dStep =  0.25 / from.distanceTo(to);

		final Random rand =  ThreadLocalRandom.current();

		final double dx = (to.x - from.x);
		final double dy = (to.y - from.y);
		final double dz = (to.z - from.z);

		double dd = dStep * 2;
		double x = from.x + dx * dd;
		double y = from.y + dy * dd;
		double z = from.z + dz * dd;

		while (dd < 1.0) {
			world.addParticle(DoomParticles.WALKER_PULSE, x, y, z, 0, 0, 0);
			final double d = dStep * (0.5 + rand.nextDouble());
			x += dx * d;
			y += dy * d;
			z += dz * d;
			dd += d;
		}

		explosion.setParticles(PULSE_FX, ParticleTypes.SMOKE, ParticleTypes.POOF);
		explosion.affectWorld(true);

		if (pvx !=0 || pvy != 0 || pvz != 0) {
			player.setVelocity(player.getVelocity().add(pvx, pvy, pvz));
		}

		final double d = 1 - (Math.sqrt(player.squaredDistanceTo(from)) / 64d);

		if (d > 0) {
			world.playSound(x, y, z, DoomSounds.WALKER_SHOT, SoundCategory.HOSTILE, (float) (d * d), 1, false);
		}
	}

	public static Identifier IDENTIFIER = DoomTree.REG.id("walker_pulse");

	public static final ExplosionFX PULSE_FX = (w, x, y, z, p, rand)  -> {
		for (int i = 0; i < 32; i++)  {
			final double vx = rand.nextGaussian() * 0.2;
			final double vy = rand.nextGaussian() * 0.2;
			final double vz = rand.nextGaussian() * 0.2;
			w.addParticle(DoomParticles.WALKER_EXPLOSION, x + vx, y + vy, z + vz, vx, vy, vz);
		}
	};
}
