package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.ichor.IchorFluid;
import net.minecraft.fluid.BaseFluid;

public enum DoomFluids {
	;
	
	public static final int ICHOR_COLOR = 0x000000;
	public static final BaseFluid ICHOR = REG.fluid("ichor", new IchorFluid.Still());
	public static final BaseFluid FLOWING_ICHOR = REG.fluid("flowing_ichor", new IchorFluid.Flowing());
}
