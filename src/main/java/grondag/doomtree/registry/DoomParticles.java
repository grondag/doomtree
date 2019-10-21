package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.particle.DefaultParticleType;

public enum DoomParticles {
	;
	
	public static final DefaultParticleType BASIN_IDLE = REG.particle("basin_idle", true);
	public static final DefaultParticleType BASIN_WAKING = REG.particle("basin_waking", true);
	public static final DefaultParticleType BASIN_ACTIVE = REG.particle("basin_active", true);
	public static final DefaultParticleType WARDED_FLAME = REG.particle("warded_flame", true);
}
