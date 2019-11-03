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
package grondag.doomtree.block.tree;

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.registry.DoomFluids;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MiasmaBlock extends Block {

	public static Material MIASMA = new Material(
		MaterialColor.AIR,
		false, // liquid
		false, // solid
		false, // blocksMovement
		false, // blocksLight
		false, // breakByHand
		false, // burnable
		true,  // replaceable
		PistonBehavior.IGNORE);

	public MiasmaBlock() {
		super(FabricBlockSettings.of(MIASMA).build());
	}

	@Override
	public BlockRenderType getRenderType(final BlockState blockState) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getOutlineShape(final BlockState blockState, final BlockView blockView, final BlockPos blockPos, final EntityContext entityContext) {
		return VoxelShapes.empty();
	}

	@Override
	public void onBlockRemoved(final BlockState myState, final World world, final BlockPos blockPos, final BlockState newState, final boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		if (!world.isClient) {
			final FluidState fluidState = newState.getFluidState();

			if(fluidState != null && !fluidState.isEmpty() && fluidState.isStill() && newState.getBlock() instanceof FluidBlock) {
				if (DoomTreeTracker.isNear(world, blockPos)) {
					world.setBlockState(blockPos, fluidState.getFluid() == Fluids.LAVA
						? DoomBlockStates.DOOMED_STONE_STATE
							: DoomFluids.ICHOR.getDefaultState().getBlockState());
				}
			}

			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}

	@Override
	public boolean isAir(final BlockState blockState) {
		return false;
	}

	@Override
	public void onEntityCollision(final BlockState blockState, final World world, final BlockPos pos, final Entity entity) {
		DoomEffect.exposeToDoom(entity, 2);
	}
}
