package grondag.doomtree.block.treeheart;

import static grondag.doomtree.block.treeheart.TreeUtils.MAX_TRUNK_HEIGHT;
import static grondag.doomtree.block.treeheart.TreeUtils.TRUNK_RADIUS;
import static grondag.doomtree.block.treeheart.TreeUtils.TRUNK_RADIUS_SQUARED;
import static grondag.doomtree.block.treeheart.TreeUtils.placeTrunkSection;

import java.util.Random;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class TrunkDesigner {

	final BlockPos.Mutable mPos = new BlockPos.Mutable();
	private final LongArrayList blocks = new LongArrayList();
	final IWorld world;
	final int x;
	final int y;
	final int z;
	final int centerHeight;
	final Random rand = new Random();

	boolean failed = false;
	int i = -TRUNK_RADIUS;
	int j = -TRUNK_RADIUS;

	public LongArrayList blocks()  {
		blocks.rem(BlockPos.asLong(x, y, z));
		return failed ? null : blocks;
	}

	public TrunkDesigner(final BlockPos pos, final IWorld world) {
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		this.world = world;
		centerHeight = y + MAX_TRUNK_HEIGHT;
	}

	/** returns true when done */
	public boolean designTrunk() {
		if (!doEeet()) {
			failed = true;
		}

		return failed || (j > TRUNK_RADIUS);
	}

	private boolean doEeet() {
		final int ds = i * i + j * j;

		if (ds < TRUNK_RADIUS_SQUARED) {
			final int dy = ds == 0 ? 0 : (int) Math.round(Math.sqrt(ds));
			if(!placeTrunkSection(world, blocks, mPos.set(x + i, 0, z + j), centerHeight - dy)) {
				return false;
			}
		}

		if (++i > TRUNK_RADIUS) {
			i = -TRUNK_RADIUS;
			j++;
		}

		return true;
	}
}
