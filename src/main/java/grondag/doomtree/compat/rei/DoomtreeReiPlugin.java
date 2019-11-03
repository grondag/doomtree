/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package grondag.doomtree.compat.rei;

import grondag.doomtree.DoomTree;
import grondag.doomtree.recipe.BasinRecipe;
import grondag.doomtree.recipe.BrazierRecipe;
import grondag.doomtree.registry.DoomBlocks;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DoomtreeReiPlugin implements REIPluginV0 {
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

	@Override
	public SemanticVersion getMinimumVersion() throws VersionParsingException {
		return SemanticVersion.parse("3.0");
	}
}
