package grondag.doomtree.recipe;

import grondag.fermion.recipe.SimpleRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public abstract class AlchemicalRecipe implements SimpleRecipe<Inventory> {
	public final Ingredient ingredient;
	public final ItemStack result;
	public final Identifier id;
	public final String group;
	public final int cost;

	public AlchemicalRecipe(Identifier id, String group, Ingredient ingredient, int cost, ItemStack result) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = result;
		this.cost = cost;
	}

	@Override
	public Identifier getId() {
		return id;
	}
	
	@Override
	public boolean matches(Inventory inventory, World world) {
		return ingredient.method_8093(inventory.getInvStack(0));
	}

	public boolean matches(ItemStack stack) {
		return ingredient.method_8093(stack);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public String getGroup() {
		return group;
	}

	@Override
	public ItemStack getOutput() {
		return result;
	}

	@Override
	public DefaultedList<Ingredient> getPreviewInputs() {
		DefaultedList<Ingredient> defaultedList = DefaultedList.of();
		defaultedList.add(this.ingredient);
		return defaultedList;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack craft(Inventory inventory) {
		return result.copy();
	}
	
	@FunctionalInterface
	public static interface Factory<T extends AlchemicalRecipe> {
		T create(Identifier id, String group, Ingredient ingredient, int cost, ItemStack result);
	}
}