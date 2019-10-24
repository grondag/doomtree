package grondag.doomtree.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class BasinParticle extends AlchemyParticle {

	static final double RESISTANCE = 0.98;
	
	public BasinParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
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
	
	public static class BasinParticleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public BasinParticleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new BasinParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}