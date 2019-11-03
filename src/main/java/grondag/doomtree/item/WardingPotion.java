package grondag.doomtree.item;

import grondag.doomtree.entity.WardingEffect;
import grondag.doomtree.registry.DoomEffects;
import grondag.fermion.entity.StatusEffectAccess;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class WardingPotion extends AbstractPotion {

	public WardingPotion(final Settings settings) {
		super(settings);
	}

	@Override
	public void applyEffects(final ItemStack stack, final World world, final PlayerEntity player) {
		final StatusEffectInstance warding = player.getStatusEffect(DoomEffects.WARDING_EFFECT);

		if (warding == null) {
			if (!world.isClient) {
				player.addPotionEffect(new StatusEffectInstance(DoomEffects.WARDING_EFFECT, WardingEffect.TICKS_PER_LEVEL, 0, false, false, true));
			}
		} else {
			System.out.println(Math.min(WardingEffect.TICKS_PER_LEVEL, WardingEffect.MAX_TICKS - warding.getDuration()));
			StatusEffectAccess.access(warding).fermion_addDuration(Math.min(WardingEffect.TICKS_PER_LEVEL, WardingEffect.MAX_TICKS - warding.getDuration()));
		}
	}
}
