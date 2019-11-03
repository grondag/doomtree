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
package grondag.doomtree.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criterions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public abstract class AbstractPotion extends Item {

	public AbstractPotion(final Settings settings) {
		super(settings);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getStackForRender() {
		return PotionUtil.setPotion(super.getStackForRender(), Potions.WATER);
	}

	@Override
	public ItemStack finishUsing(final ItemStack stack, final World world, final LivingEntity entity) {
		final PlayerEntity player = entity instanceof PlayerEntity ? (PlayerEntity)entity : null;

		if (player == null || !player.abilities.creativeMode) {
			stack.decrement(1);
		}

		if (player instanceof ServerPlayerEntity) {
			Criterions.CONSUME_ITEM.handle((ServerPlayerEntity)player, stack);
		}

		applyEffects(stack, world, player);

		if (player != null) {
			player.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		if (player == null || !player.abilities.creativeMode) {
			if (stack.isEmpty()) {
				return new ItemStack(Items.GLASS_BOTTLE);
			}

			if (player != null) {
				player.inventory.insertStack(new ItemStack(Items.GLASS_BOTTLE));
			}
		}

		return stack;
	}

	protected abstract void applyEffects(final ItemStack stack, final World world, final PlayerEntity player);

	@Override
	public int getMaxUseTime(final ItemStack stack) {
		return 20;
	}

	@Override
	public UseAction getUseAction(final ItemStack stack) {
		return UseAction.DRINK;
	}

	@Override
	public TypedActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		player.setCurrentHand(hand);
		return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, player.getStackInHand(hand));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean hasEnchantmentGlint(final ItemStack stack) {
		return false;
	}
}
