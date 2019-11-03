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

import static grondag.doomtree.block.player.AlchemicalBlockEntity.UNITS_PER_FRAGMENT;

import grondag.doomtree.block.player.AlchemicalBlockEntity.Mode;
import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.registry.DoomRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BrazierBlock extends AlchemicalBlock {

	private static final ThreadLocal<BasicInventory> INVENTORY = ThreadLocal.withInitial(() -> new BasicInventory(1));

	public static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public BrazierBlock(Block.Settings settings) {
		super(settings, DoomRecipes.BRAZIER_RECIPE_TYPE);
		setDefaultState(stateFactory.getDefaultState().with(LIT, false));
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
		return OUTLINE_SHAPE;
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return RAY_TRACE_SHAPE;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		return new BrazierBlockEntity();
	}

	@Override
	int fuelValue(Item item) {
		return item == DoomItems.DOOM_FRAGMENT_ITEM || item == DoomItems.DOOM_LEAF_ITEM ? UNITS_PER_FRAGMENT : 0;
	}

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (super.activate(blockState, world, pos, player, hand, hit)) {
			return true;
		} else {
			return trySmelting(blockState, world, pos, player, hand);
		}
	}

	protected boolean trySmelting(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand) {
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

		if (mode != Mode.ACTIVE) {
			return false;
		}

		final Inventory inv = INVENTORY.get();
		inv.setInvStack(0, stack);
		final SmeltingRecipe smelt = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, inv, world).orElse(null);

		if (smelt == null) {
			return false;
		}

		final int currentUnits = myBe.units();

		final int newUnits = currentUnits - smelt.getCookTime() * AlchemicalBlockEntity.UNITS_PER_COOKTIME;

		if (newUnits >= 0 && !world.isClient) {
			myBe.setState(newUnits == 0 ? Mode.IDLE : Mode.ACTIVE, newUnits);

			if (!player.abilities.creativeMode) {
				stack.decrement(1);
			}

			final ItemStack result = smelt.getOutput().copy();

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
	}
}
