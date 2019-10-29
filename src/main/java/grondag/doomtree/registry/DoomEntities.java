package grondag.doomtree.registry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public enum DoomEntities {
	;

	public static final DamageSource DOOM = new DamageSource("doom") {
		{
			setBypassesArmor();
		}
	};
	
	public static boolean canDoom(Entity e) {
		return e instanceof LivingEntity 
			&& e.isAlive()
			&& !e.isInvulnerable()
			&& !e.isSpectator()
			&& ((LivingEntity) e).getGroup() != EntityGroup.UNDEAD
			&& !DoomTags.UNDOOMED.contains(e.getType()) ;
	}
}
