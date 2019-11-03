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

import static grondag.fermion.position.PackedBlockPos.getExtra;
import static grondag.fermion.position.PackedBlockPos.getX;
import static grondag.fermion.position.PackedBlockPos.getY;
import static grondag.fermion.position.PackedBlockPos.getZ;

import java.util.Random;

import grondag.doomtree.DoomTree;
import grondag.doomtree.registry.DoomSounds;
import grondag.fermion.position.PackedBlockPos;
import grondag.fermion.position.PackedBlockPosList;
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
import net.minecraft.world.World;

public enum DoomS2C {
	;

	// Perhaps the ultimate offense to enums everywhere...
	public static int MIASMA = 0;
	public static int DOOM = 1;
	public static int ICHOR = 2;

	private static final PackedBlockPosList REPORTS  = new PackedBlockPosList();

	// TODO: compact with run-length encoding
	public static void send(World world, PackedBlockPosList reports) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		final int limit = reports.size();

		buf.writeVarInt(limit);

		for  (int i = 0; i < limit; i++) {
			buf.writeLong(reports.get(i));
		}

		final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);

		PlayerStream.world(world)
		.filter(p -> reports.isNear(PackedBlockPos.pack(p.x, p.y, p.z), 32))
		.forEach(p -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, packet));
	}

	public static void handle(PacketContext context, PacketByteBuf buf) {
		if (context.getPlayer() == null) return;

		REPORTS.clear();

		final int limit = buf.readVarInt();

		for (int i = 0; i < limit; i++) {
			REPORTS.add(buf.readLong());
		}

		if (context.getTaskQueue().isOnThread()) {
			handleInner(context.getPlayer());
		} else {
			context.getTaskQueue().execute(() -> handleInner(context.getPlayer()));
		}
	}

	private static void handleInner(PlayerEntity player) {
		if (player != null && player.world != null) {
			final World world = player.world;
			final Random rand = ThreadLocalRandom.current();
			final int limit = REPORTS.size();
			float vMax = 0;
			long pMax = 0;

			for (int i = 0; i < limit; i++) {
				final long p = REPORTS.get(i);
				final int x = getX(p);
				final int y = getY(p);
				final int z = getZ(p);

				if (getExtra(p) == MIASMA) {
					world.addParticle(ParticleTypes.SMOKE, x + rand.nextFloat(), y + rand.nextFloat(), z + rand.nextFloat(), 0.0D, 0.0D, 0.0D);

					final int dx = (int) player.x - x;
					final int dy = (int) player.y - y;
					final int dz = (int) player.z - z;
					final float v = 0.8f / (dx * dx + dy * dy + dz * dz);

					if (v > vMax) {
						vMax = v;
						pMax = p;
					}
				}
			}

			if (vMax > 0.01) {
				world.playSound(getX(pMax), getY(pMax), getZ(pMax), DoomSounds.MIASMA, SoundCategory.HOSTILE, vMax, 1.0f, false);
			}
		}
	}

	public static Identifier IDENTIFIER = DoomTree.REG.id("doom");
}
