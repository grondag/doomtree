package grondag.doomtree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import grondag.doomtree.recipe.BasinWardingRecipeHelper;
import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomFluids;
import grondag.doomtree.registry.DoomItems;
import grondag.doomtree.registry.DoomRecipes;
import grondag.doomtree.registry.DoomSounds;
import grondag.doomtree.registry.DoomTags;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class DoomTree implements ModInitializer {
	public static final String MOD_ID = "doomtree";
	public static final Logger LOGGER = LogManager.getLogger("The Doom Tree");
	public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "group"), () -> new ItemStack(DoomBlocks.DOOM_LOG));
	
	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
	
	@Override
	public void onInitialize() {
		DoomBlocks.init();
		DoomFluids.init();
		DoomItems.init();
		DoomRecipes.init();
		DoomSounds.init();
		DoomTags.init();
		
		ServerStopCallback.EVENT.register(s -> DoomTreeTracker.clear());
		ServerStartCallback.EVENT.register(BasinWardingRecipeHelper::init);
		ServerStopCallback.EVENT.register(BasinWardingRecipeHelper::stop);
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(BasinWardingRecipeHelper.INSTANCE);
	}
}
