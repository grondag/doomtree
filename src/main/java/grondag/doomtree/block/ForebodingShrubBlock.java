package grondag.doomtree.block;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Precipitation;

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
		return BlockTags.SAND.contains(block) || block == Blocks.TERRACOTTA
				|| block == Blocks.GRAVEL || block == Blocks.CLAY  || block == DoomBlocks.GENERATIVE_MATRIX;
	}

	static long lastMessageTime = 0;
	
	@Override
	public boolean activate(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult blockhit) {
		if  (!world.isClient && player != null &&  player.getMainHandStack().getItem() ==  DoomItems.ALCHEMICAL_ENGINE) {
			final BlockPos.Mutable mPos = new BlockPos.Mutable();
			
			if (!isMatrixInAndAround(world, blockPos, mPos)) {
				sendMessageNoSpam(player, "help.doomtree.missing_matrix");
				return true;
			}
			
			if (!hasSurroundingBlocks(world, blockPos, mPos)) {
				sendMessageNoSpam(player, "help.doomtree.missing_blocks");
				return true;
			}
			
			if (!DoomTreeTracker.canGrow(world, blockPos)) {
				sendMessageNoSpam(player, "help.doomtree.bad_position");
				return true;
			}
			
			if (world.getBiome(blockPos).getPrecipitation() != Precipitation.RAIN) {
				sendMessageNoSpam(player, "help.doomtree.bad_biome");
				return true;
			}
			
			if (!world.isSkyVisible(blockPos)) {
				sendMessageNoSpam(player, "help.doomtree.no_sky");
				return true;
			}
			
			world.setBlockState(blockPos, DoomBlocks.DOOM_SAPLING_BLOCK.getDefaultState());
			player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
			return  true;
		}
		
		return super.activate(blockState, world, blockPos, player, hand, blockhit);
	}
	
	private static void sendMessageNoSpam(PlayerEntity player, String key) {
		final long newTime = player.world.getTime();
		
		if (Math.abs(lastMessageTime - newTime) >= 20) {
			player.sendMessage(new TranslatableText(key));
			lastMessageTime = newTime;
		}
	}
	
	public static boolean isMatrixInAndAround(World world, BlockPos pos, BlockPos.Mutable mPos) {
		final int x = pos.getX();
		final int y = pos.getY() - 1;
		final int z = pos.getZ();
		
		return isMatrix(world, mPos.set(x, y, z)) 
				&& isMatrix(world, mPos.set(x - 1, y, z - 1))
				&& isMatrix(world, mPos.set(x - 1, y, z))
				&& isMatrix(world, mPos.set(x - 1, y, z + 1))
				&& isMatrix(world, mPos.set(x, y, z - 1))
				&& isMatrix(world, mPos.set(x, y, z + 1))
				&& isMatrix(world, mPos.set(x + 1, y, z - 1))
				&& isMatrix(world, mPos.set(x + 1, y, z))
				&& isMatrix(world, mPos.set(x + 1, y, z + 1));
	}
	
	private static boolean isMatrix(World world, BlockPos pos) {
		return world.getBlockState(pos) == DoomBlocks.GENERATIVE_MATRIX.getDefaultState();
	}
	
	static final int PER_TARGET_COUNT = 8;
	static final int TARGET_SHIFT = 8;
	static final int COMBINED_TARGET_COUNT = (PER_TARGET_COUNT | (PER_TARGET_COUNT << TARGET_SHIFT));;
	
	public static boolean hasSurroundingBlocks(World world, BlockPos pos, BlockPos.Mutable mPos) {
		final int x = pos.getX();
		final int y = pos.getY() - 1;
		final int z = pos.getZ();
		
		int counts = 0;
		
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z - 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z + 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x - 2, y, z + 2), counts);
		
		counts = checkSurroundingBlocks(world, mPos.set(x - 1, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 1, y, z - 2), counts);
		
		counts = checkSurroundingBlocks(world, mPos.set(x - 1, y, z + 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x, y, z + 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 1, y, z + 2), counts);
		
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z - 2), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z - 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z + 1), counts);
		counts = checkSurroundingBlocks(world, mPos.set(x + 2, y, z + 2), counts);
		
		return counts == COMBINED_TARGET_COUNT;
	}
	
	private static int checkSurroundingBlocks(World world, BlockPos.Mutable mPos, int priorCounts) {
		final Block block = world.getBlockState(mPos).getBlock();
		
		if (block == Blocks.REDSTONE_BLOCK) {
			priorCounts += 1;
		} else if (block == Blocks.PRISMARINE || block == Blocks.NETHERRACK) {
			priorCounts += 1 << TARGET_SHIFT;
		}
		
		return priorCounts;
	}
}
