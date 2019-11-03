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

import java.util.List;
import java.util.Optional;

import com.google.common.collect.ImmutableList;

import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class InfoDisplay implements RecipeDisplay {
	protected final List<List<ItemStack>> inputs;
	protected final List<ItemStack> output;
	protected ItemStack stack;

	public InfoDisplay(ItemStack stack) {
		this.stack = stack;
		output = ImmutableList.of(stack);
		inputs = ImmutableList.of(output);
	}

	@Override
	public Optional<Identifier> getRecipeLocation() {
		return Optional.ofNullable(null);
	}

	@Override
	public List<List<ItemStack>> getInput() {
		return inputs;
	}

	@Override
	public List<ItemStack> getOutput() {
		return output;
	}

	@Override
	public List<List<ItemStack>> getRequiredItems() {
		return inputs;
	}

	@Override
	public Identifier getRecipeCategory() {
		return DoomtreeReiPlugin.INFO;
	}
}
