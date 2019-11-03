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
package grondag.doomtree.mixin;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import grondag.doomtree.registry.DoomEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

@Mixin(MilkBucketItem.class)
public class MixinMilkBucketItem {
	private static final ThreadLocal<ArrayList<StatusEffectInstance>> KEEPERS = ThreadLocal.withInitial(ArrayList::new);
	private static final AtomicInteger tauntCounter = new AtomicInteger();

	@Inject(method = "finishUsing", at = @At(value = "HEAD"))
	private void beforeFinishUsing(final ItemStack itemStack, final World world, final LivingEntity livingEntity, final CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			final ArrayList<StatusEffectInstance> keepers = KEEPERS.get();
			keepers.clear();
			final StatusEffectInstance doom = livingEntity.getStatusEffect(DoomEffects.DOOM_EFFECT);

			if (doom != null) {
				keepers.add(doom);
			}

			final StatusEffectInstance warding = livingEntity.getStatusEffect(DoomEffects.WARDING_EFFECT);

			if (warding != null) {
				keepers.add(warding);
			}
		}
	}

	@Inject(method = "finishUsing", at = @At(value = "RETURN"))
	private void afterFinishUsing(final ItemStack itemStack, final World world, final LivingEntity livingEntity, final CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			final ArrayList<StatusEffectInstance> keepers = KEEPERS.get();

			if (!keepers.isEmpty() && !(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative())) {
				boolean taunt = false;
				for (final StatusEffectInstance pe : keepers) {
					livingEntity.addPotionEffect(pe);
					taunt |= pe.getEffectType() == DoomEffects.DOOM_EFFECT;
				}

				if (taunt && livingEntity instanceof ServerPlayerEntity) {
					((ServerPlayerEntity) livingEntity).sendChatMessage(
						new TranslatableText("taunt.doomtree.milk_" + (tauntCounter.getAndIncrement() & 3))
						.setStyle(new Style().setColor(Formatting.LIGHT_PURPLE)), MessageType.SYSTEM);
				}
			}
		}
	}
}
