package grondag.doomtree.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ForebodingShrubBlock extends PlantBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

	public ForebodingShrubBlock(Block.Settings settings) {
		super(settings);
		setDefaultState(stateFactory.getDefaultState());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState blockState, World world, BlockPos pos, Entity entity) {
		if (entity instanceof LivingEntity && entity.getType() != EntityType.FOX) {
			entity.slowMovement(blockState, new Vec3d(0.800000011920929D, 0.75D, 0.800000011920929D));

			if (!world.isClient && (entity.prevRenderX != entity.x || entity.prevRenderZ != entity.z)) {
				double dx = Math.abs(entity.x - entity.prevRenderX);
				double dz = Math.abs(entity.z - entity.prevRenderZ);
				if (dx >= 0.003000000026077032D || dz >= 0.003000000026077032D) {
					entity.damage(DamageSource.MAGIC, 1.0F);
				}
			}

		}
	}

	@Override
	protected boolean canPlantOnTop(BlockState blockState, BlockView blockView, BlockPos pos) {
		if (super.canPlantOnTop(blockState, blockView, pos)) {
			return true;
		}

		final Block block = blockState.getBlock();
		return BlockTags.SAND.contains(block) || block == Blocks.TERRACOTTA || block == Blocks.GRAVEL || block == Blocks.CLAY;
	}
}
