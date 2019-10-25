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