package grondag.doomtree.registry;

import grondag.doomtree.DoomTree;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

public class DoomTags {
	public static final Tag<Fluid> ICHOR = register("ichor");
	public static Tag<Block> DOOM_TREE_PROTECTED = TagRegistry.block(DoomTree.id("doom_tree_protected"));
	public static Tag<Block> DOOM_TREE_IGNORED = TagRegistry.block(DoomTree.id("doom_tree_ignored"));
	public static Tag<Item> WARDED_ITEM_TAG = TagRegistry.item(DoomTree.id("doom_warded"));
	
	private DoomTags() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	private static Tag<Fluid> register(String name) {
		return TagRegistry.fluid(DoomTree.id(name));
	}
}
