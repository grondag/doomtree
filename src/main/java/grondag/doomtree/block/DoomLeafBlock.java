package grondag.doomtree.block;

import grondag.doomtree.registry.DoomTags;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DoomLeafBlock extends Block {

	public final boolean isPlaced;

	protected final float progressFactor;

	public DoomLeafBlock(Block.Settings settings, boolean isPlaced, float progressFactor) {
		super(settings);
		this.isPlaced = isPlaced;
		this.progressFactor = progressFactor;
	}

	public static class Height extends DoomLeafBlock {
		public Height(Block.Settings settings, float progressFactor) {
			super(settings, false, progressFactor);
		}
	}

	@Override
	public float calcBlockBreakingDelta(BlockState blockState, PlayerEntity player, BlockView blockView, BlockPos pos) {
		if (isPlaced) {
			return super.calcBlockBreakingDelta(blockState, player, blockView, pos);
		}

		final ItemStack stack = player.inventory.getInvStack(player.inventory.selectedSlot);

		if (stack.isEmpty() || !DoomTags.isWardedOrEnchanted(stack)) {
			return 0;
		}

		return super.calcBlockBreakingDelta(blockState, player, blockView, pos) * progressFactor;
	}

	@Override
	public boolean isOpaque(BlockState blockState) {
		return true;
	}

//	@Override
//	public BlockRenderLayer getRenderLayer() {
//		return BlockRenderLayer.SOLID;
//	}

	@Override
	public boolean canSuffocate(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return true;
	}

	@Override
	public boolean allowsSpawning(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityType<?> entityType) {
		return false;
	}

//	@Override
//	public int getLightSubtracted(BlockState blockState, BlockView blockView, BlockPos blockPos) {
//		return 1;
//	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		if (!world.isClient) {
			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}
}
