package grondag.doomtree.block;

import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardedStairsBlock extends StairsBlock {
	public WardedStairsBlock(BlockState baseBlockState, Settings settings) {
		super(baseBlockState, settings);
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		DoomTreeTracker.reportBreak(world, blockPos, false);
	}
}
