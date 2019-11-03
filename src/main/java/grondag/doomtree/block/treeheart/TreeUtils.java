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
import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.registry.DoomTags;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.IWorld;

public enum TreeUtils {
	;

	static final int TRUNK_RADIUS = 3;
	static final int TRUNK_RADIUS_SQUARED = (int) ((TRUNK_RADIUS + 0.5f) * (TRUNK_RADIUS + 0.5f));
	static final int RADIUS = 48;
	static final int MAX_TRUNK_HEIGHT = 56;
	static final int MAX_GEN_HEIGHT = 64;


	static boolean canReplace(final IWorld world, final BlockPos pos) {
		return canReplace(world.getBlockState(pos));
	}

	static boolean canReplace(final BlockState blockState) {
		return blockState.isAir()
			|| blockState == DoomBlockStates.MIASMA_STATE
			|| blockState == DoomBlockStates.LEAF_STATE
			|| blockState == DoomBlockStates.GLEAM_STATE
			|| !blockState.getBlock().matches(DoomTags.PROTECTED_BLOCKS);
	}

	static int DISTANCES[] = new int[64];

	static {
		for (int i = 0; i < 64; i++) {
			DISTANCES[i] = (int) Math.round(Math.sqrt(i));
		}
	}

	static int squaredDistance(final int dx, final int dz) {
		return dx * dx + dz * dz;
	}

	static final int MIN_BRANCH_HEIGHT = DoomLogBlock.TERMINAL_HEIGHT + 1;

	public static int canopyRadius(final int height) {
		final int damping = height - MIN_BRANCH_HEIGHT;
		return (int) (36 * ((1f - (damping * damping) / 4096f)));
	}

	public static int canopyRadiusSquared(final int height) {
		final int r = canopyRadius(height);
		return r * r;
	}

	static BlockState logState(final BlockPos pos,final DoomHeartBlockEntity heart) {
		final BlockPos heartPos = heart.getPos();
		final int dy = pos.getY() - heart.getPos().getY();
		final int x  = heartPos.getX();
		final int z = heartPos.getZ();

		if (pos.getX() == x && Math.abs(pos.getZ() - z) == TRUNK_RADIUS || pos.getZ() == z && Math.abs(pos.getX() - x) == TRUNK_RADIUS) {
			if (dy < DoomLogBlock.TERMINAL_HEIGHT) {
				return DoomBlockStates.CHANNEL_STATE.with(DoomLogBlock.HEIGHT, MathHelper.clamp(dy, 0, DoomLogBlock.MAX_HEIGHT));
			} else if (dy == DoomLogBlock.TERMINAL_HEIGHT) {
				return DoomBlockStates.TERMINAL_STATE;
			}
		}

		return DoomBlockStates.LOG_STATE.with(DoomLogBlock.HEIGHT, MathHelper.clamp(dy, 0, DoomLogBlock.MAX_HEIGHT));
	}

	static boolean placeTrunkSection(final IWorld world, final LongArrayList blocks, final BlockPos.Mutable pos, final int height) {
		final int x = pos.getX();
		final int z = pos.getZ();
		final int limit = world.getTop(Type.MOTION_BLOCKING_NO_LEAVES, x, z);
		for (int y = 0; y <= height; y++) {
			pos.setY(y);

			if (y < limit) {
				final BlockState state = world.getBlockState(pos);

				if (state.getBlock() == Blocks.BEDROCK) continue;

				if (!TreeUtils.canReplace(state)) return false;
			}

			blocks.add(pos.asLong());
		}

		return true;
	}
}
