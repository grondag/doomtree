package grondag.doomtree.compat.rei;

import grondag.doomtree.DoomTree;
import grondag.doomtree.recipe.BasinRecipe;
import grondag.doomtree.recipe.BrazierRecipe;
import grondag.doomtree.registry.DoomBlocks;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.plugin.DefaultPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DoomtreeReiPlugin extends DefaultPlugin {

	public static final Identifier ID = DoomTree.REG.id("rei_plugin");
	public static final Identifier BASIN = DoomTree.REG.id("plugins/basin");
	public static final Identifier BRAZIER = DoomTree.REG.id("plugins/brazier");
	public static final Identifier BRAZIER_SMELTING = DoomTree.REG.id("plugins/brazier_smelting");
	public static final Identifier INFO = DoomTree.REG.id("plugins/info");

	@Override
	public Identifier getPluginIdentifier() {
		return ID;
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new BasinCategory());
		recipeHelper.registerCategory(new BrazierCategory());
		recipeHelper.registerCategory(new BrazierSmeltingCategory());
		recipeHelper.registerCategory(new InfoCategory());
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		Registry.ITEM.getIds().forEach(id -> {
			if (id.getNamespace().equals(DoomTree.MOD_ID)) {
				recipeHelper.registerDisplay(INFO, new InfoDisplay(new ItemStack(Registry.ITEM.get(id))));
			}
		});
		
		recipeHelper.registerRecipes(BRAZIER, BrazierRecipe.class, BrazierDisplay::new);
		recipeHelper.registerRecipes(BRAZIER_SMELTING, SmeltingRecipe.class, BrazierSmeltingDisplay::new);
		recipeHelper.registerRecipes(BASIN, BasinRecipe.class, BasinDisplay::new);
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(BRAZIER, new ItemStack(DoomBlocks.BRAZIER_BLOCK));
		recipeHelper.registerWorkingStations(BRAZIER_SMELTING, new ItemStack(DoomBlocks.BRAZIER_BLOCK));
		recipeHelper.registerWorkingStations(BASIN, new ItemStack(DoomBlocks.BASIN_BLOCK));
		recipeHelper.registerWorkingStations(INFO, new ItemStack(DoomBlocks.DOOM_SAPLING_BLOCK));
		
		recipeHelper.removeSpeedCraftButton(INFO);
	}
}
