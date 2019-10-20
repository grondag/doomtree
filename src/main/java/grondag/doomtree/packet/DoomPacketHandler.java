package grondag.doomtree.packet;

import java.util.Random;

import grondag.doomtree.block.BasinBlockEntity;
import grondag.doomtree.registry.DoomParticles;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class DoomPacketHandler {
	public static void accept(PacketContext context, PacketByteBuf buffer) {
		DoomPacket packet = buffer.readEnumConstant(DoomPacket.class);
		
		switch (packet) {
		case MIASMA:
			miasma(context, buffer);
			break;
		case XP_DRAIN:
			xpDrain(context, buffer);
			break;
		default:
			break;
		
		}
	}

	private static void miasma(PacketContext context, PacketByteBuf buffer) {
		final BlockPos pos = buffer.readBlockPos();
		final Random rand = ThreadLocalRandom.current();
		final int count = 4 + rand.nextInt(3);
		for (int i = 0; i < count; i++) {
			context.getPlayer().world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextFloat(), pos.getY() + rand.nextFloat(), pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
		}
	}
	
	private static void xpDrain(PacketContext context, PacketByteBuf buffer) {
		final double px = buffer.readDouble();
		final double py = buffer.readDouble() + 1;
		final double pz = buffer.readDouble();
		final BlockPos pos = buffer.readBlockPos();
		final Random rand = ThreadLocalRandom.current();

		for (int i = 0; i < BasinBlockEntity.XP_COST; i++) {
			final double x = px + rand.nextGaussian() * 0.2;
			final double y = py + rand.nextGaussian() * 0.2;
			final double z = pz + rand.nextGaussian() * 0.2;
			final double v = 1.0 + rand.nextGaussian() * 0.2;
			final double dx = (pos.getX() + 0.5 - x) * 0.03 * v;
			final double dy = (pos.getY() + 0.5 - y) * 0.03 * v;
			final double dz = (pos.getZ() + 0.5 - z) * 0.03 * v;
			context.getPlayer().world.addParticle(DoomParticles.BASIN_WAKING, x, y, z, dx, dy, dz);
		}
	}
}