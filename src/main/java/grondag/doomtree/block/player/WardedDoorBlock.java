package grondag.doomtree.block.player;

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WardedDoorBlock extends DoorBlock {

	public WardedDoorBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		DoomTreeTracker.reportBreak(world, blockPos, false);
	}
}
