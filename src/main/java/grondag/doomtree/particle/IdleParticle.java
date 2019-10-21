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
public class IdleParticle extends AlchemyParticle {

	public IdleParticle(World world, double x, double y, double z, SpriteProvider sprites) {
		super(world, x, y, z, 0, 0, 0, sprites);
		this.colorRed = random.nextFloat() * 0.4f + 0.6f;
		this.colorGreen = random.nextFloat() * 0.4f + 0.6f;
		this.colorBlue = random.nextFloat() * 0.4f + 0.6f;
		this.scale = 0.02f;
	}

	@Override
	public void tick() {
		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	public static class IdleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public IdleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new IdleParticle(world, x, y, z, sprites);
		}
	}
}