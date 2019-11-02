package grondag.doomtree.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.entity.DoomEntityAccess;
import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements DoomEntityAccess {
	private int doomExposure;

	public MixinLivingEntity(final EntityType<?> type, final World world) {
		super(type, world);
	}

	@Inject(method = "isClimbing", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void isClimbing(final CallbackInfoReturnable<Boolean> cir, final BlockState state) {
		if (state.getBlock() == DoomBlocks.WARDED_LADDER) {
			cir.setReturnValue(true);
		}
	}

	@Override
	public int getAndClearDoomExposure() {
		final int result = doomExposure;
		doomExposure = 0;
		return result;
	}

	@Override
	public int exposeToDoom(final int doomExposure) {
		if (doomExposure > this.doomExposure) {
			this.doomExposure = doomExposure;
		}
		return this.doomExposure;
	}

	@Inject(method = "spawnPotionParticles", at = @At(value = "HEAD"))
	private void beforeSpawnPotionParticles(final CallbackInfo ci) {
		DoomEffect.beforeSpawnPotionParticles((LivingEntity)(Object) this);
	}
}
