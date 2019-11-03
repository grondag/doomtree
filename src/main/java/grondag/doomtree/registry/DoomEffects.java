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
