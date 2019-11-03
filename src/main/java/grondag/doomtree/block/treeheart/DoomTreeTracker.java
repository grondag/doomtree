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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class DoomTreeTracker {
	private DoomTreeTracker() {}

	private static class TreeData {
		private final int dimId;
		private final long packedPos;
		private final int x;
		private final int z;
		private final BlockPos pos;

		TreeData(int dimId, BlockPos pos) {
			this.dimId = dimId;
			this.pos = pos;
			packedPos = pos.asLong();
			x = pos.getX();
			z = pos.getZ();
		}

		boolean isNear(BlockPos pos) {
			return horizontalDistance(pos) <=  LIMIT;
		}

		int horizontalDistance(BlockPos pos) {
			final int dx = pos.getX() - x;
			final int dz = pos.getZ() - z;

			return dx * dx + dz * dz;
		}
	}

	static final int LIMIT = TreeUtils.RADIUS * TreeUtils.RADIUS;

	static final int GROW_LIMIT = LIMIT * 4;

	// TODO: make configurable
	static final int MAX_TREES = 3;


	static final ObjectArrayList<TreeData> TREES = new ObjectArrayList<>(MAX_TREES);

	public static void clear() {
		TREES.clear();
	}

	static void track(World world, BlockPos pos) {
		if (!world.isClient && get(world, pos) == null) {
			TREES.add(new TreeData(world.dimension.getType().getRawId(), pos));
		}
	}

	static void untrack(World world, BlockPos pos) {
		if (!world.isClient) {
			final TreeData tree = get(world, pos);

			if (tree != null) {
				TREES.remove(tree);
			}
		}
	}

	private static TreeData get(World world, BlockPos pos) {
		final int dim = world.dimension.getType().getRawId();
		final long p = pos.asLong();

		for (final TreeData t : TREES) {
			if (t.dimId == dim && t.packedPos == p) {
				return t;
			}
		}

		return null;
	}

	private static TreeData getNear(World world, BlockPos pos) {
		final int dim = world.dimension.getType().getRawId();

		for (final TreeData t : TREES) {
			if (t.dimId == dim && t.isNear(pos)) {
				return t;
			}
		}

		return null;
	}

	public static boolean isNear(World world, BlockPos pos) {
		return getNear(world, pos) != null;
	}

	public static void reportBreak(World world, BlockPos pos, boolean isLog) {
		if (world.isClient) return;

		final TreeData t = getNear(world, pos);

		if (t != null) {
			final BlockEntity be = world.getBlockEntity(t.pos);

			if (be != null && be instanceof DoomHeartBlockEntity) {
				((DoomHeartBlockEntity) be).reportBreak(pos, isLog);
			}
		}
	}

	public static boolean canGrow(World world, BlockPos pos) {
		if (TREES.size() >= MAX_TREES || (255 - pos.getY()) < 64) return false;

		final int dim = world.dimension.getType().getRawId();

		for (final TreeData t : TREES) {
			if (t.dimId == dim && t.horizontalDistance(pos) < GROW_LIMIT) {
				return false;
			}
		}

		return true;
	}

}
