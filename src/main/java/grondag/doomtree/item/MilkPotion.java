package grondag.doomtree.item;

import java.util.List;
import java.util.stream.Collectors;

import grondag.doomtree.registry.DoomEffects;
import it.unimi.dsi.fastutil.HashCommon;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MilkPotion extends AbstractPotion {

	public MilkPotion(final Settings settings) {
		super(settings);
	}

	@Override
	public void applyEffects(final ItemStack stack, final World world, final PlayerEntity player) {
		final List<StatusEffectInstance> candidates = player.getStatusEffects()
			.stream()
			.filter(e -> !DoomEffects.isImmuneToMilk(e.getEffectType()))
			.collect(Collectors.toList());

		if (candidates.isEmpty()) return;

		final int size = candidates.size();

		player.removePotionEffect(candidates.get(HashCommon.mix(size) % size).getEffectType());
	}
}
