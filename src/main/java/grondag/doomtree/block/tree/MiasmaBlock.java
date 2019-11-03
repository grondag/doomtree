package grondag.doomtree.block.tree;

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.registry.DoomFluids;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class MiasmaBlock extends Block {

	public static Material MIASMA = new Material(
		MaterialColor.AIR,
		false, // liquid
		false, // solid
		false, // blocksMovement
		false, // blocksLight
		false, // breakByHand
		false, // burnable
		true,  // replaceable
		PistonBehavior.IGNORE);

	public MiasmaBlock() {
		super(FabricBlockSettings.of(MIASMA).build());
	}

	@Override
	public BlockRenderType getRenderType(final BlockState blockState) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getOutlineShape(final BlockState blockState, final BlockView blockView, final BlockPos blockPos, final EntityContext entityContext) {
		return VoxelShapes.empty();
	}

	@Override
	public void onBlockRemoved(final BlockState myState, final World world, final BlockPos blockPos, final BlockState newState, final boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		if (!world.isClient) {
			final FluidState fluidState = newState.getFluidState();

			if(fluidState != null && !fluidState.isEmpty() && fluidState.isStill() && newState.getBlock() instanceof FluidBlock) {
				if (DoomTreeTracker.isNear(world, blockPos)) {
					world.setBlockState(blockPos, fluidState.getFluid() == Fluids.LAVA
						? DoomBlockStates.DOOMED_STONE_STATE
							: DoomFluids.ICHOR.getDefaultState().getBlockState());
				}
			}

			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}

	@Override
	public boolean isAir(final BlockState blockState) {
		return false;
	}

	@Override
	public void onEntityCollision(final BlockState blockState, final World world, final BlockPos pos, final Entity entity) {
		DoomEffect.exposeToDoom(entity, 2);
	}
}