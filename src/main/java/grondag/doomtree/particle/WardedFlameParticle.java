package grondag.doomtree.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class WardedFlameParticle extends SpriteBillboardParticle {
	private WardedFlameParticle(World world, double x, double y, double z, double dx, double dy, double dz, SpriteProvider sprites) {
		super(world, x, y, z, dx, dy, dz);
		this.sprite = sprites.getSprite(0, 1);
		this.velocityX = this.velocityX * 0.009999999776482582D + dx;
		this.velocityY = this.velocityY * 0.009999999776482582D + dy;
		this.velocityZ = this.velocityZ * 0.009999999776482582D + dz;
		this.x += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
		this.y += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
		this.z += ((this.random.nextFloat() - this.random.nextFloat()) * 0.05F);
		this.maxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void move(double double_1, double double_2, double double_3) {
		this.setBoundingBox(this.getBoundingBox().offset(double_1, double_2, double_3));
		this.repositionFromBoundingBox();
	}

	@Override
	public float getSize(float tickDelta) {
		float scale = (this.age + tickDelta) / this.maxAge;
		return this.scale * (1.0F - scale * scale * 0.5F);
	}

	@Override
	public int getColorMultiplier(float tickDelta) {
		float float_2 = (this.age + tickDelta) / (float)this.maxAge;
		float_2 = MathHelper.clamp(float_2, 0.0F, 1.0F);
		int int_1 = super.getColorMultiplier(tickDelta);
		int int_2 = int_1 & 255;
		int int_3 = int_1 >> 16 & 255;
		int_2 += (int)(float_2 * 15.0F * 16.0F);
		if (int_2 > 240) {
			int_2 = 240;
		}

		return int_2 | int_3 << 16;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= 0.9599999785423279D;
			this.velocityY *= 0.9599999785423279D;
			this.velocityZ *= 0.9599999785423279D;

			if (this.onGround) {
				this.velocityX *= 0.699999988079071D;
				this.velocityZ *= 0.699999988079071D;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public static class WardedFlameParticleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public WardedFlameParticleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new WardedFlameParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}