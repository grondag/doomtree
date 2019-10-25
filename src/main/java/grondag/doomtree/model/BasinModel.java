package grondag.doomtree.model;

import java.util.List;
import java.util.function.Function;

import grondag.doomtree.DoomTree;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class BasinModel extends AlchemicalModel {
	public static final List<Identifier> TEXTURES = DoomTree.REG.idList(
			"block/basin_base",
			"block/basin_feet",
			"block/basin_glass",
			"block/basin_inlay_a",
			"block/basin_inlay_b",
			"block/inert_basin_inlay_a",
			"block/inert_basin_inlay_b",
			"block/basin_side",
			"block/basin_rim");

	protected static final int BASE = 0;
	protected static final int FEET = 1;
	protected static final int GLASS = 2;
	protected static final int INLAY_A = 3;
	protected static final int INLAY_B = 4;
	protected static final int INERT_A = 5;
	protected static final int INERT_B = 6;
	protected static final int SIDE = 7;
	protected static final int RIM = 8;

	public static final int ACTIVE_COLOR = 0xFF80B0FF;

	protected BasinModel(Sprite sprite, Function<Identifier, Sprite> spriteMap, boolean isFrame) {
		super(sprite,spriteMap, TEXTURES, isFrame, ACTIVE_COLOR, PX3);
	}

	@Override
	protected boolean hasTranslucentSides() {
		return true;
	}
	
	@Override
	protected final void emitQuads(QuadEmitter qe, boolean lit) {
		emitFrameQuads(qe);
		emitGlassQuads(qe);
		emitInlayQuads(qe, lit);
	}

	private final void emitGlassQuads(QuadEmitter qe) {
		emitGlassQuadsInner(qe, PX15);
		emitGlassQuadsInner(qe, 0);
	}

	private final void emitGlassQuadsInner(QuadEmitter qe, float depth) {
		for (Direction face : SIDES) {
			qe.material(matTranslucent)
			.square(face, PX2, PX4, PX14, PX13, depth)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[GLASS], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
		}
	}

	private final void emitInlayQuads(QuadEmitter qe, boolean lit) {
		emitInlayQuadsInner(qe, PX15, lit);
		emitInlayQuadsInner(qe, 0, lit);
	}

	private final void emitInlayQuadsInner(QuadEmitter qe, float depth, boolean lit) {
		final int aSide = lit ? INLAY_A : INERT_A;
		final int bSide = lit ? INLAY_B : INERT_B;
		final RenderMaterial mat = lit ? matCutoutGlow : matCutout;

		for (Direction face : SIDES) {
			qe.material(mat)
			.square(face, PX2, PX7, PX14, PX15, depth)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[face.getAxis() == Axis.X ? aSide : bSide], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
		}
	}

	@Override
	protected final void emitFrameQuads(QuadEmitter qe) {
		qe.material(matSolid)
		.square(Direction.DOWN, 0, 0, 1, 1, PX2)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		qe.material(matCutout)
		.square(Direction.DOWN, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[FEET], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		qe.material(matSolid)
		.square(Direction.UP, 0, 0, 1, 1, PX13)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		qe.material(matCutout)
		.square(Direction.UP, 0, 0, 1, 1, 0)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[RIM], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		for (Direction face : SIDES) {
			// INNER SIDES BOTTOM
			qe.material(matCutout)
			.square(face, 0, 0, 1, PX2, PX14)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[SIDE], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
			
			// FRAME OUTER EDGES
			qe.material(matCutout)
			.square(face, 0, PX4, 1, PX14, PX14)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[BASE], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
			
			// OUTER SIDES
			qe.material(matCutout)
			.square(face, 0, 0, 1, 1, 0)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[SIDE], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
			
			// INNER SIDES TOP
			qe.material(matCutout)
			.square(face, PX1, PX3, PX15, 1, PX15)
			.spriteColor(0, -1, -1, -1, -1)
			.spriteBake(0, sprites[SIDE], MutableQuadView.BAKE_LOCK_UV);
			qe.emit();
		}

		// FRAME OUTER EDGES (Y-AXIS)
		qe.material(matCutout)
		.square(Direction.UP, 0, 0, 1, 1, PX12)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[RIM], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();

		qe.material(matCutout)
		.square(Direction.DOWN, 0, 0, 1, 1, PX13)
		.spriteColor(0, -1, -1, -1, -1)
		.spriteBake(0, sprites[RIM], MutableQuadView.BAKE_LOCK_UV);
		qe.emit();
	}

	public static BasinModel create(Function<Identifier, Sprite> spriteMap) {
		return new BasinModel(spriteMap.apply(TEXTURES.get(INERT_A)), spriteMap, false);
	}

	public static BasinModel createFrame(Function<Identifier, Sprite> spriteMap) {
		return new BasinModel(spriteMap.apply(TEXTURES.get(INERT_A)), spriteMap, true);
	}
}
