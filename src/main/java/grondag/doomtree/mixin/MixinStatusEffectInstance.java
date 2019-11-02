package grondag.doomtree.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import grondag.fermion.entity.StatusEffectAccess;
import net.minecraft.entity.effect.StatusEffectInstance;

@Mixin(StatusEffectInstance.class)
public class MixinStatusEffectInstance implements StatusEffectAccess {
	@Shadow private int duration;
	@Shadow private int amplifier;

	@Override
	public void fermion_setDuration(final int duration) {
		this.duration = duration;
	}

	@Override
	public void fermion_setAmplifier(final int amplifier) {
		this.amplifier = amplifier;
	}

	@Override
	public void fermion_addDuration(final int duration) {
		this.duration += duration;
	}
}
