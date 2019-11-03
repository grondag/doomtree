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
package grondag.doomtree.registry;

import net.minecraft.block.BlockState;

public enum DoomBlockStates {
	;

	public static final BlockState LOG_STATE = DoomBlocks.DOOM_LOG.getDefaultState();
	public static final BlockState CHANNEL_STATE = DoomBlocks.DOOM_LOG_CHANNEL.getDefaultState();
	public static final BlockState TERMINAL_STATE = DoomBlocks.DOOM_LOG_TERMINAL.getDefaultState();
	public static final BlockState LEAF_STATE = DoomBlocks.DOOM_LEAF.getDefaultState();
	public static final BlockState MIASMA_STATE = DoomBlocks.MIASMA_BLOCK.getDefaultState();
	public static final BlockState GLEAM_STATE = DoomBlocks.DOOM_GLEAM_BLOCK.getDefaultState();
	public static final BlockState DOOMED_LOG_STATE = DoomBlocks.DOOMED_LOG.getDefaultState();
	public static final BlockState DOOMED_EARTH_STATE = DoomBlocks.DOOMED_EARTH.getDefaultState();
	public static final BlockState DOOMED_STONE_STATE = DoomBlocks.DOOMED_STONE.getDefaultState();
	public static final BlockState DOOMED_DUST_STATE = DoomBlocks.DOOMED_DUST.getDefaultState();
}
