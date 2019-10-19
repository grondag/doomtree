package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public enum DoomTags {
	;
	
	public static final Tag<Fluid> ICHOR = REG.fluidTag("ichor");
	public static Tag<Block> DOOM_TREE_PROTECTED = REG.blockTag("doom_tree_protected");
	public static Tag<Block> DOOM_TREE_IGNORED = REG.blockTag("doom_tree_ignored");
	public static Tag<Item> WARDED_ITEM_TAG = REG.itemTag("doom_warded");
}
