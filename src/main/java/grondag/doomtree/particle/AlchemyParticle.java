package grondag.doomtree.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public abstract class AlchemyParticle extends SpriteBillboardParticle {
	protected final SpriteProvider spriteProvider;
//	private static final float RESISTANCE = 0.91F;

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

//	@Override
//	public void tick() {
//		this.prevPosX = this.x;
//		this.prevPosY = this.y;
//		this.prevPosZ = this.z;
//
//		if (this.age++ >= this.maxAge) {
//			this.markDead();
//		} else {
//			this.setSpriteForAge(this.spriteProvider);
//
//			if (this.age > 2 && this.age < this.maxAge) {
//				final float factor = (float)age / maxAge;
//				//				this.colorRed = RED_0 + factor * RED_SPAN;
//				//				this.colorGreen = GREEN_0 + factor * GREEN_SPAN;
//				//				this.colorBlue = BLUE_0 + factor * BLUE_SPAN;
//			}
//
//			this.velocityY -= this.gravityStrength;
//			this.move(this.velocityX, this.velocityY, this.velocityZ);
//			this.velocityX *= RESISTANCE;
//			this.velocityY *= RESISTANCE;
//			this.velocityZ *= RESISTANCE;
//
//			if (this.onGround) {
//				this.velocityX *= 0.699999988079071D;
//				this.velocityZ *= 0.699999988079071D;
//			}
//
//		}
//	}
}
