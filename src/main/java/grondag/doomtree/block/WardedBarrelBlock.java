package grondag.doomtree.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WardedBarrelBlock extends BlockWithEntity {
	public static final DirectionProperty FACING;
	public static final BooleanProperty OPEN;

	public WardedBarrelBlock(Block.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
	}

	@Override
	public boolean activate(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return true;
		} else {
			BlockEntity be = world.getBlockEntity(pos);

			if (be instanceof WardedBarrelBlockEntity) {
				player.openContainer((WardedBarrelBlockEntity) be);
				player.incrementStat(Stats.OPEN_BARREL);
			}

			return true;
		}
	}

	@Override
	public void onBlockRemoved(BlockState blockState, World world, BlockPos pos, BlockState otherState, boolean flag) {
		if (blockState.getBlock() != otherState.getBlock()) {
			BlockEntity be = world.getBlockEntity(pos);

			if (be instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory)be);
				world.updateHorizontalAdjacent(pos, this);
			}

			super.onBlockRemoved(blockState, world, pos, otherState, flag);
		}
	}

	@Override
	public void onScheduledTick(BlockState blockState, World world, BlockPos pos, Random random) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof WardedBarrelBlockEntity) {
			((WardedBarrelBlockEntity) blockEntity).tick();
		}
	}

	@Override
	@Nullable
	public BlockEntity createBlockEntity(BlockView blockView) {
		return new WardedBarrelBlockEntity();
	}

	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.MODEL;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState blockState, @Nullable LivingEntity entity, ItemStack stack) {
		if (stack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof WardedBarrelBlockEntity) {
				((WardedBarrelBlockEntity) blockEntity).setCustomName(stack.getName());
			}
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState blockState) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState blockState, World world, BlockPos pos) {
		return Container.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockState rotate(BlockState blockState, BlockRotation rotation) {
		return blockState.with(FACING, rotation.rotate(blockState.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, BlockMirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.get(FACING)));
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
		builder.add(FACING, OPEN);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return this.getDefaultState().with(FACING, context.getPlayerLookDirection().getOpposite());
	}

	static {
		FACING = Properties.FACING;
		OPEN = Properties.OPEN;
	}
}
