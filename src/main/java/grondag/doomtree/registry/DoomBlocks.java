package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.block.AlchemicalBlock;
import grondag.doomtree.block.BasinBlock;
import grondag.doomtree.block.BasinBlockEntity;
import grondag.doomtree.block.BrazierBlock;
import grondag.doomtree.block.BrazierBlockEntity;
import grondag.doomtree.block.DoomGleamBlock;
import grondag.doomtree.block.DoomHeartBlock;
import grondag.doomtree.block.DoomLeafBlock;
import grondag.doomtree.block.DoomLogBlock;
import grondag.doomtree.block.DoomSaplingBlock;
import grondag.doomtree.block.DoomedBlock;
import grondag.doomtree.block.DoomedLogBlock;
import grondag.doomtree.block.InertAlchemicalBlock;
import grondag.doomtree.block.MiasmaBlock;
import grondag.doomtree.ichor.IchorBlock;
import grondag.doomtree.treeheart.DoomHeartBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;

public enum DoomBlocks {
	;

	public static final Block ICHOR_BLOCK = REG.blockNoItem("ichor", new IchorBlock(DoomFluids.ICHOR, FabricBlockSettings.copy(Blocks.WATER).build()));

	public static Block DOOM_SAPLING = REG.block("doom_sapling", new DoomSaplingBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS).build()));
	public static Block MIASMA_BLOCK = REG.blockNoItem("miasma", new MiasmaBlock());
	public static Block DOOM_GLEAM_BLOCK =  REG.blockNoItem("doom_gleam", new DoomGleamBlock());

	public static Block DOOM_HEART_BLOCK = REG.blockNoItem("doom_tree_heart", new DoomHeartBlock(FabricBlockSettings.of(Material.WOOD).dropsLike(DOOM_SAPLING).breakByTool(FabricToolTags.AXES, 3).strength(200.0F, 1200.0F).build()));
	public static final BlockEntityType<DoomHeartBlockEntity> DOOM_HEART = REG.blockEntityType("doom_tree", DoomHeartBlockEntity::new, DOOM_HEART_BLOCK);

	public static Block PLACED_DOOM_LOG = REG.block("doom_log_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block PLACED_DOOM_LOG_CHANNEL = REG.block("doom_log_channel_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block PLACED_DOOM_LOG_TERMINAL = REG.block("doom_log_terminal_p", new DoomLogBlock(logSettings().build(), true, 1));
	public static Block DOOM_LOG = REG.blockNoItem("doom_log", new DoomLogBlock.Height(logSettings().dropsLike(PLACED_DOOM_LOG).build(), 0.04f));
	public static Block DOOM_LOG_CHANNEL = REG.blockNoItem("doom_log_channel", new DoomLogBlock.Height(logSettings().dropsLike(PLACED_DOOM_LOG_CHANNEL).build(), 0.02f));
	public static Block DOOM_LOG_TERMINAL = REG.blockNoItem("doom_log_terminal", new DoomLogBlock(logSettings().dropsLike(PLACED_DOOM_LOG_TERMINAL).build(), false, 0.02f));
	public static Block DOOM_LEAF = REG.block("doom_leaves", new DoomLeafBlock(doomedSettings(), false, 1));

	public static Block DOOMED_LOG = REG.block("doomed_log", new DoomedLogBlock(doomedSettings()));
	public static Block DOOMED_EARTH = REG.block("doomed_earth", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_STONE = REG.block("doomed_stone", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_DUST = REG.block("doomed_dust", new DoomedBlock(doomedSettings()));
	public static Block DOOMED_RESIDUE_BLOCK = REG.block("doomed_residue_block", new Block(FabricBlockSettings.of(Material.EARTH).breakByHand(true).sounds(BlockSoundGroup.SAND).build()));
	public static Block WARDING_ESSENCE_BLOCK = REG.block("warding_essence_block", new Block(FabricBlockSettings.of(Material.EARTH).breakByHand(true).sounds(BlockSoundGroup.SAND).build()));

	public static AlchemicalBlock BASIN_BLOCK = REG.block("alchemical_basin", new BasinBlock(FabricBlockSettings.of(Material.METAL).breakByHand(true).strength(0.5F, 0.5F).build()));
	public static Block INERT_BASIN_BLOCK = REG.block("inert_alchemical_basin", new InertAlchemicalBlock(BASIN_BLOCK));
	public static final BlockEntityType<BasinBlockEntity> BASIN_BLOCK_ENTITY = REG.blockEntityType("alchemical_basin", BasinBlockEntity::new, BASIN_BLOCK);

	public static AlchemicalBlock BRAZIER_BLOCK = REG.block("alchemical_brazier", new BrazierBlock(FabricBlockSettings.of(Material.METAL).breakByHand(true).strength(0.5F, 0.5F).build()));
	public static Block INERT_BRAZIER_BLOCK = REG.block("inert_alchemical_brazier", new InertAlchemicalBlock(BRAZIER_BLOCK));
	public static final BlockEntityType<BrazierBlockEntity> BRAZIER_BLOCK_ENTITY = REG.blockEntityType("alchemical_brazier", BrazierBlockEntity::new, BASIN_BLOCK);

	
	static FabricBlockSettings logSettings() {
		return FabricBlockSettings.of(Material.WOOD).breakByTool(FabricToolTags.AXES, 2).strength(3.0F, 20.0F);
	}

	static Block.Settings doomedSettings() {
		return FabricBlockSettings.of(Material.EARTH).breakByHand(true).breakInstantly().sounds(BlockSoundGroup.SAND).build();
	}
}
