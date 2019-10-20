package grondag.doomtree.particle;

import grondag.doomtree.registry.DoomBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public abstract class AlchemyParticle extends SpriteBillboardParticle {
	protected final SpriteProvider spriteProvider;
	private static final float RESISTANCE = 0.91F;

	public AlchemyParticle(World world, double x, double y, double z, double vX, double vY, double vZ, SpriteProvider sprites) {
		super(world, x, y, z);
		this.spriteProvider = sprites;
		//		this.gravityStrength = 0.008F;
		this.scale = 0.05f;
		this.maxAge = 16;
		this.velocityX = vX;
		this.velocityY = vY;
		this.velocityZ = vZ;
		//		this.setColorAlpha(0);
		this.setBoundingBoxSpacing(0.05F, 0.05F);
		setSprite(sprites.getSprite(0, 1));
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	protected int getColorMultiplier(float float_1) {
		return 0xF000F0;
	}

	@Override
	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;

		if (this.age++ >= this.maxAge) {
			this.markDead();
		} else {
			this.setSpriteForAge(this.spriteProvider);

			if (this.age > 2 && this.age < this.maxAge) {
				final float factor = (float)age / maxAge;
				//				this.colorRed = RED_0 + factor * RED_SPAN;
				//				this.colorGreen = GREEN_0 + factor * GREEN_SPAN;
				//				this.colorBlue = BLUE_0 + factor * BLUE_SPAN;
			}

			this.velocityY -= this.gravityStrength;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= RESISTANCE;
			this.velocityY *= RESISTANCE;
			this.velocityZ *= RESISTANCE;

			if (this.onGround) {
				this.velocityX *= 0.699999988079071D;
				this.velocityZ *= 0.699999988079071D;
			}

		}
	}

	private static class IdleParticle extends AlchemyParticle {

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
	}

	@Environment(EnvType.CLIENT)
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

	private static class WakingParticle extends AlchemyParticle {

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
	}

	@Environment(EnvType.CLIENT)
	public static class WakingFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public WakingFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new WakingParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}
