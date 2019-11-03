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
	private int doomAddition;

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
		final int result = doomExposure + doomAddition;
		doomExposure = 0;
		doomAddition = 0;
		return result;
	}

	@Override
	public void exposeToDoom(final int doomExposure) {
		if (doomExposure > this.doomExposure) {
			this.doomExposure = doomExposure;
		}
	}

	@Override
	public void addToDoom(final int howMuch) {
		doomAddition += howMuch;
	}

	@Inject(method = "spawnPotionParticles", at = @At(value = "HEAD"))
	private void beforeSpawnPotionParticles(final CallbackInfo ci) {
		DoomEffect.beforeSpawnPotionParticles((LivingEntity)(Object) this);
	}
}
