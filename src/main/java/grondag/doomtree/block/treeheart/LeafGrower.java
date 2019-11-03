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

import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.registry.DoomBlocks;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.World;

public class LeafGrower {
	static void growLeaves(final DoomHeartBlockEntity heart) {
		if (!heart.logs.hasBranches()) return;

		final BlockPos.Mutable mPos = heart.mPos;
		final Random r = ThreadLocalRandom.current();
		final World world = heart.getWorld();

		final BlockPos origin = heart.getPos();

		for (int i = 0; i < 8; i++) {
			final int rp = heart.logs.randomBranch(r);

			if (rp == Integer.MIN_VALUE) {
				return;
			}

			RelativePos.set(mPos, origin, rp);
			final long pos = mPos.asLong();

			if (world.getBlockState(mPos).getBlock() != DoomBlocks.DOOM_LOG) continue;

			final int placeCount = addLeaves(world, origin, heart.logs, pos, mPos, r);

			if (placeCount > 0) {
				heart.power -= placeCount * 5;
				return;
			}

			if (!heart.logs.hasBranches()) return;
		}
	}

	private static int addLeaves(final World world, final BlockPos origin, final LogTracker logs, final long pos, final Mutable mPos, final Random r)  {
		int placeCount = 0;

		for (int x = -4; x <= 4; x++) {
			for (int z = -4; z <= 4; z++) {
				final int sqd = x * x + z * z;

				if (x * x + z * z > 16) continue;

				if(sqd > 1) {
					placeCount += setLeaf(world, mPos.set(BlockPos.add(pos, x, 0, z)));
				}

				if (sqd > 9) continue;

				placeCount += setLeaf(world, mPos.set(BlockPos.add(pos, x, 1, z)));


				if(sqd > 1) {
					if(clearAround(logs, RelativePos.relativePos(origin, BlockPos.add(pos, x, 0, z)))) {
						placeCount += setLeaf(world, mPos.set(BlockPos.add(pos, x, -1, z)));
					}
				}

				if (sqd > 4) continue;

				placeCount += setLeaf(world, mPos.set(BlockPos.add(pos, x, 2, z)));

				if (sqd == 0) {
					placeCount += setLeaf(world, mPos.set(BlockPos.add(pos, x, 3, z)));
				}
			}
		}
		return placeCount;
	}

	private static boolean clearAround(final LogTracker logs, final int relPos) {
		final int x = RelativePos.rx(relPos);
		final int y = RelativePos.ry(relPos);
		final int z = RelativePos.rz(relPos);

		if (logs.contains(RelativePos.relativePos(x - 1, y, z - 1))) return false;
		if (logs.contains(RelativePos.relativePos(x - 1, y, z))) return false;
		if (logs.contains(RelativePos.relativePos(x - 1, y, z + 1))) return false;
		if (logs.contains(RelativePos.relativePos(x, y, z - 1))) return false;
		if (logs.contains(RelativePos.relativePos(x, y, z))) return false;
		if (logs.contains(RelativePos.relativePos(x, y, z + 1))) return false;
		if (logs.contains(RelativePos.relativePos(x + 1, y, z - 1))) return false;
		if (logs.contains(RelativePos.relativePos(x + 1, y, z))) return false;
		if (logs.contains(RelativePos.relativePos(x + 1, y, z + 1))) return false;

		return true;
	}

	private static int setLeaf(final World world, final BlockPos pos) {
		final BlockState state = world.getBlockState(pos);
		if (state.isAir() || state.getBlock() == DoomBlocks.MIASMA_BLOCK) {
			return world.setBlockState(pos, DoomBlockStates.LEAF_STATE, 18) ? 1 : 0;
		} else {
			return 0;
		}
	}
}
