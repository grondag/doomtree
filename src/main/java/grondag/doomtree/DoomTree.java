package grondag.doomtree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import grondag.doomtree.block.treeheart.DoomTreeTracker;
import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomEffects;
import grondag.doomtree.registry.DoomFeatures;
import grondag.doomtree.registry.DoomFluids;
import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.registry.DoomLoot;
import grondag.doomtree.registry.DoomParticles;
import grondag.doomtree.registry.DoomRecipes;
import grondag.doomtree.registry.DoomSounds;
import grondag.doomtree.registry.DoomTags;
import grondag.fermion.registrar.Registrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class DoomTree implements ModInitializer {
	public static final String MOD_ID = "doomtree";
	public static final Logger LOGGER = LogManager.getLogger("The Doom Tree");
	public static final Registrar REG = new Registrar(MOD_ID, "doom_sapling");
	
	@Override
	public void onInitialize() {
		DoomBlocks.values();
		DoomFluids.values();
		DoomItems.values();
		DoomRecipes.values();
		DoomSounds.values();
		DoomTags.values();
		DoomParticles.values();
		DoomFeatures.values();
		DoomEffects.values();
		
		ServerStopCallback.EVENT.register(s -> DoomTreeTracker.clear());
		ServerStartCallback.EVENT.register(DoomRecipes.HELPER::init);
		ServerStopCallback.EVENT.register(DoomRecipes.HELPER::stop);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(DoomRecipes.HELPER);
		LootTableLoadingCallback.EVENT.register(DoomLoot::init);
	}
}
