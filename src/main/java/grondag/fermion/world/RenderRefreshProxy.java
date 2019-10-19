package grondag.fermion.world;

import java.util.function.Consumer;

import net.minecraft.util.math.BlockPos;

public class RenderRefreshProxy {

	/**
	 * Server-safe proxy for block entities to call render refresh
	 */
	public static Consumer<BlockPos> RENDER_REFRESH_HANDLER = p -> {};
}
