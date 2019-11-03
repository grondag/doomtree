package grondag.doomtree;

import grondag.doomtree.block.player.WardedWoodSignBlockEntity;
import grondag.doomtree.entity.WardingEffect;
import grondag.doomtree.model.BasinModel;
import grondag.doomtree.model.BrazierModel;
import grondag.doomtree.model.ChannelModel;
import grondag.doomtree.model.HeartModel;
import grondag.doomtree.model.LogModel;
import grondag.doomtree.model.TerminalModel;
import grondag.doomtree.packet.AlchemyCraftS2C;
import grondag.doomtree.packet.DoomS2C;
import grondag.doomtree.packet.XpDrainS2C;
import grondag.doomtree.particle.BasinParticle.BasinParticleFactory;
import grondag.doomtree.particle.BrazierParticle.BrazierParticleFactory;
import grondag.doomtree.particle.IdleParticle.IdleParticleFactory;
import grondag.doomtree.particle.SummoningParticle.SummoningParticleFactory;
import grondag.doomtree.particle.WakingParticle.WakingParticleFactory;
import grondag.doomtree.particle.WardedFlameParticle;
import grondag.doomtree.registry.DoomFluids;
import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.registry.DoomParticles;
import grondag.doomtree.render.DoomEffectRender;
import grondag.fermion.block.sign.OpenSignRenderer;
import grondag.fermion.client.ClientRegistrar;
import grondag.fermion.client.models.SimpleUnbakedModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry;
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

		REGISTRAR.modelVariant("doom_heart", new SimpleUnbakedModel(HeartModel::create, HeartModel.TERMINAL_TEXTURES));

		REGISTRAR.simpleRandomModel("doom_leaves", "block/doom_leaves_0_0", "block/doom_leaves_0_1", "block/doom_leaves_0_2", "block/doom_leaves_0_3");
		REGISTRAR.simpleRandomModel("doomed_residue_block", "block/doomed_residue_block");
		REGISTRAR.simpleRandomModel("warding_essence_block", "block/warding_essence_block");

		ClientSidePacketRegistry.INSTANCE.register(AlchemyCraftS2C.IDENTIFIER, AlchemyCraftS2C::handle);
		ClientSidePacketRegistry.INSTANCE.register(DoomS2C.IDENTIFIER, DoomS2C::handle);
		ClientSidePacketRegistry.INSTANCE.register(XpDrainS2C.IDENTIFIER, XpDrainS2C::handle);

		REGISTRAR.fluidRenderHandler(DoomFluids.ICHOR, DoomFluids.ICHOR_COLOR, "block/water_still", "block/water_flow");

		ParticleFactoryRegistry.getInstance().register(DoomParticles.ALCHEMY_IDLE, IdleParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.ALCHEMY_WAKING, WakingParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.BASIN_ACTIVE, BasinParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.BRAZIER_ACTIVE, BrazierParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.WARDED_FLAME, WardedFlameParticle.WardedFlameParticleFactory::new);
		ParticleFactoryRegistry.getInstance().register(DoomParticles.SUMMONING, SummoningParticleFactory::new);

		ClientSpriteRegistryCallback.event(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).register((atlasTexture, registry) -> {
			registry.register(REGISTRAR.id("warded_flame"));
		});

		BlockEntityRendererRegistry.INSTANCE.register(WardedWoodSignBlockEntity.class, new OpenSignRenderer(REGISTRAR.id("textures/entity/warded_wood_sign.png"), new SignBlockEntityModel()));
		//BlockEntityRendererRegistry.INSTANCE.register(DoomSaplingBlockEntity.class, new DoomSaplingBlockEntityRenderer());

		DoomEffectRender.init(REGISTRAR);

		ColorProviderRegistry.ITEM.register((s, i) -> WardingEffect.COLOR, DoomItems.WARDING_POTION);
		ColorProviderRegistry.ITEM.register((s, i) -> 0xFFFFFFFF, DoomItems.MILK_POTION);
		ColorProviderRegistry.ITEM.register((s, i) -> 0xFFB6D8FF, DoomItems.SALVATION_POTION);
	}
}
