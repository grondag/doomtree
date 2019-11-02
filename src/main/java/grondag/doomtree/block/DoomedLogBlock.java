package grondag.doomtree.block;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomTags;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoomedLogBlock extends PillarBlock {

	public DoomedLogBlock(final Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockRemoved(final BlockState myState, final World world, final BlockPos blockPos, final BlockState newState, final boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);
		DoomTreeTracker.reportBreak(world, blockPos, false);
	}

	@Override
	public void afterBreak(final World world, final PlayerEntity player, final BlockPos pos, final BlockState blockState, final BlockEntity blockEntity, final ItemStack toolStack) {
		super.afterBreak(world, player, pos, blockState, blockEntity, toolStack);

		if (!toolStack.getItem().isIn(DoomTags.WARDED_ITEMS)) {
			DoomEffect.exposeToDoom(player, 4);

			if (!world.isClient) {
				float extraExhaustion = 0.01F;

				// if using a tool, take extra durability.  If not, then extra doom exposure for player
				if (toolStack.getItem().isDamageable()) {
					player.getMainHandStack().damage(3, player, p -> {
						p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
					});
				} else {
					extraExhaustion += 0.01F;
				}

				player.addExhaustion(extraExhaustion);
			}
		}
	}
}
