package grondag.doomtree.recipe;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomRecipes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class BrazierRecipe extends AlchemicalRecipe {

	public BrazierRecipe(Identifier id, String group, Ingredient ingredient, int cost, ItemStack result) {
		super(id, group, ingredient, cost, result);
	}

	@Override
	public RecipeType<?> getType() {
		return DoomRecipes.BRAZIER_RECIPE_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DoomRecipes.BRAZIER_RECIPE_SERIALIZER;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getRecipeKindIcon() {
		return new ItemStack(DoomBlocks.BRAZIER_BLOCK);
	}
}
