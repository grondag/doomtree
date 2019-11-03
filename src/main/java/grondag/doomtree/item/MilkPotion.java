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

import java.util.List;
import java.util.stream.Collectors;

import grondag.doomtree.registry.DoomEffects;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MilkPotion extends AbstractPotion {
	public MilkPotion(final Settings settings) {
		super(settings);
	}

	@Override
	public void applyEffects(final ItemStack stack, final World world, final PlayerEntity player) {
		final List<StatusEffectInstance> candidates = player.getStatusEffects()
			.stream()
			.filter(e -> !DoomEffects.isImmuneToMilk(e.getEffectType()))
			.collect(Collectors.toList());

		if (candidates.isEmpty()) return;

		final int size = candidates.size();

		player.removePotionEffect(candidates.get(HashCommon.mix(size) % size).getEffectType());
	}
}
