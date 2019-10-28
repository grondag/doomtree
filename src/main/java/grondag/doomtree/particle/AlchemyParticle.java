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

	public AlchemyParticle(World world, double x, double y, double z, double vX, double vY, double vZ, SpriteProvider sprites) {
		super(world, x, y, z);
		this.spriteProvider = sprites;
		this.scale = 0.05f;
		this.maxAge = 16;
		this.velocityX = vX;
		this.velocityY = vY;
		this.velocityZ = vZ;
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
}
