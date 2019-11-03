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
package grondag.doomtree.block.treeheart;

import static grondag.doomtree.block.treeheart.TreeUtils.canReplace;

import it.unimi.dsi.fastutil.ints.IntHeapPriorityQueue;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** Tracks and builds in log spaces needing placement */
@SuppressWarnings("serial")
class TrunkBuilder extends IntHeapPriorityQueue {

	private final IntOpenHashSet set = new IntOpenHashSet();

	private final int originX;
	private final int originY;
	private final int originZ;

	TrunkBuilder(BlockPos origin) {
		super((i0, i1) -> Integer.compare(RelativePos.squaredDistance(i0), RelativePos.squaredDistance(i1)));

		originX = origin.getX();
		originY = origin.getY();
		originZ = origin.getZ();
	}

	void enqueue(long pos) {
		enqueue(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	void enqueue(BlockPos pos) {
		enqueue(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	@Override
	public void enqueue(int relativePos) {
		if (set.add(relativePos)) {
			super.enqueue(relativePos);
		}
	}

	@Override
	public int dequeueInt() {
		final int result = super.dequeueInt();
		set.remove(result);
		return result;
	}

	static final int PLACE_LIMIT = 16;
	static final int CHECK_LIMIT = 64;

	void build(DoomHeartBlockEntity heart) {
		final World world = heart.getWorld();
		final BlockPos.Mutable mPos = heart.mPos;
		int placeCount = 0;

		for (int i = 0; i < CHECK_LIMIT; i++) {
			if (heart.power >= 50 && !isEmpty()) {
				mPos.set(RelativePos.absolutePos(originX, originY, originZ, dequeueInt()));
				final BlockState currentState = world.getBlockState(mPos);

				if(currentState.getBlock() == Blocks.BEDROCK) {
					continue;
				}

				final BlockState targetState = TreeUtils.logState(mPos, heart);

				if (targetState != currentState && canReplace(world, mPos)) {
					world.setBlockState(mPos, targetState, 18);
					heart.power -= 50;

					if (++placeCount >= PLACE_LIMIT) break;
				}
			}
		}

		if (placeCount > 0) {
			heart.resetTickCounter();
		}
	}

	int buildDistanceSquared() {
		return isEmpty() ? Integer.MAX_VALUE : RelativePos.squaredDistance(firstInt());
	}
}
