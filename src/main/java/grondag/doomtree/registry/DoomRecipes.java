package grondag.doomtree.registry;

import grondag.doomtree.DoomTree;
import grondag.doomtree.recipe.BasinWardingRecipe;
import grondag.doomtree.recipe.BasinWardingRecipeSerializer;
import grondag.doomtree.recipe.IchorRecipe;
import grondag.doomtree.recipe.IchorRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.registry.Registry;

public class DoomRecipes {

	private static final String BASIN_WARDING_ID = "basin_warding";
	public static final RecipeSerializer<BasinWardingRecipe> WARDING_RECIPE_SERIALIZER = Registry.register(Registry.RECIPE_SERIALIZER, DoomTree.id(BASIN_WARDING_ID), new BasinWardingRecipeSerializer());
	public static final RecipeType<BasinWardingRecipe> WARDING_RECIPE_TYPE = Registry.register(Registry.RECIPE_TYPE, DoomTree.id(BASIN_WARDING_ID), new RecipeType<BasinWardingRecipe>() {
		@Override
		public String toString() {
			return BASIN_WARDING_ID;
		}
	});
	
	public static final RecipeSerializer<IchorRecipe> ICHOR_RECIPE_SERIALIZER = Registry.register(
		Registry.RECIPE_SERIALIZER,
		IchorRecipeSerializer.ID,
		IchorRecipeSerializer.INSTANCE
	);

	public static final RecipeType<IchorRecipe> ICHOR_RECIPE = Registry.register(
		Registry.RECIPE_TYPE,
		IchorRecipe.Type.ID,
		IchorRecipe.Type.INSTANCE
	);

	public static void init() {
		// NO-OP
	}

	private DoomRecipes() {
		// NO-OP
	}
}
