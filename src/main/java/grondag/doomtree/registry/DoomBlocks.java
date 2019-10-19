package grondag.doomtree.registry;

import java.util.function.Function;

import grondag.doomtree.DoomTree;
import grondag.doomtree.block.AlchemicalBasinBlock;
import grondag.doomtree.block.AlchemicalBasinBlockEntity;
import grondag.doomtree.block.DoomGleamBlock;
import grondag.doomtree.block.DoomHeartBlock;
import grondag.doomtree.block.DoomLeafBlock;
import grondag.doomtree.block.DoomLogBlock;
import grondag.doomtree.block.DoomSaplingBlock;
import grondag.doomtree.block.DoomedBlock;
import grondag.doomtree.block.DoomedLogBlock;
import grondag.doomtree.block.MiasmaBlock;
import grondag.doomtree.ichor.IchorBlock;
import grondag.doomtree.ichor.IchorBubbleColumnBlock;
import grondag.doomtree.treeheart.DoomHeartBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;

public class DoomBlocks {
	public static final Block ICHOR_BLOCK = register("ichor", new IchorBlock(DoomFluids.ICHOR, FabricBlockSettings.copy(Blocks.WATER).build()), (BlockItem) null);
	public static final Block ICHOR_BUBBLE_COLUMN = registerNoItem("ichor_bubble_column", new IchorBubbleColumnBlock(FabricBlockSettings.copy(Blocks.BUBBLE_COLUMN).build()));
	public static Block DOOM_SAPLING = register("doom_sapling",
	new DoomSaplingBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).build()));
	public static Block MIASMA_BLOCK = Registry.register(Registry.BLOCK, DoomTree.id("miasma"), new MiasmaBlock());
	public static Block DOOM_GLEAM_BLOCK = Registry.register(Registry.BLOCK, DoomTree.id("doom_gleam"), new DoomGleamBlock());
	public static Block DOOM_HEART_BLOCK = Registry.register(Registry.BLOCK, DoomTree.id("doom_tree_heart"),
	new DoomHeartBlock(FabricBlockSettings.of(Material.WOOD).dropsLike(DOOM_SAPLING).breakByTool(FabricToolTags.AXES, 3).strength(200.0F, 1200.0F).build()));
	public static final BlockEntityType<DoomHeartBlockEntity> DOOM_HEART =
	Registry.register(Registry.BLOCK_ENTITY, DoomTree.id("doom_tree"), BlockEntityType.Builder.create(DoomHeartBlockEntity::new, DOOM_HEART_BLOCK).build(null));
	public static Block PLACED_DOOM_LOG = register("doom_log_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block PLACED_DOOM_LOG_CHANNEL = register("doom_log_channel_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block PLACED_DOOM_LOG_TERMINAL = register("doom_log_terminal_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block DOOM_LOG = Registry.register(Registry.BLOCK, DoomTree.id("doom_log"),
			new DoomLogBlock.Height(logSettings().dropsLike(PLACED_DOOM_LOG).build(), 0.04f));
	public static Block DOOM_LOG_CHANNEL = Registry.register(Registry.BLOCK, DoomTree.id("doom_log_channel"),
			new DoomLogBlock.Height(logSettings().dropsLike(PLACED_DOOM_LOG_CHANNEL).build(), 0.02f));
	public static Block DOOM_LOG_TERMINAL = Registry.register(Registry.BLOCK, DoomTree.id("doom_log_terminal"),
			new DoomLogBlock(logSettings().dropsLike(PLACED_DOOM_LOG_TERMINAL).build(), false, 0.02f));
	public static Block DOOM_LEAF = register("doom_leaves", new DoomLeafBlock(doomedSettings(), false, 1));
	public static Block DOOMED_LOG = register("doomed_log", new DoomedLogBlock(doomedSettings()));
	public static Block DOOMED_EARTH = register("doomed_earth", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_STONE = register("doomed_stone", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_DUST = register("doomed_dust", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_RESIDUE_BLOCK = register("doomed_residue_block", new Block(FabricBlockSettings.of(Material.EARTH).breakByHand(true).sounds(BlockSoundGroup.SAND).build()));
	public static Block WARDING_ESSENCE_BLOCK = register("warding_essence_block", new Block(FabricBlockSettings.of(Material.EARTH).breakByHand(true).sounds(BlockSoundGroup.SAND).build()));
	public static Block ALCHEMICAL_BASIN_BLOCK = register("alchemical_basin", new AlchemicalBasinBlock(FabricBlockSettings
	.of(Material.METAL).breakByHand(true).strength(0.5F, 0.5F).lightLevel(5).build()));
	public static final BlockEntityType<AlchemicalBasinBlockEntity> ALCHEMICAL_BASIN =
	Registry.register(Registry.BLOCK_ENTITY, DoomTree.id("alchemical_basin"), BlockEntityType.Builder.create(AlchemicalBasinBlockEntity::new, ALCHEMICAL_BASIN_BLOCK).build(null));


	private DoomBlocks() {
		// NO-OP
	}

	public static void init() {
	}

	static <T extends Block> T register(String name, T block, Item.Settings settings) {
		return register(name, block, new BlockItem(block, settings));
	}

	public static <T extends Block> T register(String name, T block) {
		return register(name, block, new Item.Settings().group(DoomTree.GROUP));
	}

	static <T extends Block> T register(String name, T block, Function<T, BlockItem> itemFactory) {
		return register(name, block, itemFactory.apply(block));
	}

	static <T extends Block> T register(String name, T block, BlockItem item) {
		T b = Registry.register(Registry.BLOCK, DoomTree.id(name), block);
		if (item != null) {
			BlockItem bi = DoomItems.register(name, item);
			bi.appendBlocks(BlockItem.BLOCK_ITEMS, bi);
		}
		return b;
	}

	static <T extends Block> T registerNoItem(String name, T block) {
		T b = Registry.register(Registry.BLOCK, DoomTree.id(name), block);
		return b;
	}
	
	static FabricBlockSettings logSettings() {
		return FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES, 2).strength(3.0F, 20.0F);
	}

	static Block.Settings doomedSettings() {
		return FabricBlockSettings.of(Material.EARTH).breakByHand(true).breakInstantly().sounds(BlockSoundGroup.SAND).build();
	}
}
