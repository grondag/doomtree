package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.particle.DefaultParticleType;

public enum DoomParticles {
	;
	
	public static final DefaultParticleType ALCHEMY_IDLE = REG.particle("alchemy_idle", true);
	public static final DefaultParticleType ALCHEMY_WAKING = REG.particle("alchemy_waking", true);
	public static final DefaultParticleType BASIN_ACTIVE = REG.particle("basin_active", true);
	public static final DefaultParticleType BRAZIER_ACTIVE = REG.particle("brazier_active", true);
	public static final DefaultParticleType WARDED_FLAME = REG.particle("warded_flame", true);
	public static final DefaultParticleType SUMMONING = REG.particle("summoning", true);
}
