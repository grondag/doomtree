package grondag.doomtree.block;

import static grondag.doomtree.block.AlchemicalBlockEntity.MAX_UNITS;

import grondag.doomtree.block.AlchemicalBlockEntity.Mode;
import grondag.doomtree.registry.DoomItems;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InertAlchemicalBlock extends Block {
	final AlchemicalBlock baseBlock;
	
	public InertAlchemicalBlock(AlchemicalBlock baseBlock) {
		super(FabricBlockSettings.copy(baseBlock).build());
		this.baseBlock = baseBlock;
	}
	
	@Override
	public boolean isOpaque(BlockState blockState) {
		return false;
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, EntityContext entityContext) {
		return baseBlock.getOutlineShape(blockState, blockView, blockPos, entityContext);
	}

	@Override
	public VoxelShape getRayTraceShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
		return baseBlock.getRayTraceShape(blockState, blockView, blockPos);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState blockState) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	public boolean activate(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final ItemStack stack = player.getStackInHand(hand);

		if (stack.isEmpty()) {
			return true;
		};

		final Item item = stack.getItem();

		if (item == DoomItems.ICHOR_BUCKET) {
			if (!world.isClient) {
				if (!player.abilities.creativeMode) {
					player.setStackInHand(hand, new ItemStack(Items.BUCKET));
				}

				world.setBlockState(pos, baseBlock.getDefaultState());
				final BlockEntity be = world.getBlockEntity(pos);

				if (be != null && be instanceof AlchemicalBlockEntity) {
					((AlchemicalBlockEntity) be).setState(Mode.WAKING, MAX_UNITS);
				}

				world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			return true;
		} else {
			return false;
		}
	}
}
