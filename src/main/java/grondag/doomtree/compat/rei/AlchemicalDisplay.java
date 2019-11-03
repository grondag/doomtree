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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public abstract class AlchemicalDisplay<T extends Recipe<?>> implements RecipeDisplay {

	protected List<List<ItemStack>> inputs;
	protected List<ItemStack> output;
	protected T display;

	public AlchemicalDisplay(T recipe) {
		this(recipe.getPreviewInputs(), recipe.getOutput());
		this.display = recipe;
	}

	public AlchemicalDisplay(DefaultedList<Ingredient> ingredients, ItemStack output) {
		this.inputs = ingredients.stream().map(i -> Arrays.asList(i.getStackArray())).collect(Collectors.toList());
		this.output = Collections.singletonList(output);
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(display).map(Recipe::getId);
	}

	@Override
	public List<List<ItemStack>> getInput() {
		return inputs;
	}

	@Override
	public List<ItemStack> getOutput() {
		return this.output;
	}

	@Override
	public List<List<ItemStack>> getRequiredItems() {
		return getInput();
	}
}
