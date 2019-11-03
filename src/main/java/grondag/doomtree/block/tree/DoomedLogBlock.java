/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package grondag.doomtree.block.tree;

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
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
	public void onBreak(final World world, final BlockPos pos, final BlockState blockState, final PlayerEntity player) {
		final ItemStack toolStack = player.getMainHandStack();

		if (!toolStack.getItem().isIn(DoomTags.WARDED_ITEMS)) {
			DoomEffect.addToDoom(player, 80);

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
