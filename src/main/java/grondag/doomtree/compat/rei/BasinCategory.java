package grondag.doomtree.compat.rei;

import grondag.doomtree.registry.DoomBlocks;
import me.shedaniel.rei.api.Renderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class BasinCategory extends AlchemicalCategory<BasinDisplay> {
	@Override
	public Identifier getIdentifier() {
		return DoomtreeReiPlugin.BASIN;
	}

	@Override
	public Renderer getIcon() {
		return Renderer.fromItemStack(new ItemStack(DoomBlocks.BASIN_BLOCK));
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.doomtree.basin");
	}
}
