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
import grondag.fermion.client.models.SimpleModels;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class ChannelModel extends LogModel {

	public static final List<Identifier> CHANNEL_TEXTURES = DoomTree.REG.idList(
		"block/doom_log_channel_0_0",
		"block/doom_log_channel_0_1",
		"block/doom_log_channel_0_2",
		"block/doom_log_channel_0_3");

	protected final Sprite[] channelSprite = new Sprite[4];

	protected ChannelModel(Sprite sprite, Function<Identifier, Sprite> spriteMap) {
		super(sprite, spriteMap);
		for (int i = 0; i < 4; i++) {
			channelSprite[i] = spriteMap.apply(CHANNEL_TEXTURES.get(i));
		}
	}

	@Override
	protected int[] makeGlowColors() {
		return makeGlowColors(CHANNEL_LOW_COLOR, CHANNEL_HIGH_COLOR);
	}

	@Override
	protected void emitSideFace(QuadEmitter qe, Direction face, int bits, int height) {
		final int logTexture = (bits >> 2) & 3;
		qe.material(outerMaterial)
		.square(face, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, channelSprite[logTexture], MutableQuadView.BAKE_LOCK_UV);
		SimpleModels.contractUVs(0, channelSprite[logTexture], qe);
		qe.emit();
	}

	public static ChannelModel create(Function<Identifier, Sprite> spriteMap) {
		return new ChannelModel(spriteMap.apply(CHANNEL_TEXTURES.get(0)), spriteMap);
	}
}
