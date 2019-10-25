package grondag.doomtree.compat.rei;

import grondag.doomtree.recipe.BasinRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;

public class BasinDisplay extends AlchemicalDisplay<BasinRecipe> {

	public BasinDisplay(BasinRecipe recipe) {
		super(recipe);
	}

	public BasinDisplay(DefaultedList<Ingredient> ingredients, ItemStack output) {
		super(ingredients, output);
	}


	@Override
	public Identifier getRecipeCategory() {
		return DoomtreeReiPlugin.BASIN;
	}
}
