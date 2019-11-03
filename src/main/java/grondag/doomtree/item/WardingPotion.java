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

import grondag.doomtree.entity.WardingEffect;
import grondag.doomtree.registry.DoomEffects;
import grondag.fermion.entity.StatusEffectAccess;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WardingPotion extends AbstractPotion {
	public WardingPotion(final Settings settings) {
		super(settings);
	}

	@Override
	public void applyEffects(final ItemStack stack, final World world, final PlayerEntity player) {
		final StatusEffectInstance warding = player.getStatusEffect(DoomEffects.WARDING_EFFECT);

		if (warding == null) {
			if (!world.isClient) {
				player.addPotionEffect(new StatusEffectInstance(DoomEffects.WARDING_EFFECT, WardingEffect.TICKS_PER_LEVEL, 0, false, false, true));
			}
		} else {
			System.out.println(Math.min(WardingEffect.TICKS_PER_LEVEL, WardingEffect.MAX_TICKS - warding.getDuration()));
			StatusEffectAccess.access(warding).fermion_addDuration(Math.min(WardingEffect.TICKS_PER_LEVEL, WardingEffect.MAX_TICKS - warding.getDuration()));
		}
	}
}
