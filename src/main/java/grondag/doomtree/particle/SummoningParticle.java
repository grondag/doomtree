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
public class SummoningParticle extends SimpleParticle {
	public SummoningParticle(World world, double x, double y, double z, double vX, double vY, double vZ,  SpriteProvider sprites) {
		super(world, x, y, z, vX, vY, vZ, sprites);
		colorRed = 1f;
		colorGreen = 1f;
		colorBlue = 1f;
		colorAlpha = 1f;
		scale = 0.05f;
		maxAge = 40;
		collidesWithWorld = false;
	}

	@Override
	public void tick() {
		if (age++ >= maxAge || world.getBlockState(new BlockPos(x, y, z)).getBlock() == DoomBlocks.DOOM_SAPLING_BLOCK) {
			markDead();
		} else {
			prevPosX = x;
			prevPosY = y;
			prevPosZ = z;

			setPos(x + velocityX, y + velocityY, z + velocityZ);

			velocityX *= 1.04;
			velocityY *= 1.04;
			velocityZ *= 1.04;

			scale *= 1.04;
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
