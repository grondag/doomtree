package grondag.doomtree.packet;

import java.util.Random;

import grondag.doomtree.DoomTree;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
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
		final BlockPos pos = buffer.readBlockPos();
		final Random rand = ThreadLocalRandom.current();
		final int count = 4 + rand.nextInt(3);
		for (int i = 0; i < count; i++) {
			context.getPlayer().world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}

	public static Identifier IDENTIFIER = DoomTree.REG.id("miasma");
}
