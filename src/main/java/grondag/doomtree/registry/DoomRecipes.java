package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.recipe.BasinWardingRecipe;
import grondag.doomtree.recipe.BasinWardingRecipeSerializer;
import grondag.doomtree.recipe.IchorRecipe;
import grondag.doomtree.recipe.IchorRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public enum DoomRecipes {
	;

	private static final String BASIN_WARDING_ID = "basin_warding";
	public static final RecipeSerializer<BasinWardingRecipe> WARDING_RECIPE_SERIALIZER = REG.recipeSerializer(BASIN_WARDING_ID, new BasinWardingRecipeSerializer());
	public static final RecipeType<BasinWardingRecipe> WARDING_RECIPE_TYPE = REG.recipeType(BASIN_WARDING_ID);

	private static final String ICHOR_ID = "ichor_recipe";
	public static final RecipeSerializer<IchorRecipe> ICHOR_RECIPE_SERIALIZER = REG.recipeSerializer(ICHOR_ID, new IchorRecipeSerializer());
	public static final RecipeType<IchorRecipe> ICHOR_RECIPE = REG.recipeType(ICHOR_ID);
}
