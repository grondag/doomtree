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
package grondag.doomtree.block.player;

import java.util.Random;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomParticles;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public class BrazierBlockEntity extends AlchemicalBlockEntity {

	public BrazierBlockEntity(BlockEntityType<?> entityType) {
		super(entityType, DoomParticles.BRAZIER_ACTIVE);
	}

	public BrazierBlockEntity() {
		this(DoomBlocks.BRAZIER_BLOCK_ENTITY);
	}

	@Override
	protected void doActiveParticles(Random rand) {
		super.doActiveParticles(rand);

		if (rand.nextInt(8) == 0) {
			final BlockPos pos = this.pos;
			world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextDouble(), pos.getY() + 1.0 + rand.nextDouble() * 0.2, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}
}
