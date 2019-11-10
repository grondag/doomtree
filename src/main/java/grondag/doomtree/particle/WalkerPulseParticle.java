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
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;


@Environment(EnvType.CLIENT)
public class WalkerPulseParticle extends SimpleParticle {
	protected final boolean isMoving;

	public WalkerPulseParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
		super(world, x, y, z, vX, vY, vZ, sprites);
		colorRed = 1f;
		colorGreen = 1f;
		colorBlue = 1f;
		colorAlpha = 1f;
		angle = (float) (random.nextFloat() * Math.PI * 2);
		scale = random.nextFloat() * 0.05f + 0.05f;
		maxAge = 4 + random.nextInt(4);
		collidesWithWorld = false;
		isMoving = vX != 0 || vY != 0 || vZ != 0;
	}

	@Override
	public void tick() {
		if (age++ >= maxAge) {
			markDead();
		} else {
			setSpriteForAge(spriteProvider);

			if (isMoving) {
				prevPosX = x;
				prevPosY = y;
				prevPosZ = z;
				setPos(x + velocityX, y + velocityY, z + velocityZ);
				//				velocityX *= RESISTANCE;
				//				velocityY *= RESISTANCE;
				//				velocityZ *= RESISTANCE;
			}
			//			scale *= 0.8;
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class WalkerPulseParticleFactory implements ParticleFactory<DefaultParticleType> {
		private final FabricSpriteProvider sprites;

		public WalkerPulseParticleFactory(FabricSpriteProvider sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double vX, double vY, double vZ) {
			return new WalkerPulseParticle(world, x, y, z, vX, vY, vZ, sprites);
		}
	}
}
