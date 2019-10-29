package grondag.doomtree.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import grondag.doomtree.block.DoomSaplingBlockEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;

public class DoomSaplingBlockEntityRenderer extends BlockEntityRenderer<DoomSaplingBlockEntity> {
	@Override
	public void render(DoomSaplingBlockEntity sapling, double x, double y, double z, float tickDelta, int lightmap) {
		super.render(sapling, x, y, z, tickDelta, lightmap);

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder builder = tess.getBufferBuilder();
		GlStateManager.disableTexture();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
		double[] xOffset = new double[8];
		double[] zOffset = new double[8];
		double xNext = 0.0D;
		double zNext = 0.0D;
		final Random rand = sapling.renderRand;
		rand.setSeed(sapling.renderSeed);

		for(int i = 7; i >= 0; --i) {
			xOffset[i] = xNext;
			zOffset[i] = zNext;
			xNext += (rand.nextInt(11) - 5);
			zNext += (rand.nextInt(11) - 5);
		}

		for(int i = 0; i < 4; ++i) {
			rand.setSeed(sapling.renderSeed);

			for(int j = 0; j < 3; ++j) {
				int yMax = 7;
				int yMin = 0;

				if (j > 0) {
					yMax = 7 - j;
				}

				if (j > 0) {
					yMin = yMax - 2;
				}

				double xRand0 = xOffset[yMax] - xNext;
				double zRand0 = zOffset[yMax] - zNext;

				for(int yStep = yMax; yStep >= yMin; --yStep) {
					double xRand1 = xRand0;
					double zRand1 = zRand0;

					if (j == 0) {
						xRand0 += (rand.nextInt(11) - 5);
						zRand0 += (rand.nextInt(11) - 5);
					} else {
						xRand0 += (rand.nextInt(31) - 15);
						zRand0 += (rand.nextInt(31) - 15);
					}

					builder.begin(GL11.GL_TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
					double a = 0.05D + i * 0.1D; //0.1D + i * 0.2D;

					if (j == 0) {
						a *= yStep * 0.05D + 0.1; //* 0.1D + 1.0D;
					}

					double b = 0.05D + i * 0.1D; //0.1D + i * 0.2D;

					if (j == 0) {
						b *= (yStep - 1) * 0.05D + 0.1; //* 0.1D + 1.0D;
					}

					for(int k = 0; k < 5; ++k) {
						double x1 = x - a;
						double z1 = z - a;

						if (k == 1 || k == 2) {
							x1 += a * 2.0D;
						}

						if (k == 2 || k == 3) {
							z1 += a * 2.0D;
						}

						double x0 = x - b;
						double z0 = z - b;

						if (k == 1 || k == 2) {
							x0 += b * 2.0D;
						}

						if (k == 2 || k == 3) {
							z0 += b * 2.0D;
						}

						builder.vertex(x0 + xRand0, y + (yStep * 16), z0 + zRand0).color(0.45F, 0.45F, 0.5F, 0.3F).next();
						builder.vertex(x1 + xRand1, y + ((yStep + 1) * 16), z1 + zRand1).color(0.45F, 0.45F, 0.5F, 0.3F).next();
					}

					tess.draw();
				}
			}
		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture();
	}

	@Override
	public boolean method_3563(DoomSaplingBlockEntity sapling) {
		return true;
	}
}
