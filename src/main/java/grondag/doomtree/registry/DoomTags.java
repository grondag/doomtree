package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;

public enum DoomTags {
	;
	
	public static final Tag<Fluid> ICHOR = REG.fluidTag("ichor");
	public static Tag<Block> PROTECTED_BLOCKS = REG.blockTag("protected_blocks");
	public static Tag<Block> IGNORED_BLOCKS = REG.blockTag("ignored_blocks");
	public static Tag<Block> WARDED_BLOCKS = REG.blockTag("warded_blocks");
	public static Tag<Item> WARDED_ITEMS = REG.itemTag("warded_items");
	public static Tag<Item> WARDED_TOOLS = REG.itemTag("warded_tools");
	public static Tag<EntityType<?>> UNDOOMED = REG.entityTag("undoomed");
	
	public static boolean isWardedOrEnchanted(ItemStack stack) {
		return stack.hasEnchantments() || WARDED_ITEMS.contains(stack.getItem());
	}
}
