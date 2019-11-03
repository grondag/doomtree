package grondag.doomtree.entity;

import java.util.Iterator;

import javax.annotation.Nullable;

import grondag.doomtree.registry.DoomEffects;
import grondag.doomtree.registry.DoomTags;
import grondag.fermion.entity.StatusEffectAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AbstractEntityAttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class DoomEffect extends StatusEffect {
	private static final int[] AMPLIFIER_DURATION_SECONDS = {120, 90, 60, 50, 40, 30, 25, 20, 15, 10};
	private static final int[] AMPLIFIER_DURATION_TICKS = new int[AMPLIFIER_DURATION_SECONDS.length];
	public static final int MAX_AMPLIFIER = AMPLIFIER_DURATION_SECONDS.length - 1;
	public static final int MAX_AMPLIFIER_DURATION_TICKS;

	static {
		for (int i = 0; i <= MAX_AMPLIFIER; i++) {
			AMPLIFIER_DURATION_TICKS[i] = AMPLIFIER_DURATION_SECONDS[i] * 20;
		}

		MAX_AMPLIFIER_DURATION_TICKS = AMPLIFIER_DURATION_TICKS[MAX_AMPLIFIER];
	}

	public static int durationTicks(final int amplifier) {
		return AMPLIFIER_DURATION_TICKS[MathHelper.clamp(amplifier, 0, MAX_AMPLIFIER)];
	}

	public DoomEffect() {
		super(StatusEffectType.HARMFUL, 0x808000);
	}

	@Override
	public boolean canApplyUpdateEffect(final int duration, final int amplifier) {
		return true;
	}

	@Override
	public void applyInstantEffect(@Nullable final Entity actor, @Nullable final Entity actorOwner, final LivingEntity target, final int duration, final double squaredDist) {
		// NOOP
	}

	/** result is two ints packed in a long to avoid allocating tuples.  Amplitude is high side */
	private static long calcDoom(final LivingEntity entity, @Nullable final StatusEffectInstance doom ) {
		if (entity == null) return 0;

		int exposure  = ((DoomEntityAccess) entity).getAndClearDoomExposure();

		if(exposure == 0 && doom == null) return 0;

		final int currentAmplifier = doom == null ? 0 : doom.getAmplifier();
		final int currentDuration = doom == null ? 0 : doom.getDuration();

		final int resistance = Math.round(doomResistance(entity) * 8);

		if (resistance > 0) {
			exposure -= Math.min(resistance, exposure);
		}

		int newDuration = currentDuration + exposure;
		int newAmplifier = currentAmplifier;

		int maxDuration = durationTicks(newAmplifier);

		// +1 because will lose a tick
		while (newDuration > maxDuration + 1 && newAmplifier < MAX_AMPLIFIER) {
			newDuration -= maxDuration;
			maxDuration = durationTicks(++newAmplifier);
		}

		if (newAmplifier > 0 && newDuration == 1) {
			newDuration = durationTicks(newAmplifier - 1) + 1;
			newAmplifier--;
		}

		if (newAmplifier == MAX_AMPLIFIER && newDuration > MAX_AMPLIFIER_DURATION_TICKS) {
			newDuration = MAX_AMPLIFIER_DURATION_TICKS;
		}

		return ((long)newAmplifier << 32) | newDuration;
	}

	@Override
	public void applyUpdateEffect(final LivingEntity target, final int amplifier) {
		// NO OP
	}

	// adds modifiers
	@Override
	public void method_5555(final LivingEntity livingEntity_1, final AbstractEntityAttributeContainer abstractEntityAttributeContainer_1, final int int_1) {
		super.method_5555(livingEntity_1, abstractEntityAttributeContainer_1, int_1);
	}

	// removes modifiers
	@Override
	public void method_5562(final LivingEntity livingEntity_1, final AbstractEntityAttributeContainer abstractEntityAttributeContainer_1, final int int_1) {
		super.method_5562(livingEntity_1, abstractEntityAttributeContainer_1, int_1);
	}

	public static void exposeToDoom(final Entity e, final int exposure) {
		// TODO: not only players?
		if (canDoom(e) && e instanceof PlayerEntity) {
			((DoomEntityAccess) e).exposeToDoom(exposure);
		}
	}

	public static void addToDoom(final Entity e, final int exposure) {
		// TODO: not only players?
		if (canDoom(e) && e instanceof PlayerEntity) {
			((DoomEntityAccess) e).addToDoom(exposure);
		}
	}

	/**
	 * Does not check {@link canDoom}.
	 * Do that first.
	 *
	 * @return 0 =  fully susceptible, >= 1 fully immune, with values in
	 * between  based on gear and potion effects.
	 */
	public static float doomResistance(final LivingEntity e) {
		final Iterable<ItemStack> armor = e.getArmorItems();

		final float potion = e.hasStatusEffect(DoomEffects.WARDING_EFFECT) ? 0.25f : 0;

		if(armor == null) return potion;

		final Iterator<ItemStack> it = armor.iterator();

		int enchantCount = 0;
		int wardCount = 0;
		int encrustedCount = 0;

		while (it.hasNext()) {
			final ItemStack stack = it.next();

			if (DoomTags.WARDED_ITEMS.contains(stack.getItem())) {
				wardCount++;

				if (DoomTags.ENCRUSTED_ITEMS.contains(stack.getItem())) {
					encrustedCount++;
				}
			} else if (stack.hasEnchantments()) {
				enchantCount++;
			}
		}

		if (wardCount ==  4) {
			return potion + 0.25f + 0.125f * encrustedCount;
		} else {
			return potion + (enchantCount == 4 ? 0.125f : 0);
		}
	}

	public static boolean canDoom(final Entity e) {
		return e instanceof LivingEntity
			&& e.isAlive()
			&& !e.isInvulnerable()
			&& !e.isSpectator()
			&& ((LivingEntity) e).getGroup() != EntityGroup.UNDEAD
			&& !DoomTags.UNDOOMED.contains(e.getType()) ;
	}

	public static void beforeSpawnPotionParticles(final LivingEntity me) {
		StatusEffectInstance doom = me.getStatusEffect(DoomEffects.DOOM_EFFECT);

		final boolean isClient = me.world == null || me.world.isClient;

		final long doomVals = DoomEffect.calcDoom(me, doom);
		if (doomVals == 0) return;

		final int duration = (int) (doomVals & 0xFFFFFFFFL);
		final int amplifier = (int) (doomVals >>> 32 & 0xFFFFFFFFL);

		if (doom == null) {
			if (isClient) return;

			doom = new StatusEffectInstance(DoomEffects.DOOM_EFFECT, duration, amplifier, false, false, true);
			me.addPotionEffect(doom);
		} else if (duration != doom.getDuration() || amplifier != doom.getAmplifier()) {
			StatusEffectAccess.access(doom).fermion_set(duration, amplifier);
		}

		int hunger = -1;
		int frailty = -1;

		switch (amplifier) {
		case 9:
			// Damage
			if (!isClient && me.world.getTime() % 20 == 0) {
				me.damage(DoomEffects.DOOM, 1.0F);
			}

		case 8:
			// slowness
			final StatusEffectInstance slowness = me.getStatusEffect(StatusEffects.SLOWNESS);

			if (slowness == null) {
				if (!isClient) me.addPotionEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 0, false, false, true));
			} else if (slowness.getAmplifier() == 0 && slowness.getDuration() < duration) {
				StatusEffectAccess.access(slowness).fermion_setDuration(duration);
			}

		case 7:
			frailty++;

			// fatigue
			final StatusEffectInstance fatigue = me.getStatusEffect(StatusEffects.MINING_FATIGUE);

			if (fatigue == null) {
				if (!isClient) me.addPotionEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, duration, 0, false, false, true));
			} else if (fatigue.getAmplifier() == 0 && fatigue.getDuration() < duration) {
				StatusEffectAccess.access(fatigue).fermion_setDuration(duration);
			}

		case 6:
			frailty++;

			// weakness
			final StatusEffectInstance weakness = me.getStatusEffect(StatusEffects.WEAKNESS);

			if (weakness == null) {
				if (!isClient) me.addPotionEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, duration, 0, false, false, true));
			} else if (weakness.getAmplifier() == 0 && weakness.getDuration() < duration) {
				StatusEffectAccess.access(weakness).fermion_setDuration(duration);
			}

		case 5:
			frailty++;
			hunger++;

		case 4:
			frailty++;
			hunger++;

		case 3:
			frailty++;
			hunger++;

		case 2:
			// add hunger
			hunger++;

		case 1:
			// add hunger
			hunger++;

		default:
		case 0:
			// Serves as a warning only

		}

		if (hunger >= 0) {

		}

		if (hunger >= 0) {
			final StatusEffectInstance hungerEffect = me.getStatusEffect(StatusEffects.HUNGER);

			if (hungerEffect == null || hungerEffect.getAmplifier() < hunger) {
				if (!isClient) me.addPotionEffect(new StatusEffectInstance(StatusEffects.HUNGER, duration, hunger, false, false, true));
			} else if (hungerEffect.getAmplifier() == hunger && hungerEffect.getDuration() < duration) {
				StatusEffectAccess.access(hungerEffect).fermion_set(duration, hunger);
			}
		}

		if (frailty >= 0) {
			final StatusEffectInstance frailtyEffect = me.getStatusEffect(DoomEffects.FRAILTY);

			if (frailtyEffect == null || frailtyEffect.getAmplifier() < frailty) {
				if (!isClient) me.addPotionEffect(new StatusEffectInstance(DoomEffects.FRAILTY, duration, frailty, false, false, true));
			} else if (frailtyEffect.getAmplifier() == frailty && frailtyEffect.getDuration() < duration)
				StatusEffectAccess.access(frailtyEffect).fermion_set(duration, frailty);
		}
	}
}