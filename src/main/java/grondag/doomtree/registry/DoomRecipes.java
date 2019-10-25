package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.recipe.BasinRecipe;
import grondag.doomtree.recipe.BrazierRecipe;
import grondag.doomtree.recipe.IchorRecipe;
import grondag.doomtree.recipe.IchorRecipeSerializer;
import grondag.fermion.recipe.AbstractSimpleRecipe;
import grondag.fermion.recipe.SimpleRecipeHelper;
import grondag.fermion.recipe.SimpleRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public enum DoomRecipes {
	;

	private static final String BASIN_RECIPE_ID = "alchemical_basin";
	public static final RecipeSerializer<AbstractSimpleRecipe> BASIN_RECIPE_SERIALIZER = REG.recipeSerializer(BASIN_RECIPE_ID, new SimpleRecipeSerializer<>(BasinRecipe::new));
	public static final RecipeType<AbstractSimpleRecipe> BASIN_RECIPE_TYPE = REG.recipeType(BASIN_RECIPE_ID);

	private static final String BRAZIER_RECIPE_ID = "alchemical_brazier";
	public static final RecipeSerializer<AbstractSimpleRecipe> BRAZIER_RECIPE_SERIALIZER = REG.recipeSerializer(BRAZIER_RECIPE_ID, new SimpleRecipeSerializer<>(BrazierRecipe::new));
	public static final RecipeType<AbstractSimpleRecipe> BRAZIER_RECIPE_TYPE = REG.recipeType(BRAZIER_RECIPE_ID);
	
	private static final String ICHOR_ID = "ichor_recipe";
	public static final RecipeSerializer<IchorRecipe> ICHOR_RECIPE_SERIALIZER = REG.recipeSerializer(ICHOR_ID, new IchorRecipeSerializer());
	public static final RecipeType<IchorRecipe> ICHOR_RECIPE = REG.recipeType(ICHOR_ID);
	
	public static final SimpleRecipeHelper HELPER = new SimpleRecipeHelper(REG.id("recipe_helper"), BASIN_RECIPE_TYPE, BRAZIER_RECIPE_TYPE);
}
