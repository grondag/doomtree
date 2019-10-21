package grondag.doomtree.particle;

import java.util.Random;

import grondag.doomtree.registry.DoomParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WardingParticle extends AlchemyParticle {

	static final double RESISTANCE = 0.98;
	
	public WardingParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
		super(world, x, y, z, vX, vY, vZ, sprites);
		this.colorRed = random.nextFloat() * 0.2f + 0.8f;
		this.colorGreen = 1;
		this.colorBlue = 1;
		this.scale = 0.03f;
		this.maxAge = 24;
		this.collidesWithWorld = false;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
			prevPosX = x;
			prevPosY = y;
			prevPosZ = z;
			setPos(x + velocityX, y + velocityY, z + velocityZ);
			velocityX *= RESISTANCE;
			velocityY *= RESISTANCE;
			velocityZ *= RESISTANCE;
		}
	}
	
	public static void spawn(World world, BlockPos pos, Random rand, int howMany, double speedFactor) {
		final double px = pos.getX() + 0.5;
		final double py = pos.getY() + 1;
		final double pz = pos.getZ() + 0.5;
		
		for (int i = 0; i < howMany; i++) {
			final double dx = rand.nextGaussian() * 0.2;
			final double dy = Math.abs(rand.nextGaussian()) * 0.2;
			final double dz = rand.nextGaussian() * 0.2;
			final double v = 0.01 + rand.nextDouble() * 0.02 * speedFactor;
			world.addParticle(DoomParticles.BASIN_ACTIVE, px + dx, py + dy, pz + dz, dx * v, dy * v, dz * v);
		}
	}
	
	public static class WardingFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public WardingFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new WardingParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}