package grondag.doomtree.particle;

import grondag.doomtree.registry.DoomBlocks;
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
public class WakingParticle extends AlchemyParticle {

	public WakingParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
		super(world, x, y, z, vX, vY, vZ, sprites);
		this.colorRed = 1f;
		this.colorGreen = 1f;
		this.colorBlue = 1f;
		this.colorAlpha = 1f;
		this.scale = 0.1f;
		this.maxAge = 40;
		this.collidesWithWorld = false;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge || world.getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock() == DoomBlocks.BASIN_BLOCK) {
			this.markDead();
		} else {
			if (velocityX != 0.0D || velocityY != 0.0D || velocityZ != 0.0D) {
				prevPosX = x;
				prevPosY = y;
				prevPosZ = z;
				setPos(x + velocityX, y + velocityY, z + velocityZ);
			}
		}
	}

	public static class WakingParticleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public WakingParticleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new WakingParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}