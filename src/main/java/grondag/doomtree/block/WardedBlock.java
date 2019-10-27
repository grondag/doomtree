package grondag.doomtree.block;

import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardedBlock extends Block {
	public WardedBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		DoomTreeTracker.reportBreak(world, blockPos, false);
	}
}
