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

import java.util.Random;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import grondag.doomtree.DoomTreeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class WalkerEntityModel extends BipedEntityModel<WalkerEntity> {

	private final static float Y_OFFSET = -10f;
	private final Random random = new Random();

	private void setRotationPoints() {
		body.rotationPointY = Y_OFFSET;
		body.rotationPointZ = -0.0F;
		rightArm.rotationPointZ = 0.0F;
		rightArm.rotationPointY = Y_OFFSET + 4.0F;

		leftArm.rotationPointZ = 0.0F;
		leftArm.rotationPointY = Y_OFFSET + 4.0F;

		rightLeg.rotationPointZ = 0.0F;
		rightLeg.rotationPointY = Y_OFFSET + 14.0F;

		leftLeg.rotationPointZ = 0.0F;
		leftLeg.rotationPointY = Y_OFFSET + 14.0F;

		head.rotationPointZ = -0.0F;
		head.rotationPointY = Y_OFFSET + 1;

		headwear.rotationPointX = head.rotationPointX;
		headwear.rotationPointY = head.rotationPointY;
		headwear.rotationPointZ = head.rotationPointZ;
	}

	private void init() {
		head = new Cuboid(this, 0, 0);
		head.addBox(-4F, -7.0F, -8.0F, 8, 8, 16, -1.50f);
		head.setRotationPoint(0.0F, Y_OFFSET + 1, 0.0F);

		headwear = new Cuboid(this, 0, 0);
		headwear.addBox(-4F, -7.0F, -8.0F, 8, 8, 16, -1.50f);
		headwear.visible = false;

		body = new Cuboid(this, 36, 32);
		body.addBox(-1F, -1.5F, -1.0F, 2, 18, 2, 0f);
		body.addBox(-6.0F, 4F, -1.0F, 12, 2, 2, 0f);
		body.setTextureOffset(32, 0);
		body.addBox(-3.0F, 3F, -2.0F, 6, 8, 4, 0f);
		body.setRotationPoint(0.0F, Y_OFFSET, 0.0F);

		rightArm = new Cuboid(this, 0, 0);
		rightArm.addBox(-2.5F, -1.0F, -1.5F, 3, 13, 3, 0f);

		rightArm.setTextureOffset(48, 59);
		rightArm.addBox(-3.5F, 11.0F, -0.5F, 1, 4, 1, 0.0f);
		rightArm.setTextureOffset(51, 59);
		rightArm.addBox(-1.5F, 11.0F, -2.5F, 1, 4, 1, 0.0f);
		rightArm.setTextureOffset(54, 59);
		rightArm.addBox(-1.5F, 11.0F,  1.5F, 1, 4, 1, 0.0f);
		rightArm.setRotationPoint(-5.0F, Y_OFFSET + 2.0F, 0.0F);

		leftArm = new Cuboid(this, 48, 12);
		leftArm.mirror = true;
		leftArm.addBox(-0.5F, -1.0F, -1.5F, 3, 13, 3, 0f);

		leftArm.setTextureOffset(48, 59);
		leftArm.addBox( 2.5F, 11.0F, -0.5F, 1, 4, 1, 0.0f);
		leftArm.setTextureOffset(51, 59);
		leftArm.addBox( 0.5F, 11.0F, -2.5F, 1, 4, 1, 0.0f);
		leftArm.setTextureOffset(54, 59);
		leftArm.addBox( 0.5F, 11.0F,  1.5F, 1, 4, 1, 0.0f);
		leftArm.setRotationPoint(5.0F, Y_OFFSET + 2.0F, 0.0F);

		rightLeg = new Cuboid(this, 0, 24);
		rightLeg.addBox(-3.0F, 0.0F, -1.5F, 3, 3, 6, 0F);

		rightLeg.setTextureOffset(0, 36);
		rightLeg.addBox(-3.0F, 3.0F, 1.5F, 3, 8, 3, 0F);

		rightLeg.setTextureOffset(0, 52);
		rightLeg.addBox(-3.0F, 11.0F, -1.5F, 3, 3, 6, 0F);

		rightLeg.setTextureOffset(36, 30);
		rightLeg.addBox(-3.0F, 14.0F, -1.5F, 3, 6, 3, 0F);

		rightLeg.setRotationPoint(-1.0F, Y_OFFSET + 9.0F, 0.0F);

		leftLeg.mirror = false;
		leftLeg = new Cuboid(this, 18, 24);
		leftLeg.addBox(-1.0F, 0.0F, -1.5F, 3, 3, 6, 0F);

		leftLeg.setTextureOffset(18, 36);
		leftLeg.addBox(-1.0F, 3.0F, 1.5F, 3, 8, 3, 0F);

		leftLeg.setTextureOffset(18, 52);
		leftLeg.addBox(-1.0F, 11.0F, -1.5F, 3, 3, 6, 0F);

		leftLeg.setTextureOffset(48, 30);
		leftLeg.addBox(-1.0F, 14.0F, -1.5F, 3, 6, 3, 0F);

		leftLeg.setRotationPoint(2.0F, Y_OFFSET + 9.0F, 0.0F);

		setRotationPoints();
	}

	public WalkerEntityModel() {
		super(0.0F, Y_OFFSET, 64, 64);

		init();

		// Used for interactive debug of model changes
		// InvalidateRenderStateCallback.EVENT.register(this::init);
	}

	@Override
	public void render(WalkerEntity entity_1, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
		super.render(entity_1, float_1, float_2, float_3, float_4, float_5, float_6);
	}

	@Override
	public void method_17088(WalkerEntity walker, float float_1, float float_2, float float_3, float float_4, float float_5, float tickDelta) {
		method_17087(walker, float_1, float_2, float_3, float_4, float_5, tickDelta);

		final int pulseCount = walker.pulseCount();

		if (pulseCount > 0) {
			random.setSeed(pulseCount);
			final float f = 0.002f * pulseCount;
			final float x = (float) (random.nextGaussian() * f);
			final float y = (float) (random.nextGaussian() * f);
			final float z = (float) (random.nextGaussian() * f);
			GlStateManager.pushMatrix();
			GlStateManager.translatef(x, y, z);
			head.render(tickDelta);
			//headwear.render(tickDelta);
			GlStateManager.popMatrix();
		} else {
			head.render(tickDelta);
			headwear.render(tickDelta);
		}

		body.render(tickDelta);
		rightArm.render(tickDelta);
		leftArm.render(tickDelta);
		rightLeg.render(tickDelta);
		leftLeg.render(tickDelta);

	}


	@Override
	public void method_17087(WalkerEntity walker, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6) {
		super.method_17087(walker, float_1, float_2, float_3, float_4, float_5, float_6);

		head.visible = true;
		headwear.visible = false;

		//		body.pitch = 0.0F;

		//		rightArm.pitch = rightArm.pitch * 0.5F;
		//		leftArm.pitch = leftArm.pitch * 0.5F;
		//		rightLeg.pitch = rightLeg.pitch * 0.5F;
		//		leftLeg.pitch = leftLeg.pitch * 0.5F;
		//
		//		if (rightArm.pitch > 0.4F) {
		//			rightArm.pitch = 0.4F;
		//		}
		//
		//		if (leftArm.pitch > 0.4F) {
		//			leftArm.pitch = 0.4F;
		//		}
		//
		//		if (rightArm.pitch < -0.4F) {
		//			rightArm.pitch = -0.4F;
		//		}
		//
		//		if (leftArm.pitch < -0.4F) {
		//			leftArm.pitch = -0.4F;
		//		}
		//
		//		if (rightLeg.pitch > 0.4F) {
		//			rightLeg.pitch = 0.4F;
		//		}
		//
		//		if (leftLeg.pitch > 0.4F) {
		//			leftLeg.pitch = 0.4F;
		//		}
		//
		//		if (rightLeg.pitch < -0.4F) {
		//			rightLeg.pitch = -0.4F;
		//		}
		//
		//		if (leftLeg.pitch < -0.4F) {
		//			leftLeg.pitch = -0.4F;
		//		}

		// headwear.pitch = head.pitch;
		// headwear.yaw = head.yaw;
		// headwear.roll = head.roll;

		setRotationPoints();
	}

	public static class WalkerFeatureRenderer extends FeatureRenderer<WalkerEntity, WalkerEntityModel> {
		private static final Identifier SKIN = DoomTreeClient.REGISTRAR.id("textures/entity/walker_cracks.png");

		public WalkerFeatureRenderer(FeatureRendererContext<WalkerEntity, WalkerEntityModel> featureRendererContext_1) {
			super(featureRendererContext_1);
		}

		@Override
		public void render(WalkerEntity entity, float float_1, float float_2, float float_3, float float_4, float float_5, float float_6, float float_7) {
			bindTexture(SKIN);
			final GameRenderer gameRenderer = MinecraftClient.getInstance().gameRenderer;
			gameRenderer.setFogBlack(false);
			GlStateManager.enableBlend();
			GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthMask(!entity.isInvisible());
			int light = entity.getLightmapCoordinates();
			if (entity.isOnFire()) {
				light = 15728880;
			}

			final int int_2 = light % 65536;
			final int int_3 = light / 65536;
			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, int_2, int_3);
			GlStateManager.enableLighting();
			GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			getModel().method_17088(entity, float_1, float_2, float_4, float_5, float_6, float_7);
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
		}

		@Override
		public boolean hasHurtOverlay() {
			return true;
		}
	}
}