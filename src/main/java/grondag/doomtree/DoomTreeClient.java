package grondag.doomtree;

import grondag.doomtree.model.AlchemicalBasin;
import grondag.doomtree.model.DoomLog;
import grondag.doomtree.model.DoomLogChannel;
import grondag.doomtree.model.DoomLogTerminal;
import grondag.doomtree.model.DoomTreeHeart;
import grondag.doomtree.packet.DoomTreePacket;
import grondag.doomtree.packet.DoomTreePacketHandler;
import grondag.doomtree.registry.DoomFluids;
import grondag.fermion.client.ClientRegistrar;
import grondag.fermion.client.models.SimpleUnbakedModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;

public class DoomTreeClient implements ClientModInitializer {
	public static final ClientRegistrar REGISTRAR = new ClientRegistrar(DoomTree.MOD_ID);
	
	@Override
	public void onInitializeClient() {
		
		SimpleUnbakedModel model = new SimpleUnbakedModel(DoomLog::create, DoomLog.TEXTURES);
		REGISTRAR.modelVariant("doom_log", model);
		REGISTRAR.modelVariant("doom_log_p", model);

		model = new SimpleUnbakedModel(DoomLogChannel::create, DoomLogChannel.CHANNEL_TEXTURES);
		REGISTRAR.modelVariant("doom_log_channel", model);
		REGISTRAR.modelVariant("doom_log_channel_p", model);

		model = new SimpleUnbakedModel(DoomLogTerminal::create, DoomLogTerminal.TERMINAL_TEXTURES);
		REGISTRAR.modelVariant("doom_log_terminal", model);
		REGISTRAR.modelVariant("doom_log_terminal_p", model);


		REGISTRAR.modelVariant("alchemical_basin", new SimpleUnbakedModel(AlchemicalBasin::create, AlchemicalBasin.TEXTURES));
		REGISTRAR.modelVariant("alchemical_basin_frame", new SimpleUnbakedModel(AlchemicalBasin::createFrame, AlchemicalBasin.TEXTURES));

		REGISTRAR.modelVariant("doom_tree_heart", new SimpleUnbakedModel(DoomTreeHeart::create, DoomTreeHeart.TERMINAL_TEXTURES));

		REGISTRAR.simpleRandomModel("doom_leaves", "block/doom_leaves_0_0", "block/doom_leaves_0_1", "block/doom_leaves_0_2", "block/doom_leaves_0_3");
		REGISTRAR.simpleRandomModel("doomed_residue_block", "block/doomed_residue_block");
		REGISTRAR.simpleRandomModel("warding_essence_block", "block/warding_essence_block");

		ClientSidePacketRegistry.INSTANCE.register(DoomTreePacket.IDENTIFIER, DoomTreePacketHandler::accept);
		
		REGISTRAR.fluidRenderHandler(DoomFluids.ICHOR, 0x403220, "block/water_still", "block/water_flowing");
	}
}
