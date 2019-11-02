package grondag.doomtree.mixin;

import java.util.concurrent.atomic.AtomicInteger;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import grondag.doomtree.registry.DoomEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

@Mixin(MilkBucketItem.class)
public class MixinMilkBucketItem {
	private static final ThreadLocal<StatusEffectInstance> DOOM = new ThreadLocal<>();
	private static final AtomicInteger tauntCounter = new AtomicInteger();

	@Inject(method = "finishUsing", at = @At(value = "HEAD"))
	private void beforeFinishUsing(final ItemStack itemStack, final World world, final LivingEntity livingEntity, final CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			DOOM.set(livingEntity.hasStatusEffect(DoomEntities.DOOM_EFFECT) ? livingEntity.getStatusEffect(DoomEntities.DOOM_EFFECT) : null );
		}
	}

	@Inject(method = "finishUsing", at = @At(value = "RETURN"))
	private void afterFinishUsing(final ItemStack itemStack, final World world, final LivingEntity livingEntity, final CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			final StatusEffectInstance doom = DOOM.get();

			if (doom != null && !(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative())) {
				livingEntity.addPotionEffect(doom);

				if (livingEntity instanceof PlayerEntity) {
					livingEntity.sendMessage(new TranslatableText("taunt.doomtree.milk_" + (tauntCounter.getAndIncrement() & 3)));
				}
			}
		}
	}
}
