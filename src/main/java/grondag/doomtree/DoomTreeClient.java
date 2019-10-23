package grondag.doomtree;

import grondag.doomtree.block.WardedWoodSignBlockEntity;
import grondag.doomtree.model.BasinModel;
import grondag.doomtree.model.BrazierModel;
import grondag.doomtree.model.ChannelModel;
import grondag.doomtree.model.HeartModel;
import grondag.doomtree.model.LogModel;
import grondag.doomtree.model.TerminalModel;
import grondag.doomtree.packet.DoomPacket;
import grondag.doomtree.packet.DoomPacketHandler;
import grondag.doomtree.particle.IdleParticle.IdleFactory;
import grondag.doomtree.particle.WakingParticle.WakingFactory;
import grondag.doomtree.particle.WardedFlameParticle;
import grondag.doomtree.particle.WardingParticle.WardingFactory;
import grondag.doomtree.registry.DoomFluids;
import grondag.doomtree.registry.DoomParticles;
import grondag.fermion.block.sign.OpenSignRenderer;
import grondag.fermion.client.ClientRegistrar;
import grondag.fermion.client.models.SimpleUnbakedModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.entity.model.SignBlockEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;

public class DoomTreeClient implements ClientModInitializer {
	public static final ClientRegistrar REGISTRAR = new ClientRegistrar(DoomTree.MOD_ID);

	@Override
	public void onInitializeClient() {

		SimpleUnbakedModel model = new SimpleUnbakedModel(LogModel::create, LogModel.TEXTURES);
		REGISTRAR.modelVariant("doom_log", model);
		REGISTRAR.modelVariant("doom_log_p", model);

		model = new SimpleUnbakedModel(ChannelModel::create, ChannelModel.CHANNEL_TEXTURES);
		REGISTRAR.modelVariant("doom_log_channel", model);
		REGISTRAR.modelVariant("doom_log_channel_p", model);

		model = new SimpleUnbakedModel(TerminalModel::create, TerminalModel.TERMINAL_TEXTURES);
		REGISTRAR.modelVariant("doom_log_terminal", model);
		REGISTRAR.modelVariant("doom_log_terminal_p", model);

		model = new SimpleUnbakedModel(BasinModel::create, BasinModel.TEXTURES);
		REGISTRAR.modelVariant("inert_alchemical_basin", model);
		REGISTRAR.modelVariant("alchemical_basin", model);
		REGISTRAR.modelVariant("alchemical_basin_frame", new SimpleUnbakedModel(BasinModel::createFrame, BasinModel.TEXTURES));

		model = new SimpleUnbakedModel(BrazierModel::create, BrazierModel.TEXTURES);
		REGISTRAR.modelVariant("inert_alchemical_brazier", model);
		REGISTRAR.modelVariant("alchemical_brazier", model);
		REGISTRAR.modelVariant("alchemical_brazier_frame", new SimpleUnbakedModel(BrazierModel::createFrame, BrazierModel.TEXTURES));

		REGISTRAR.modelVariant("doom_tree_heart", new SimpleUnbakedModel(HeartModel::create, HeartModel.TERMINAL_TEXTURES));

		REGISTRAR.simpleRandomModel("doom_leaves", "block/doom_leaves_0_0", "block/doom_leaves_0_1", "block/doom_leaves_0_2", "block/doom_leaves_0_3");
		REGISTRAR.simpleRandomModel("doomed_residue_block", "block/doomed_residue_block");
		REGISTRAR.simpleRandomModel("warding_essence_block", "block/warding_essence_block");

		ClientSidePacketRegistry.INSTANCE.register(DoomPacket.IDENTIFIER, DoomPacketHandler::accept);

		REGISTRAR.fluidRenderHandler(DoomFluids.ICHOR, DoomFluids.ICHOR_COLOR, "block/water_still", "block/water_flowing");

		ParticleFactoryRegistry.getInstance().register(DoomParticles.BASIN_IDLE, IdleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.BASIN_WAKING, WakingFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.BASIN_ACTIVE, WardingFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.WARDED_FLAME, WardedFlameParticle.Factory::new);

		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).register((atlasTexture, registry) -> {
			registry.register(REGISTRAR.id("warded_flame"));
		});

		BlockEntityRendererRegistry.INSTANCE.register(WardedWoodSignBlockEntity.class, new OpenSignRenderer(REGISTRAR.id("textures/entity/warded_wood_sign.png"), new SignBlockEntityModel()));
	}
}
