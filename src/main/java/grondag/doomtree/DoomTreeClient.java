package grondag.doomtree;

import grondag.doomtree.model.AlchemicalBasin;
import grondag.doomtree.model.DoomLog;
import grondag.doomtree.model.DoomLogChannel;
import grondag.doomtree.model.DoomLogTerminal;
import grondag.doomtree.model.DoomTreeHeart;
import grondag.doomtree.packet.DoomTreePacket;
import grondag.doomtree.packet.DoomTreePacketHandler;
import grondag.fermion.client.models.SimpleModels;
import grondag.fermion.client.models.SimpleRandomModel;
import grondag.fermion.client.models.SimpleUnbakedModel;
import grondag.fermion.world.RenderRefreshProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.InvalidateRenderStateCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

public class DoomTreeClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		SimpleModels.init();
		
		SimpleUnbakedModel model = new SimpleUnbakedModel(DoomLog::create, DoomLog.TEXTURES);
		SimpleModels.register("doom_log", model);
		SimpleModels.register("doom_log_p", model);

		model = new SimpleUnbakedModel(DoomLogChannel::create, DoomLogChannel.CHANNEL_TEXTURES);
		SimpleModels.register("doom_log_channel", model);
		SimpleModels.register("doom_log_channel_p", model);

		model = new SimpleUnbakedModel(DoomLogTerminal::create, DoomLogTerminal.TERMINAL_TEXTURES);
		SimpleModels.register("doom_log_terminal", model);
		SimpleModels.register("doom_log_terminal_p", model);


		SimpleModels.register("alchemical_basin", new SimpleUnbakedModel(AlchemicalBasin::create, AlchemicalBasin.TEXTURES));
		SimpleModels.register("alchemical_basin_frame", new SimpleUnbakedModel(AlchemicalBasin::createFrame, AlchemicalBasin.TEXTURES));

		SimpleModels.register("doom_tree_heart", new SimpleUnbakedModel(DoomTreeHeart::create, DoomTreeHeart.TERMINAL_TEXTURES));

		SimpleRandomModel.register("doom_leaves", "block/doom_leaves_0_0", "block/doom_leaves_0_1", "block/doom_leaves_0_2", "block/doom_leaves_0_3");
		SimpleRandomModel.register("doomed_residue_block", "block/doomed_residue_block");
		SimpleRandomModel.register("warding_essence_block", "block/warding_essence_block");

		ClientSidePacketRegistry.INSTANCE.register(DoomTreePacket.IDENTIFIER, DoomTreePacketHandler::accept);

		InvalidateRenderStateCallback.EVENT.register(() -> {
			RenderRefreshProxy.RENDER_REFRESH_HANDLER = p -> {
				final MinecraftClient client = MinecraftClient.getInstance();
				client.worldRenderer.updateBlock(client.world, p, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), 8);
			};
		});
	}
}
