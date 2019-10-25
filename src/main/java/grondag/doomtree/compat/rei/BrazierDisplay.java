package grondag.doomtree.compat.rei;

import grondag.doomtree.recipe.BrazierRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class BrazierDisplay extends AlchemicalDisplay<BrazierRecipe> {

	public BrazierDisplay(BrazierRecipe recipe) {
		super(recipe);
	}

	public BrazierDisplay(DefaultedList<Ingredient> ingredients, ItemStack output) {
		super(ingredients, output);
	}


	@Override
	public Identifier getRecipeCategory() {
		return DoomtreeReiPlugin.BRAZIER;
	}
}
