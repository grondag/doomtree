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

import static grondag.doomtree.block.player.AlchemicalBlockEntity.MAX_UNITS;

import grondag.doomtree.block.player.AlchemicalBlockEntity.Mode;
import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.registry.DoomRecipes;
import grondag.fermion.recipe.AbstractSimpleRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlchemicalBlock extends BlockWithEntity {
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	protected final RecipeType<AbstractSimpleRecipe> recipeType;

	protected AlchemicalBlock(Settings settings, RecipeType<AbstractSimpleRecipe> recipeType) {
		super(settings);
		this.recipeType = recipeType;
	}

	@Override
	public boolean isOpaque(BlockState blockState) {
		return false;
	}

	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Override
	public int getLuminance(BlockState blockState) {
		return blockState.get(LIT) ? 7 : 4;
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(LIT);
	}

	abstract int fuelValue(Item item);

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final ItemStack stack = player.getStackInHand(hand);

		if (stack.isEmpty()) {
			return false;
		}

		final BlockEntity be = world.getBlockEntity(pos);

		if (be == null || !(be instanceof AlchemicalBlockEntity)) {
			return false;
		}

		final AlchemicalBlockEntity myBe = (AlchemicalBlockEntity) be;
		final Mode mode = myBe.mode();
		final Item item = stack.getItem();
		final int currentUnits = myBe.units();
		final int limit = Math.max(0, MAX_UNITS - currentUnits);

		final int fuelValue = fuelValue(item);

		if (fuelValue > 0) {
			if (limit > 0 && fuelValue <= limit && (mode == Mode.IDLE || mode == Mode.ACTIVE)) {
				final int consumed = Math.min(limit / fuelValue, 1);

				if (consumed > 0 && !world.isClient) {
					if (!player.abilities.creativeMode) {
						stack.decrement(consumed);
					}

					myBe.sendCraftingParticles();

					myBe.setState(Mode.ACTIVE, currentUnits + consumed * fuelValue);
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);

					if (mode != Mode.ACTIVE) {
						world.setBlockState(pos, blockState.with(LIT, true), 3);
					}
				}
			}

			return true;
		}

		if (mode == Mode.ACTIVE) {
			final AbstractSimpleRecipe recipe = DoomRecipes.HELPER.get(recipeType, stack);

			if (recipe != null) {
				final int newUnits = currentUnits - recipe.cost;

				if (newUnits >= 0 && !world.isClient) {
					myBe.setState(newUnits == 0 ? Mode.IDLE : Mode.ACTIVE, newUnits);

					if (!player.abilities.creativeMode) {
						stack.decrement(1);
					}

					final ItemStack result = recipe.result.copy();

					if (stack.isEmpty()) {
						player.setStackInHand(hand, result);
					} else if (!player.inventory.insertStack(result)) {
						player.dropItem(result, false);
					}

					myBe.sendCraftingParticles();

					if (newUnits == 0) {
						world.setBlockState(pos, blockState.with(LIT, false), 3);
					}
				}

				return true;
			} else {
				return handleActiveRecipe(blockState, world, pos, myBe, player, hand, stack, currentUnits);
			}
		}

		return false;
	}

	protected boolean handleActiveRecipe(BlockState blockState, World world, BlockPos pos, AlchemicalBlockEntity blockEntity, PlayerEntity player, Hand hand, ItemStack stack, int currentUnits) {
		return false;
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		DoomTreeTracker.reportBreak(world, blockPos, false);
	}
}
