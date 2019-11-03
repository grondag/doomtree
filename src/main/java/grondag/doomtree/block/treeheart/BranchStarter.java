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

import grondag.doomtree.block.tree.DoomLogBlock;
import net.minecraft.util.math.Direction;

public class BranchStarter  {

	private static final Direction[] BRANCH_FACES = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

	static Direction branchFace(final int y) {
		int dy = y - DoomLogBlock.TERMINAL_HEIGHT + 2;
		dy /= 2;
		return BRANCH_FACES[dy & 3];
	}

	static Job addBranches(final DoomHeartBlockEntity heart) {
		int y = DoomLogBlock.TERMINAL_HEIGHT + 2;
		final TrunkBuilder builds = heart.builds;
		final LogTracker logs = heart.logs;
		final BranchBuilder branches = heart.branches;
		final int limit = TreeUtils.MAX_TRUNK_HEIGHT - 4;

		while (y < limit) {
			addBranch(builds, logs, branches, y);
			y += 2;
		}

		heart.markDirty();

		return null;
	}

	private static void addBranch(final TrunkBuilder builds, final LogTracker logs, final BranchBuilder branches, final int y) {
		final Direction face = branchFace(y);
		final int relPos = RelativePos.relativePos(face.getOffsetX() * (TreeUtils.TRUNK_RADIUS + 1), y, face.getOffsetZ() * (TreeUtils.TRUNK_RADIUS + 1));

		if (!logs.contains(relPos)) {
			logs.add(relPos);
			builds.enqueue(relPos);
			branches.enqueue(relPos);
		}
	}
}
