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
package grondag.doomtree.model;

import java.util.List;
import java.util.function.Function;

import grondag.doomtree.DoomTree;
import grondag.doomtree.block.tree.DoomLogBlock;
import grondag.fermion.client.models.SimpleModels;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class TerminalModel extends LogModel {
	public static final List<Identifier> TERMINAL_TEXTURES = DoomTree.REG.idList("block/doom_log_terminal");

	protected final Sprite termimnalSprite;

	protected TerminalModel(Sprite sprite, Function<Identifier, Sprite> spriteMap) {
		super(sprite, spriteMap);
		termimnalSprite = spriteMap.apply(TERMINAL_TEXTURES.get(0));
	}

	@Override
	protected int getHeightFromState(BlockState state) {
		return DoomLogBlock.TERMINAL_HEIGHT;
	}

	@Override
	protected int[] makeGlowColors() {
		return makeGlowColors(CHANNEL_LOW_COLOR, CHANNEL_HIGH_COLOR);
	}

	@Override
	protected void emitSideFace(QuadEmitter qe, Direction face, int bits, int height) {
		qe.material(outerMaterial)
		.square(face, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, termimnalSprite, MutableQuadView.BAKE_LOCK_UV);
		SimpleModels.contractUVs(0, termimnalSprite, qe);
		qe.emit();
	}

	public static TerminalModel create(Function<Identifier, Sprite> spriteMap) {
		return new TerminalModel(spriteMap.apply(TERMINAL_TEXTURES.get(0)), spriteMap);
	}
}
