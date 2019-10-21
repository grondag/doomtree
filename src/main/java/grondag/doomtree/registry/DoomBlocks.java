package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import java.util.Random;

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
import grondag.doomtree.block.WardedBarrelBlock;
import grondag.doomtree.block.WardedBarrelBlockEntity;
import grondag.doomtree.ichor.IchorBlock;
import grondag.doomtree.treeheart.DoomHeartBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.LogBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SignBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

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

	public static Block WARDED_IRON_BLOCK = REG.block("warded_iron_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).strength(5.0F, 8.0F).sounds(BlockSoundGroup.METAL).build()));
	public static Block WARDED_DIAMOND_BLOCK = REG.block("warded_diamond_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.DIAMOND).strength(5.0F, 10.0F).sounds(BlockSoundGroup.METAL).build()));

	public static Block WARDED_LADDER = REG.block("warded_ladder", new LadderBlock(FabricBlockSettings.of(Material.PART, MaterialColor.CLAY).strength(1F, 3F).sounds(BlockSoundGroup.LADDER).build()) {});
	public static Block WARDED_IRON_BARS = REG.block("warded_iron_bars", new PaneBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.AIR).strength(5.0F, 8.0F).sounds(BlockSoundGroup.METAL).build()) {});

	public static Block WARDED_IRON_DOOR = REG.block("warded_iron_door", new DoorBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).strength(5.0F, 8.0F).sounds(BlockSoundGroup.METAL).build()) {});

	public static Block WARDED_IRON_PRESSURE_PLATE = REG.block("warded_iron_pressure_plate", new WeightedPressurePlateBlock(150, FabricBlockSettings.of(Material.METAL).noCollision().strength(1F, 8.0F).sounds(BlockSoundGroup.METAL).build()) {});

	public static Block WARDED_IRON_TRAPDOOR = REG.block("warded_iron_trapdoor", new TrapdoorBlock(FabricBlockSettings.of(Material.METAL).strength(5.0F, 8.0F).sounds(BlockSoundGroup.METAL).build()) {});

	public static Block WARDED_CONCRETE_POWDER = REG.block("warded_concrete_powder", new Block(FabricBlockSettings.of(Material.SAND, MaterialColor.CLAY).strength(0.5F, 0.5F).sounds(BlockSoundGroup.SAND).build()));

	public static Block WARDED_CONCRETE = REG.block("warded_concrete", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.CLAY).strength(8.0F, 1200).build()));

	public static Block WARDED_GLASS_PANE = REG.block("warded_glass_pane", new PaneBlock(FabricBlockSettings.of(Material.GLASS).strength(1F, 8.0F).sounds(BlockSoundGroup.GLASS).build()) {
		@Override
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.TRANSLUCENT;
		}

		@Override
		@Environment(EnvType.CLIENT)
		public boolean isSideInvisible(BlockState myState, BlockState otherState, Direction face) {
			if (otherState.getBlock() == this) {
				if (!face.getAxis().isHorizontal()) {
					return myState == otherState;
				}

				if (myState.get(FACING_PROPERTIES.get(face)) && otherState.get(FACING_PROPERTIES.get(face.getOpposite()))) {
					return true;
				}
			}

			return super.isSideInvisible(myState, otherState, face);
		}
	});

	public static Block WARDED_GLASS = REG.block("warded_glass", new AbstractGlassBlock(FabricBlockSettings.of(Material.GLASS, DyeColor.WHITE).strength(1F, 8F).sounds(BlockSoundGroup.GLASS).build()) {
		@Override
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.TRANSLUCENT;
		}
	});

	public static final Block WARDED_BARREL = REG.block("warded_barrel", new WardedBarrelBlock(FabricBlockSettings.of(Material.WOOD).strength(3F, 10F).sounds(BlockSoundGroup.WOOD).build()));

	public static final BlockEntityType<WardedBarrelBlockEntity> WARDED_BARREL_BLOCK_ENTITY = REG.blockEntityType("warded_barrel", WardedBarrelBlockEntity::new, WARDED_BARREL);

	public static Block WARDED_LANTERN = REG.block("warded_lantern", new LanternBlock(FabricBlockSettings.of(Material.METAL).strength(3.5F, 10F).sounds(BlockSoundGroup.LANTERN).lightLevel(15).build()));

	public static Block WARDED_TORCH = REG.blockNoItem("warded_torch", new TorchBlock(FabricBlockSettings.of(Material.PART).noCollision().breakInstantly().lightLevel(14).sounds(BlockSoundGroup.WOOD).build()) {
		@Override
		@Environment(EnvType.CLIENT)
		public void randomDisplayTick(BlockState blockState, World world, BlockPos pos, Random rand) {
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.7D;
			final double z = pos.getZ() + 0.5D;
			world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
			world.addParticle(DoomParticles.WARDED_FLAME, x, y, z, 0.0D, 0.0D, 0.0D);
		}
	});

	public static Block WARDED_WALL_TORCH = REG.blockNoItem("warded_wall_torch", new WallTorchBlock(FabricBlockSettings.of(Material.PART).noCollision().breakInstantly().lightLevel(14).sounds(BlockSoundGroup.WOOD).dropsLike(WARDED_TORCH).build()) {
		@Override
		@Environment(EnvType.CLIENT)
		public void randomDisplayTick(BlockState blockState, World world, BlockPos pos, Random rand) {
			Direction face = blockState.get(FACING);
			final double x = pos.getX() + 0.5D;
			final double y = pos.getY() + 0.7D;
			final double z = pos.getZ() + 0.5D;
			Direction opposite = face.getOpposite();
			world.addParticle(ParticleTypes.SMOKE, x + 0.27D * opposite.getOffsetX(), y + 0.22D, z + 0.27D * opposite.getOffsetZ(), 0.0D, 0.0D, 0.0D);
			world.addParticle(DoomParticles.WARDED_FLAME, x + 0.27D * opposite.getOffsetX(), y + 0.22D, z + 0.27D * opposite.getOffsetZ(), 0.0D, 0.0D, 0.0D);
		}
	});

	public static Block WARDED_LEVER = REG.block("warded_lever", new LeverBlock(FabricBlockSettings.of(Material.PART).noCollision().strength(1, 8).sounds(BlockSoundGroup.WOOD).build()) {});

	public static Block WARDED_STONE = REG.block("warded_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(1.5F, 6.0F).build()));
	public static Block WARDED_STONE_BUTTON = REG.block("warded_stone_button", new StoneButtonBlock(FabricBlockSettings.of(Material.PART).noCollision().strength(1, 8).build()) {});
	public static Block WARDED_STONE_PRESSURE_PLATE = REG.block("warded_stone_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.MOBS, FabricBlockSettings.of(Material.STONE).noCollision().strength(1, 8).build()) {});
	public static Block WARDED_STONE_SLAB = REG.block("warded_stone_slab", new SlabBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(2.0F, 6.0F).build()));
	public static Block WARDED_STONE_STAIRS = REG.block("warded_stone_stairs", new StairsBlock(WARDED_STONE.getDefaultState(), FabricBlockSettings.copy(WARDED_STONE).build()) {});
	public static Block WARDED_SMOOTH_STONE = REG.block("warded_smooth_stone", new Block(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(2.0F, 6.0F).build()));
	public static Block WARDED_SMOOTH_STONE_SLAB = REG.block("warded_smooth_stone_slab", new SlabBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(2.0F, 6.0F).build()));
	public static Block WARDED_STONE_BRICKS = REG.block("warded_stone_bricks", new Block(FabricBlockSettings.of(Material.STONE).strength(1.5F, 6.0F).build()));
	public static Block WARDED_STONE_BRICK_SLAB = REG.block("warded_stone_brick_slab", new SlabBlock(FabricBlockSettings.of(Material.STONE, MaterialColor.STONE).strength(2.0F, 6.0F).build()));
	public static Block WARDED_STONE_BRICK_STAIRS = REG.block("warded_stone_brick_stairs", new StairsBlock(WARDED_STONE_BRICKS.getDefaultState(), FabricBlockSettings.copy(WARDED_STONE_BRICKS).build()) {});
	public static Block WARDED_STONE_BRICK_WALL = REG.block("warded_stone_brick_wall", new WallBlock(FabricBlockSettings.copy(WARDED_STONE_BRICKS).build()));

	public static Block WARDED_WOOD_LOG = REG.block("warded_wood_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.of(Material.WOOD, MaterialColor.CLAY).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_WOOD = REG.block("warded_wood_wood", new PillarBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_STRIPPED_LOG = REG.block("stripped_warded_wood_log", new LogBlock(MaterialColor.WOOD, FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_STRIPPED_WOOD = REG.block("stripped_warded_wood_wood", new PillarBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));

	public static Block WARDED_WOOD_PLANKS = REG.block("warded_wood_planks", new Block(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 3.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_BUTTON = REG.block("warded_wood_button", new WoodButtonBlock(FabricBlockSettings.of(Material.PART).noCollision().strength(1.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()) {});
	public static Block WARDED_WOOD_DOOR = REG.block("warded_wood_door", new DoorBlock(FabricBlockSettings.copy(WARDED_WOOD_PLANKS).strength(3.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()) {});
	public static Block WARDED_WOOD_FENCE_GATE = REG.block("warded_wood_fence_gate", new FenceGateBlock(FabricBlockSettings.copy(WARDED_WOOD_PLANKS).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_FENCE = REG.block("warded_wood_fence", new FenceBlock(FabricBlockSettings.copy(WARDED_WOOD_PLANKS).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_PRESSURE_PLATE = REG.block("warded_wood_pressure_plate", new PressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING, FabricBlockSettings.copy(WARDED_WOOD_PLANKS).noCollision().strength(1.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()) {});
	public static Block WARDED_WOOD_SLAB = REG.block("warded_wood_slab", new SlabBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(2.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_STAIRS = REG.block("warded_wood_stairs", new StairsBlock(WARDED_WOOD_PLANKS.getDefaultState(), FabricBlockSettings.copy(WARDED_WOOD_PLANKS).build()) {});
	public static Block WARDED_WOOD_TRAPDOOR = REG.block("warded_wood_trapdoor", new TrapdoorBlock(FabricBlockSettings.of(Material.WOOD, MaterialColor.WOOD).strength(3.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()) {});
	public static Block WARDED_WOOD_SIGN = REG.blockNoItem("warded_wood_sign", new SignBlock(FabricBlockSettings.of(Material.WOOD).noCollision().strength(1.0F, 8.0F).sounds(BlockSoundGroup.WOOD).build()));
	public static Block WARDED_WOOD_WALL_SIGN = REG.blockNoItem("warded_wood_wall_sign", new WallSignBlock(FabricBlockSettings.of(Material.WOOD).noCollision().strength(1.0F, 8.0F).sounds(BlockSoundGroup.WOOD).dropsLike(WARDED_WOOD_SIGN).build()) {});
}
