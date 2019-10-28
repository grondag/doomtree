package grondag.doomtree.particle;

import grondag.doomtree.registry.DoomBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@Environment(EnvType.CLIENT)
public class SummoningParticle extends AlchemyParticle {

	public SummoningParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
		super(world, x, y, z, vX, vY, vZ, sprites);
		this.colorRed = 1f;
		this.colorGreen = 1f;
		this.colorBlue = 1f;
		this.colorAlpha = 1f;
		this.scale = 0.05f;
		this.maxAge = 40;
		this.collidesWithWorld = false;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge || world.getBlockState(new BlockPos(this.x, this.y, this.z)).getBlock() == DoomBlocks.DOOM_SAPLING_BLOCK) {
			this.markDead();
		} else {
			prevPosX = x;
			prevPosY = y;
			prevPosZ = z;
			
			setPos(x + velocityX, y + velocityY, z + velocityZ);
			
			velocityX *= 1.05;
			velocityY *= 1.05;
			velocityZ *= 1.05;
			
			scale *= 1.05;
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class SummoningParticleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public SummoningParticleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new SummoningParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}