package grondag.doomtree.block;

import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardedSlabBlock extends SlabBlock {
	public WardedSlabBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		DoomTreeTracker.reportBreak(world, blockPos, false);
	}
}
