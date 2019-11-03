package grondag.doomtree.item;

import java.util.List;
import java.util.stream.Collectors;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomEffects;
import grondag.fermion.entity.StatusEffectAccess;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SalvationPotion extends MilkPotion {

	public SalvationPotion(final Settings settings) {
		super(settings);
	}

	@Override
	public void applyEffects(final ItemStack stack, final World world, final PlayerEntity player) {
		final StatusEffectInstance doom = player.getStatusEffect(DoomEffects.DOOM_EFFECT);

		if (doom == null) {
			final List<StatusEffectInstance> candidates = player.getStatusEffects()
				.stream()
				.filter(e -> !DoomEffects.isImmuneToMilk(e.getEffectType()) && e.getEffectType().getType() == StatusEffectType.HARMFUL)
				.collect(Collectors.toList());

			if (candidates.isEmpty()) return;

			final int size = candidates.size();

			player.removePotionEffect(candidates.get(HashCommon.mix(size) % size).getEffectType());
			return;
		}

		final int aOld = doom.getAmplifier();

		if (aOld <= 1) {
			player.removePotionEffect(DoomEffects.DOOM_EFFECT);
			return;
		}

		final float dFactor = (float)doom.getDuration() / DoomEffect.durationTicks(aOld);
		final int aNew = aOld - 2;
		StatusEffectAccess.access(doom).fermion_set(Math.round(DoomEffect.durationTicks(aNew) * dFactor), aNew);
	}
}
