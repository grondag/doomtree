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
package grondag.doomtree.ichor;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomFluids;
import grondag.doomtree.registry.DoomItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateFactory;

public abstract class IchorFluid extends WaterFluid {
	@Override
	public Fluid getFlowing() {
		return DoomFluids.FLOWING_ICHOR;
	}

	@Override
	public Fluid getStill() {
		return DoomFluids.ICHOR;
	}

	@Override
	protected boolean isInfinite() {
		return false;
	}

	@Override
	public Item getBucketItem() {
		return DoomItems.ICHOR_BUCKET;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ParticleEffect getParticle() {
		return ParticleTypes.DRIPPING_WATER;
	}

	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == DoomFluids.ICHOR || fluid == DoomFluids.FLOWING_ICHOR;
	}

	@Override
	public BlockState toBlockState(FluidState fluidState) {
		return DoomBlocks.ICHOR_BLOCK.getDefaultState().with(FluidBlock.LEVEL, method_15741(fluidState));
	}

	public static class Flowing extends IchorFluid {
		@Override
		protected void appendProperties(StateFactory.Builder<Fluid, FluidState> stateBuilder) {
			super.appendProperties(stateBuilder);
			stateBuilder.add(LEVEL);
		}

		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
	}

	public static class Still extends IchorFluid {
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}

		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
	}
}
