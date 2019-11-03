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
import net.minecraft.text.TranslatableText;
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
					((ServerPlayerEntity) livingEntity).sendChatMessage(new TranslatableText("taunt.doomtree.milk_" + (tauntCounter.getAndIncrement() & 3)), MessageType.SYSTEM);
				}
			}
		}
	}
}
