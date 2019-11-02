package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.entity.DoomEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;

public enum DoomEntities {
	;

	public static final DamageSource DOOM = new DamageSource("doom") {
		{
			setBypassesArmor();
			setUnblockable();
		}
	};

	public static final StatusEffect DOOM_EFFECT = REG.statusEffect("doom", new DoomEffect()
		.addAttributeModifier(EntityAttributes.MAX_HEALTH, "8A32A4FF-0A4F-4DD5-8D8C-3A2B88F8B2C3", -2.0D, EntityAttributeModifier.Operation.ADDITION));
}
