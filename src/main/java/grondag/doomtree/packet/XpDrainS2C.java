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
