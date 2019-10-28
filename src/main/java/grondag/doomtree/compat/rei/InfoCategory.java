package grondag.doomtree.compat.rei;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import grondag.doomtree.registry.DoomBlocks;
import me.shedaniel.math.api.Rectangle;
import me.shedaniel.rei.api.RecipeCategory;
import me.shedaniel.rei.api.Renderer;
import me.shedaniel.rei.gui.widget.SlotWidget;
import me.shedaniel.rei.gui.widget.Widget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class InfoCategory implements RecipeCategory<InfoDisplay> {
	@Override
	public List<Widget> setupDisplay(Supplier<InfoDisplay> recipeDisplaySupplier, Rectangle bounds) {
		final ItemStack stack = recipeDisplaySupplier.get().getOutput().get(0);
		
		List<Widget> widgets = new LinkedList<>();
		widgets.add(new SlotWidget(bounds.getMinX(), bounds.getMinY(), Renderer.fromItemStack(stack), true, true, true));
		widgets.add(new InfoWidget(bounds.getMinX() + 24, bounds.getMinY(), bounds.getWidth() - 24, 24, 
				I18n.translate(stack.getTranslationKey()),
				Formatting.DARK_BLUE.toString()));
		widgets.add(new InfoWidget(bounds.getMinX(), bounds.getMinY() + 24, bounds.getWidth(), bounds.getHeight() - 24, 
				I18n.translate(stack.getTranslationKey() + ".info"),
				Formatting.BLACK.toString()));
		return widgets;
	}

	@Override
	public int getDisplayHeight() {
		return 128;
	}
	
	@Override
	public Identifier getIdentifier() {
		return DoomtreeReiPlugin.INFO;
	}

	@Override
	public Renderer getIcon() {
		return Renderer.fromItemStack(new ItemStack(DoomBlocks.DOOM_SAPLING_BLOCK));
	}

	@Override
	public String getCategoryName() {
		return I18n.translate("category.doomtree.info");
	}
}