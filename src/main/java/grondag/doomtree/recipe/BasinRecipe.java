package grondag.doomtree.recipe;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomRecipes;
import grondag.fermion.recipe.AbstractSimpleRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class BasinRecipe extends AbstractSimpleRecipe {

	public BasinRecipe(Identifier id, String group, Ingredient ingredient, int cost, ItemStack result) {
		super(id, group, ingredient, cost, result);
	}

	@Override
	public RecipeType<?> getType() {
		return DoomRecipes.BASIN_RECIPE_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DoomRecipes.BASIN_RECIPE_SERIALIZER;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(DoomBlocks.BASIN_BLOCK);
	}
}
