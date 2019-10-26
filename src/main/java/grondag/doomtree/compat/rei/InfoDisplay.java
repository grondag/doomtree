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
		this.output = ImmutableList.of(stack);
		this.inputs = ImmutableList.of(output);
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
		return this.output;
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