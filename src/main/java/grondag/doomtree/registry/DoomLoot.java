package grondag.doomtree.registry;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.FabricLootSupplierBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback.LootTableSetter;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.loot.ConstantLootTableRange;
import net.minecraft.world.loot.LootManager;
import net.minecraft.world.loot.LootTables;
import net.minecraft.world.loot.condition.RandomChanceLootCondition;
import net.minecraft.world.loot.entry.ItemEntry;

public enum DoomLoot {
	;

	private static final Object2FloatOpenHashMap<Identifier> CHEST_TARGETS = new Object2FloatOpenHashMap<>();
	
	static {
		CHEST_TARGETS.addTo(LootTables.ABANDONED_MINESHAFT_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.BURIED_TREASURE_CHEST, 1f);
		CHEST_TARGETS.addTo(LootTables.DESERT_PYRAMID_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.END_CITY_TREASURE_CHEST, 1f);
		CHEST_TARGETS.addTo(LootTables.IGLOO_CHEST_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.JUNGLE_TEMPLE_CHEST, 0.6f);
		CHEST_TARGETS.addTo(LootTables.NETHER_BRIDGE_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.PILLAGER_OUTPOST_CHEST, 0.5f);
		CHEST_TARGETS.addTo(LootTables.SHIPWRECK_TREASURE_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.SHIPWRECK_SUPPLY_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.SHIPWRECK_MAP_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.SIMPLE_DUNGEON_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.STRONGHOLD_CORRIDOR_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.STRONGHOLD_CROSSING_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.STRONGHOLD_LIBRARY_CHEST, 0.4f);
		CHEST_TARGETS.addTo(LootTables.UNDERWATER_RUIN_BIG_CHEST, 1f);
		CHEST_TARGETS.addTo(LootTables.UNDERWATER_RUIN_SMALL_CHEST, 1f);
		CHEST_TARGETS.addTo(LootTables.WOODLAND_MANSION_CHEST, 1f);
	}

	public static void init(ResourceManager resourceManager, LootManager manager, Identifier id, FabricLootSupplierBuilder supplier, LootTableSetter setter) {
		if (CHEST_TARGETS.containsKey(id)) {
	        FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
	                .withRolls(ConstantLootTableRange.create(1))
	                .withCondition(RandomChanceLootCondition.builder(CHEST_TARGETS.getFloat(id)).build())
	                .withEntry(ItemEntry.builder(DoomItems.ALCHEMICAL_ENGINE));
	        supplier.withPool(poolBuilder);
		}
	}
}
