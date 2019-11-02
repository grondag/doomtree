package grondag.doomtree.mixin;

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

	@Inject(method = "finishUsing", at = @At(value = "HEAD"))
	private void beforeFinishUsing(ItemStack itemStack, World world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			DOOM.set(livingEntity.hasStatusEffect(DoomEntities.DOOM_EFFECT) ? livingEntity.getStatusEffect(DoomEntities.DOOM_EFFECT) : null );
		}
	}
	
	@Inject(method = "finishUsing", at = @At(value = "RETURN"))
	private void afterFinishUsing(ItemStack itemStack, World world, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> ci) {
		if (!world.isClient) {
			StatusEffectInstance doom = DOOM.get();
			
			if (doom != null && !(livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).isCreative())) {
				livingEntity.addPotionEffect(doom);
				
				if (livingEntity instanceof PlayerEntity) {
					livingEntity.sendMessage(new TranslatableText("taunt.doomtree.milk"));
				}
			}
		}
	}
}
