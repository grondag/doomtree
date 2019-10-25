package grondag.doomtree.compat.rei;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class BrazierSmeltingDisplay extends AlchemicalDisplay<SmeltingRecipe> {

	public BrazierSmeltingDisplay(SmeltingRecipe recipe) {
		super(recipe);
	}

	public BrazierSmeltingDisplay(DefaultedList<Ingredient> ingredients, ItemStack output) {
		super(ingredients, output);
	}


	@Override
	public Identifier getRecipeCategory() {
		return DoomtreeReiPlugin.BRAZIER_SMELTING;
	}
}
