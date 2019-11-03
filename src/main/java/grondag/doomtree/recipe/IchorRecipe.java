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
package grondag.doomtree.recipe;

import static grondag.doomtree.DoomTree.REG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import grondag.doomtree.registry.DoomRecipes;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class IchorRecipe implements Recipe<BasicInventory> {
	private final List<Ingredient> ingredients;
	private final ItemStack result;
	private final Identifier recipeId;

	IchorRecipe(Ingredient ingredient, ItemStack result, Identifier recipeId) {
		ingredients = Collections.singletonList(ingredient);
		this.result = result;
		this.recipeId = recipeId;
	}

	IchorRecipe(ItemStack result, Identifier recipeId, Ingredient... ingredients) {
		this.ingredients = Arrays.asList(ingredients);
		this.result = result;
		this.recipeId = recipeId;
	}

	IchorRecipe(List<Ingredient> ingredients, ItemStack result, Identifier recipeId) {
		this.ingredients = ingredients;
		this.result = result;
		this.recipeId = recipeId;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	@Override
	public boolean matches(BasicInventory inventory, World world) {
		final ArrayList<ItemStack> inventoryList = new ArrayList<>();
		for(int i = 0; i < inventory.getInvSize(); i++) {
			inventoryList.add(inventory.getInvStack(i));
		}

		return hasRequiredIngredients(inventoryList);
	}

	private boolean hasRequiredIngredients(List<ItemStack> toCheck) {
		for (final Ingredient ingredient : ingredients) {
			boolean hasIngredient = false;
			for (final ItemStack potentialIngredient : toCheck) {
				if (ingredient.method_8093(potentialIngredient)) {
					toCheck.remove(potentialIngredient);
					hasIngredient = true;
					break;
				}
			}

			if (!hasIngredient) {
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack craft(BasicInventory inventory) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int var1, int var2) {
		return false;
	}

	@Override
	public ItemStack getOutput() {
		return result;
	}

	@Override
	public Identifier getId() {
		return recipeId;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DoomRecipes.ICHOR_RECIPE_SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return Type.INSTANCE;
	}

	public static class Type implements RecipeType<IchorRecipe> {
		public static final Type INSTANCE = new Type();
		public static final Identifier ID = REG.id("ichor_recipe");

		private Type() {
			// NO-OP
		}
	}
}
