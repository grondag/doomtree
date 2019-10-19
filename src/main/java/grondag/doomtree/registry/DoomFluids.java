package grondag.doomtree.registry;

import grondag.doomtree.DoomTree;
import grondag.doomtree.ichor.IchorFluid;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

public class DoomFluids {
	public static final BaseFluid ICHOR = register("ichor", new IchorFluid.Still());
	public static final BaseFluid FLOWING_ICHOR = register("ichor_flowing", new IchorFluid.Flowing());
	
	private DoomFluids() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	static <T extends Fluid> T register(String name, T fluid) {
		T b = Registry.register(Registry.FLUID, DoomTree.id(name), fluid);
		return b;
	}
}
