package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.entity.WardingEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.HealthBoostStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public enum DoomEffects {
	;

	public static final DamageSource DOOM = new DamageSource("doom") {
		{
			setBypassesArmor();
			setUnblockable();
		}
	};

	public static final StatusEffect DOOM_EFFECT = REG.statusEffect("doom", new DoomEffect());
	public static final StatusEffect WARDING_EFFECT = REG.statusEffect("warding", new WardingEffect());
	public static final StatusEffect FRAILTY = REG.statusEffect("frailty", new HealthBoostStatusEffect(StatusEffectType.HARMFUL, 0xA0F080)
		.addAttributeModifier(EntityAttributes.MAX_HEALTH, "8A32A4FF-0A4F-4DD5-8D8C-3A2B88F8B2C3", -2.0D, EntityAttributeModifier.Operation.ADDITION));

	public static boolean isImmuneToMilk(final StatusEffect effect) {
		return effect == DOOM_EFFECT || effect == WARDING_EFFECT || effect == FRAILTY;
	}
}
