package grondag.doomtree.entity;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class WardingEffect extends StatusEffect {
	public static final int TICKS_PER_LEVEL = 2 * 60 * 20;
	public static final int MAX_TICKS = TICKS_PER_LEVEL * 10;
	public static final int MAX_REFILL_TICKS = MAX_TICKS - TICKS_PER_LEVEL;
	public static final int COLOR = 0xFF80FFFF;
	public WardingEffect() {
		super(StatusEffectType.BENEFICIAL, COLOR);
	}

	@Override
	public boolean canApplyUpdateEffect(final int duration, final int amplifier) {
		return false;
	}

	@Override
	public void applyInstantEffect(@Nullable final Entity actor, @Nullable final Entity actorOwner, final LivingEntity target, final int duration, final double squaredDist) {
		// NOOP
	}

	@Override
	public void applyUpdateEffect(final LivingEntity target, final int amplifier) {
		// NO OP
	}
}