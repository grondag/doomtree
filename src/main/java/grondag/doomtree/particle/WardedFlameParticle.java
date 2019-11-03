/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
		sprite = sprites.getSprite(0, 1);
		velocityX = velocityX * 0.009999999776482582D + dx;
		velocityY = velocityY * 0.009999999776482582D + dy;
		velocityZ = velocityZ * 0.009999999776482582D + dz;
		this.x += ((random.nextFloat() - random.nextFloat()) * 0.05F);
		this.y += ((random.nextFloat() - random.nextFloat()) * 0.05F);
		this.z += ((random.nextFloat() - random.nextFloat()) * 0.05F);
		maxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D)) + 4;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Override
	public void move(double double_1, double double_2, double double_3) {
		setBoundingBox(getBoundingBox().offset(double_1, double_2, double_3));
		repositionFromBoundingBox();
	}

	@Override
	public float getSize(float tickDelta) {
		final float scale = (age + tickDelta) / maxAge;
		return this.scale * (1.0F - scale * scale * 0.5F);
	}

	@Override
	public int getColorMultiplier(float tickDelta) {
		float float_2 = (age + tickDelta) / maxAge;
		float_2 = MathHelper.clamp(float_2, 0.0F, 1.0F);
		final int int_1 = super.getColorMultiplier(tickDelta);
		int int_2 = int_1 & 255;
		final int int_3 = int_1 >> 16 & 255;
		int_2 += (int)(float_2 * 15.0F * 16.0F);
		if (int_2 > 240) {
			int_2 = 240;
		}

		return int_2 | int_3 << 16;
	}

	@Override
	public void tick() {
		prevPosX = x;
		prevPosY = y;
		prevPosZ = z;

		if (age++ >= maxAge) {
			markDead();
		} else {
			this.move(velocityX, velocityY, velocityZ);
			velocityX *= 0.9599999785423279D;
			velocityY *= 0.9599999785423279D;
			velocityZ *= 0.9599999785423279D;

			if (onGround) {
				velocityX *= 0.699999988079071D;
				velocityZ *= 0.699999988079071D;
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
