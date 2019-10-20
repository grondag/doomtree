package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.fermion.registrar.SimpleArmorMaterial;
import grondag.fermion.registrar.SimpleToolMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.sound.SoundEvents;

public enum DoomItems {
	;
	
	public static final BucketItem ICHOR_BUCKET = REG.item("ichor_bucket", new BucketItem(DoomFluids.ICHOR, REG.itemSettings().recipeRemainder(Items.BUCKET).maxCount(1)));
	public static Item PLACED_DOOM_LOG_ITEM = Item.fromBlock(DoomBlocks.PLACED_DOOM_LOG);
	public static Item DOOM_LOG_ITEM = Item.fromBlock(DoomBlocks.DOOM_LOG);
	public static Item DOOM_LEAF_ITEM = Item.fromBlock(DoomBlocks.DOOM_LEAF);
	public static Item WARDING_ESSENCE_BLOCK_ITEM = Item.fromBlock(DoomBlocks.WARDING_ESSENCE_BLOCK);
	public static Item DOOMED_RESIDUE_ITEM = REG.item("doomed_residue");
	public static Item WARDING_ESSENCE_ITEM = REG.item("warding_essence");
	public static Item DOOM_FRAGMENT_ITEM = REG.item("doom_fragment");
	public static Item ALCHEMICAL_BASIN_FRAME = REG.item("alchemical_basin_frame");
	public static Item ALCHEMICAL_BRAZIER_FRAME = REG.item("alchemical_brazier_frame");
	public static Item WARDED_IRON_INGOT = REG.item("warded_iron_ingot");
	public static Item WARDED_IRON_NUGGET = REG.item("warded_iron_nugget");
	public static Item WARDED_DIAMOND = REG.item("warded_diamond");
	public static Item WARDED_STICK = REG.item("warded_stick");

	public static final ArmorMaterial WARDED_IRON_ARMOR_MATERIAL = SimpleArmorMaterial.of("warded_iron", 17, new int[]{2, 5, 6, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F, WARDED_IRON_INGOT);
	public static final Item WARDED_IRON_HELMET =  REG.armorItem("warded_iron_helmet", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.HEAD);
	public static final Item WARDED_IRON_CHESTPLATE =  REG.armorItem("warded_iron_chestplate", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.CHEST);
	public static final Item WARDED_IRON_LEGGINGS =  REG.armorItem("warded_iron_leggings", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.LEGS);
	public static final Item WARDED_IRON_BOOTS =  REG.armorItem("warded_iron_boots", WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.FEET);
	
	public static final ArmorMaterial ENCRUSTED_ARMOR_MATERIAL = SimpleArmorMaterial.of("encrusted", 37, new int[]{3, 6, 8, 3}, 11, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F, WARDED_DIAMOND);
	public static final Item ENCRUSTED_HELMET =  REG.armorItem("encrusted_helmet", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.HEAD);
	public static final Item ENCRUSTED_CHESTPLATE =  REG.armorItem("encrusted_chestplate", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.CHEST);
	public static final Item ENCRUSTED_LEGGINGS =  REG.armorItem("encrusted_leggings", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.LEGS);
	public static final Item ENCRUSTED_BOOTS =  REG.armorItem("encrusted_boots", ENCRUSTED_ARMOR_MATERIAL, EquipmentSlot.FEET);
	
	public static final ToolMaterial WARDED_IRON_TOOL_MATERIAL = SimpleToolMaterial.of(2, 300, 6.6F, 2.2F, 15, WARDED_IRON_INGOT);
	public static final Item WARDED_IRON_HOE = REG.item("warded_iron_hoe", new HoeItem(WARDED_IRON_TOOL_MATERIAL, -1.0F, REG.itemSettings()){});
	public static final Item WARDED_IRON_SHOVEL = REG.item("warded_iron_shovel", new ShovelItem(WARDED_IRON_TOOL_MATERIAL, 1.5F, -3.0F, REG.itemSettings()){});
	public static final Item WARDED_IRON_PICKAXE = REG.item("warded_iron_pickaxe", new PickaxeItem(WARDED_IRON_TOOL_MATERIAL, 1, -2.8F, REG.itemSettings()){});
	public static final Item WARDED_IRON_AXE = REG.item("warded_iron_axe", new AxeItem(WARDED_IRON_TOOL_MATERIAL, 6.0F, -3.1F, REG.itemSettings()){});
	public static final Item WARDED_IRON_SWORD = REG.item("warded_iron_sword", new SwordItem(WARDED_IRON_TOOL_MATERIAL, 3, -2.4F, REG.itemSettings()){});

	
	public static final ToolMaterial ENCRUSTED_TOOL_MATERIAL = SimpleToolMaterial.of(3, 1800, 8.8F, 3.3F, 11, WARDED_DIAMOND);
	public static final Item ENCRUSTED_HOE = REG.item("encrusted_hoe", new HoeItem(ENCRUSTED_TOOL_MATERIAL, 0.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_SHOVEL = REG.item("encrusted_shovel", new ShovelItem(ENCRUSTED_TOOL_MATERIAL, 1.5F, -3.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_PICKAXE = REG.item("encrusted_pickaxe", new PickaxeItem(ENCRUSTED_TOOL_MATERIAL, 1, -2.8F, REG.itemSettings()){});
	public static final Item ENCRUSTED_AXE = REG.item("encrusted_axe", new AxeItem(ENCRUSTED_TOOL_MATERIAL, 5.0F, -3.0F, REG.itemSettings()){});
	public static final Item ENCRUSTED_SWORD = REG.item("encrusted_sword", new SwordItem(ENCRUSTED_TOOL_MATERIAL, 3, -2.4F, REG.itemSettings()){});
}
