package grondag.doomtree.block;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomTags;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DoomedBlock extends Block {

	public DoomedBlock(final Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockRemoved(final BlockState myState, final World world, final BlockPos blockPos, final BlockState newState, final boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);
		DoomTreeTracker.reportBreak(world, blockPos, false);
	}

	@Override
	public void onBreak(final World world, final BlockPos pos, final BlockState blockState, final PlayerEntity player) {
		final ItemStack toolStack = player.getMainHandStack();

		if (!toolStack.getItem().isIn(DoomTags.WARDED_ITEMS)) {
			DoomEffect.addToDoom(player, 20);

			if (!world.isClient) {
				float extraExhaustion = 0.01F;

				// if using a tool, take extra durability.  If not, then extra doom exposure for player
				if (toolStack.getItem().isDamageable()) {
					toolStack.damage(3, player, p -> {
						p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
					});
				} else {
					extraExhaustion += 0.01F;
				}

				player.addExhaustion(extraExhaustion);
			}
		}

		super.onBreak(world, pos, blockState, player);
	}
}
