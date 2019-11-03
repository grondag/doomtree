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

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomBlockStates;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class IchorBlock extends FluidBlock {
	public IchorBlock(final BaseFluid fluid, final Settings settings) {
		super(fluid, settings);
	}

	@Override
	public void onEntityCollision(final BlockState blockState, final World world, final BlockPos pos, final Entity entity) {
		DoomEffect.exposeToDoom(entity, 8);

		//		if(!world.isClient) {
		//			if(entity instanceof ItemEntity) {
		//				final List<ItemEntity> entities = world.getEntities(ItemEntity.class, new Box(pos));
		//				final BasicInventory inventory = new BasicInventory(entities.size());
		//
		//				entities.forEach(itemEntity -> {
		//					final ItemStack stack = itemEntity.getStack();
		//					inventory.add(stack);
		//				});
		//
		//				final Optional<IchorRecipe> match = world.getRecipeManager()
		//					.getFirstMatch(IchorRecipe.Type.INSTANCE, inventory, world);
		//
		//				if (match.isPresent()) {
		//					spawnCraftingResult(world, pos, match.get().getOutput());
		//
		//					for (final Ingredient ingredient : match.get().getIngredients()) {
		//						for (final ItemEntity testEntity : entities) {
		//							if (ingredient.method_8093(testEntity.getStack())) {
		//								testEntity.getStack().decrement(1);
		//								break;
		//							}
		//						}
		//					}
		//				}
		//			}
		//		}

		super.onEntityCollision(blockState, world, pos, entity);
	}

	//	private void spawnCraftingResult(final World world, final BlockPos pos, final ItemStack result) {
	//		final ItemEntity itemEntity = new ItemEntity(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, result);
	//		world.spawnEntity(itemEntity);
	//	}

	@Override
	public Fluid tryDrainFluid(final IWorld world, final BlockPos pos, final BlockState blockState) {
		if (blockState.get(LEVEL) == 0) {
			world.setBlockState(pos, DoomTreeTracker.isNear(world.getWorld(), pos) ? DoomBlockStates.MIASMA_STATE : Blocks.AIR.getDefaultState(), 11);
			return fluid;
		} else {
			return Fluids.EMPTY;
		}
	}

	@Override
	public void onBlockRemoved(final BlockState myState, final World world, final BlockPos blockPos, final BlockState newState, final boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		if (!world.isClient) {
			final FluidState fluidState = newState.getFluidState();

			if(fluidState != null && !fluidState.isEmpty() && fluidState.isStill() && newState.getBlock() instanceof FluidBlock) {
				return;
			}

			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}
}
