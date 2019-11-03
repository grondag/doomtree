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

import java.util.Random;

import grondag.doomtree.block.tree.DoomLogBlock;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.util.math.BlockPos;

/** Tracks spaces that should be logs */
@SuppressWarnings("serial")
class LogTracker extends IntOpenHashSet {
	private final int originX;
	private final int originY;
	private final int originZ;

	private final IntArrayList branches = new IntArrayList();

	LogTracker(final BlockPos origin) {
		originX = origin.getX();
		originY = origin.getY();
		originZ = origin.getZ();
	}

	boolean contains(final long pos) {
		return super.contains(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	boolean contains(final BlockPos pos) {
		return super.contains(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	@Override
	public boolean contains(final int relativePos) {
		return super.contains(relativePos);
	}

	boolean add(final long pos) {
		return add(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	boolean add(final BlockPos pos) {
		return add(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	@Override
	public boolean add(final int relativePos) {
		final boolean result = super.add(relativePos);

		if (result
			&& RelativePos.ry(relativePos) > DoomLogBlock.TERMINAL_HEIGHT
			&& (Math.abs(RelativePos.rx(relativePos)) > TreeUtils.TRUNK_RADIUS
				|| Math.abs(RelativePos.rz(relativePos)) > TreeUtils.TRUNK_RADIUS)) {
			branches.add(relativePos);
		}

		return result;
	}

	void fromArray(final int[] relativePositions) {
		clear();
		branches.clear();
		for (final int pos : relativePositions) {
			add(pos);
		}
	}

	boolean hasBranches() {
		return !branches.isEmpty();
	}

	int randomBranch(final Random rand) {
		final int limit = branches.size();

		if (limit == 0) return Integer.MIN_VALUE;

		return branches.removeInt(limit == 1 ? 0 :rand.nextInt(limit));
	}
}
