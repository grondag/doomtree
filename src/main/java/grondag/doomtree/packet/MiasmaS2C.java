package grondag.doomtree.packet;

import java.util.Random;

import grondag.doomtree.DoomTree;
import grondag.doomtree.block.AlchemicalBlockEntity;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.Packet;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum MiasmaS2C {
	;

	public static void send(World world, BlockPos pos) {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBlockPos(pos);
		final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		PlayerStream.around(world, pos, 32).forEach(p -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, packet));
	}

	public static void handle(PacketContext context, PacketByteBuf buffer) {
		if (context.getPlayer() == null) return;
		
		final BlockPos pos = buffer.readBlockPos();

		if (context.getTaskQueue().isOnThread()) {
			handleInner(context.getPlayer().world, pos);
		} else {
			context.getTaskQueue().execute(() -> handleInner(context.getPlayer().world, pos));
		}
	}
	
	private static void handleInner(World world, BlockPos pos) {
		if (world != null) {
			final BlockEntity be = world.getBlockEntity(pos);
			
			if (be instanceof AlchemicalBlockEntity) {
				final Random rand = ThreadLocalRandom.current();
				final int count = 4 + rand.nextInt(3);
				
				for (int i = 0; i < count; i++) {
					world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
				}
			}
		}
	}

	public static Identifier IDENTIFIER = DoomTree.REG.id("miasma");
}
