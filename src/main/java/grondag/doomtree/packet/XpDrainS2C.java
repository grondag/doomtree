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
import grondag.doomtree.block.player.AlchemicalBlockEntity;
import grondag.doomtree.block.player.BasinBlockEntity;
import grondag.doomtree.registry.DoomParticles;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum XpDrainS2C {
	;

	public static void send(World world, double x, double y, double z, BlockPos pos) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeBlockPos(pos);
		final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		PlayerStream.around(world, pos, 32).forEach(p -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, packet));
	}

	public static void handle(PacketContext context, PacketByteBuf buffer) {
		if (context.getPlayer() == null) return;

		final double px = buffer.readDouble();
		final double py = buffer.readDouble() + 1;
		final double pz = buffer.readDouble();
		final BlockPos pos = buffer.readBlockPos();

		if (context.getTaskQueue().isOnThread()) {
			handleInner(context.getPlayer().world, pos, px, py, pz);
		} else {
			context.getTaskQueue().execute(() -> handleInner(context.getPlayer().world, pos, px, py, pz));
		}
	}

	private static void handleInner(World world, BlockPos pos, double px, double py, double pz) {
		if (world != null) {
			final BlockEntity be = world.getBlockEntity(pos);

			if (be instanceof AlchemicalBlockEntity) {
				final Random rand = ThreadLocalRandom.current();

				for (int i = 0; i < BasinBlockEntity.XP_COST; i++) {
					final double x = px + rand.nextGaussian() * 0.2;
					final double y = py + rand.nextGaussian() * 0.2;
					final double z = pz + rand.nextGaussian() * 0.2;
					final double v = 1.0 + rand.nextGaussian() * 0.2;
					final double dx = (pos.getX() + 0.5 - x) * 0.03 * v;
					final double dy = (pos.getY() + 0.5 - y) * 0.03 * v;
					final double dz = (pos.getZ() + 0.5 - z) * 0.03 * v;
					world.addParticle(DoomParticles.ALCHEMY_WAKING, x, y, z, dx, dy, dz);
				}
			}
		}
	}

	public static Identifier IDENTIFIER = DoomTree.REG.id("xpdrain");
}
