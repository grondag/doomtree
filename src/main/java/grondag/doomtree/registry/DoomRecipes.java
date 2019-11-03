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
package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.recipe.BasinRecipe;
import grondag.doomtree.recipe.BasinRepairRecipe;
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

	private static final String BASIN_REPAIR_RECIPE_ID = "alchemical_basin_repair";
	public static final RecipeSerializer<AbstractSimpleRecipe> BASIN_REPAIR_RECIPE_SERIALIZER = REG.recipeSerializer(BASIN_REPAIR_RECIPE_ID, new SimpleRecipeSerializer<>(BasinRepairRecipe::new));
	public static final RecipeType<AbstractSimpleRecipe> BASIN_REPAIR_RECIPE_TYPE = REG.recipeType(BASIN_REPAIR_RECIPE_ID);

	private static final String BRAZIER_RECIPE_ID = "alchemical_brazier";
	public static final RecipeSerializer<AbstractSimpleRecipe> BRAZIER_RECIPE_SERIALIZER = REG.recipeSerializer(BRAZIER_RECIPE_ID, new SimpleRecipeSerializer<>(BrazierRecipe::new));
	public static final RecipeType<AbstractSimpleRecipe> BRAZIER_RECIPE_TYPE = REG.recipeType(BRAZIER_RECIPE_ID);

	private static final String ICHOR_ID = "ichor_recipe";
	public static final RecipeSerializer<IchorRecipe> ICHOR_RECIPE_SERIALIZER = REG.recipeSerializer(ICHOR_ID, new IchorRecipeSerializer());
	public static final RecipeType<IchorRecipe> ICHOR_RECIPE = REG.recipeType(ICHOR_ID);

	public static final SimpleRecipeHelper HELPER = new SimpleRecipeHelper(REG.id("recipe_helper"), BASIN_RECIPE_TYPE, BRAZIER_RECIPE_TYPE, BASIN_REPAIR_RECIPE_TYPE);
}
