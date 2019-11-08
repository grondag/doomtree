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
package grondag.doomtree.entity;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import grondag.doomtree.DoomTreeClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class WalkerEntityRenderer extends MobEntityRenderer<WalkerEntity, WalkerEntityModel> {
	private static final Identifier SKIN = DoomTreeClient.REGISTRAR.id("textures/entity/walker.png");

	public WalkerEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher, new WalkerEntityModel(), 0.5F);
		addFeature(new WalkerEntityModel.WalkerFeatureRenderer(this));
	}

	@Override
	protected Identifier getTexture(WalkerEntity entity) {
		return SKIN;
	}

	//TODO: needed?
	@Override
	protected void scale(WalkerEntity entity, float f1) {
		//		final float f2 = 1.2F;
		//		GlStateManager.scalef(f2, f2, f2);
		super.scale(entity, f1);
	}

	@Override
	public void render(WalkerEntity entity_1, double double_1, double double_2, double double_3, float float_1, float float_2) {
		final int int_2 = 0xF00F0 % 65536;
		final int int_3 = 0xF00F0 / 65536;
		GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, int_2, int_3);
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		final GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;
		gameRenderer.setFogBlack(true);
		GlStateManager.disableLighting();
		super.render(entity_1, double_1, double_2, double_3, float_1, float_2);

	}

	@Override
	public void applyLightmapCoordinates(WalkerEntity entity_1) {
		// TODO Auto-generated method stub
		super.applyLightmapCoordinates(entity_1);
	}
}
