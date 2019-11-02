package grondag.doomtree.block;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DoomGleamBlock extends Block {
	public DoomGleamBlock() {
		super(FabricBlockSettings.copy(Blocks.AIR).build());
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
			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}

	@Override
	public int getLuminance(final BlockState blockState) {
		return 7;
	}

	//		@Override
	//		@Environment(EnvType.CLIENT)
	//		public void randomDisplayTick(BlockState blockState, World world, BlockPos pos, Random rand) {
	//			final double x = pos.getX() + rand.nextDouble();
	//			final double y = pos.getY() + rand.nextDouble();
	//			final double z = pos.getZ() + rand.nextDouble();
	//			world.addParticle(ParticleTypes.PORTAL, x, y, z, 0.0D, 0.0D, 0.0D);
	//		}

	@Override
	public void onEntityCollision(final BlockState blockState, final World world, final BlockPos pos, final Entity entity) {
		DoomEffect.exposeToDoom(entity, 2);
	}
}
