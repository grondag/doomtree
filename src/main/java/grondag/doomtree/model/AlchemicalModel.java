package grondag.doomtree.model;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import grondag.doomtree.block.AlchemicalBlockEntity;
import grondag.doomtree.block.AlchemicalBlockEntity.Mode;
import grondag.doomtree.block.BasinBlockEntity;
import grondag.doomtree.registry.DoomFluids;
import grondag.fermion.client.models.SimpleModel;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.ModelHelper;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ExtendedBlockView;

// TODO: uses meshes for static portions of models

public abstract class AlchemicalModel extends SimpleModel {
	protected static final float PX1 = 1f / 16f;
	protected static final float PX2 = 2f / 16f;
	protected static final float PX3 = 3f / 16f;
	protected static final float PX4 = 4f / 16f;
	protected static final float PX6 = 6f / 16f;
	protected static final float PX7 = 7f / 16f;
	protected static final float PX8 = 8f / 16f;
	protected static final float PX9 = 9f / 16f;
	protected static final float PX12 = 12f / 16f;
	protected static final float PX13 = 13f / 16f;
	protected static final float PX14 = 14f / 16f;
	protected static final float PX15 = 15f / 16f;

	protected static final Direction[] SIDES = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
	
	protected final Renderer renderer = RendererAccess.INSTANCE.getRenderer();
	protected final RenderMaterial matCutout = renderer.materialFinder().blendMode(0, BlockRenderLayer.CUTOUT).find();
	protected final RenderMaterial matSolid = renderer.materialFinder().blendMode(0, BlockRenderLayer.SOLID).find();
	protected final RenderMaterial matTranslucent = renderer.materialFinder().blendMode(0, BlockRenderLayer.TRANSLUCENT).find();
	
	protected final RenderMaterial matSolidGlow = renderer.materialFinder().blendMode(0, BlockRenderLayer.SOLID).emissive(0, true)
			.disableAo(0, true).disableDiffuse(0, true).find();
	
	protected final RenderMaterial matCutoutGlow = renderer.materialFinder().blendMode(0, BlockRenderLayer.CUTOUT).emissive(0, true)
			.disableAo(0, true).disableDiffuse(0, true).find();

	protected final RenderMaterial matTranslucentGlow = renderer.materialFinder().blendMode(0, BlockRenderLayer.TRANSLUCENT).emissive(0, true)
			.disableAo(0, true).disableDiffuse(0, true).find();
	
	protected final List<Identifier> textures;
	protected final Sprite[] sprites;
	protected final Sprite waterSprite;
	protected final boolean isFrame;
	protected final int activeColor;

	protected final float bottomDepth;
	/** negative, because added to depth, making fluid stop surface less deep */
	protected final float depthSpan;
	protected final float levelMultiplier;

	public AlchemicalModel(
			Sprite sprite, 
			Function<Identifier, Sprite> spriteMap, 
			List<Identifier> textures, 
			boolean isFrame, 
			int activeColor,
			float bottomHeight) {
		super(sprite,  ModelHelper.MODEL_TRANSFORM_BLOCK);
		this.textures = textures;
		this.isFrame = isFrame;
		this.sprites = new Sprite[textures.size()];
		this.activeColor = activeColor;
		this.bottomDepth = 1f - bottomHeight;
		this.depthSpan = PX1 - bottomDepth;
		this.levelMultiplier = depthSpan / BasinBlockEntity.MAX_VISIBLE_LEVEL;

		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = spriteMap.apply(textures.get(i));
		}

		waterSprite = spriteMap.apply(new Identifier("minecraft:block/water_still"));
	}

	@Override
	public final void emitBlockQuads(ExtendedBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		final QuadEmitter qe = context.getEmitter();
		if (isFrame) {
			emitFrameQuads(qe);
		} else {
			Object be = ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);

			if (be == null || !(be instanceof AlchemicalBlockEntity)) {
				emitQuads(qe, false);
			} else {
				AlchemicalBlockEntity myBe = (AlchemicalBlockEntity) be;
				emitContents(qe, myBe);
				emitQuads(qe, myBe.mode() != Mode.WAKING);
			}
		}
	}

	protected abstract void emitQuads(QuadEmitter qe, boolean lit);

	protected abstract void emitFrameQuads(QuadEmitter qe);

	protected void emitContents(QuadEmitter qe, AlchemicalBlockEntity myBe) {
		switch (myBe.mode()) {
		case WAKING:
			renderFluidContent(qe, 0xFF000000 | DoomFluids.ICHOR_COLOR, myBe.visibleLevel(), false);
			break;

		case ACTIVE:
			renderFluidContent(qe, activeColor, myBe.visibleLevel(), true);
			renderFluidOverlay(qe, myBe.visibleLevel());
			break;

		case IDLE:
		default:
			break;
		}
	}
	
	protected boolean hasTranslucentSides() {
		return false;
	}

	protected void renderFluidContent(QuadEmitter qe, int color, int level, boolean glow) {
		final float depth = bottomDepth + levelMultiplier * level;
		final float height = Math.min(PX13, 1f - depth);
		final RenderMaterial mat = glow 
				? (hasTranslucentSides() ? matTranslucentGlow : matSolidGlow)
				: (hasTranslucentSides() ? matTranslucent : matSolid);
		
		qe.material(mat)
		.square(Direction.UP, PX1, PX1, PX15, PX15, depth)
		.spriteColor(0, color, color, color, color)
		.spriteBake(0, waterSprite, MutableQuadView.BAKE_LOCK_UV);
		qe.emit();
		
		if (hasTranslucentSides()) {
			for (Direction face : SIDES) {
				qe.material(mat)
				.square(face, PX2, PX4, PX14, height , PX1)
				.spriteColor(0, color, color, color, color)
				.spriteBake(0, waterSprite, MutableQuadView.BAKE_LOCK_UV);
				qe.emit();
			}
		}
	}

	protected void renderFluidOverlay(QuadEmitter qe, int level) {

	}

	@Override
	protected final Mesh createMesh() {
		MeshBuilder mb = renderer.meshBuilder();
		if (isFrame) {
			emitFrameQuads(mb.getEmitter());
		} else {
			emitQuads(mb.getEmitter(), false);
		}
		return mb.build();
	}
}
