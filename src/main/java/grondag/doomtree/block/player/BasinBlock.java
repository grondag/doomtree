package grondag.doomtree.block.player;

import static grondag.doomtree.block.player.AlchemicalBlockEntity.UNITS_PER_BUCKET;
import static grondag.doomtree.block.player.AlchemicalBlockEntity.UNITS_PER_INGOT;

import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.registry.DoomRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.Item;
import net.minecraft.util.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BasinBlock extends AlchemicalBlock {
	public static final VoxelShape RAY_TRACE_SHAPE = createCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
	public static final VoxelShape OUTLINE_SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.union(createCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), createCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), RAY_TRACE_SHAPE), BooleanBiFunction.ONLY_FIRST);

	public BasinBlock(Block.Settings settings) {
		super(settings, DoomRecipes.BASIN_RECIPE_TYPE);
		setDefaultState(this.stateFactory.getDefaultState().with(LIT, false));
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
		return new BasinBlockEntity();
	}
	
	@Override
	int fuelValue(Item item) {
		return item == DoomItems.WARDING_ESSENCE_ITEM ? UNITS_PER_INGOT
				: item == DoomItems.WARDING_ESSENCE_BLOCK_ITEM ? UNITS_PER_BUCKET : 0;
	}
}
