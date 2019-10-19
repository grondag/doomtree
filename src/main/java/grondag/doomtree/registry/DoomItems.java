package grondag.doomtree.registry;

import grondag.doomtree.DoomTree;
import grondag.fermion.material.SimpleArmorMaterial;
import grondag.fermion.material.SimpleToolMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
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
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.registry.Registry;

public class DoomItems {
	public static final BucketItem ICHOR_BUCKET = register("ichor_bucket", new BucketItem(DoomFluids.ICHOR, new Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1).group(DoomTree.GROUP)));
	public static Item PLACED_DOOM_LOG_ITEM = Item.fromBlock(DoomBlocks.PLACED_DOOM_LOG);
	public static Item DOOM_LOG_ITEM = Item.fromBlock(DoomBlocks.DOOM_LOG);
	public static Item DOOM_LEAF_ITEM = Item.fromBlock(DoomBlocks.DOOM_LEAF);
	public static Item WARDING_ESSENCE_BLOCK_ITEM = Item.fromBlock(DoomBlocks.WARDING_ESSENCE_BLOCK);
	public static Item DOOMED_RESIDUE_ITEM = Registry.register(Registry.ITEM, DoomTree.id("doomed_residue"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item WARDING_ESSENCE_ITEM = Registry.register(Registry.ITEM, DoomTree.id("warding_essence"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item DOOM_FRAGMENT_ITEM = Registry.register(Registry.ITEM, DoomTree.id("doom_fragment"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item ALCHEMICAL_BASIN_FRAME = Registry.register(Registry.ITEM, DoomTree.id("alchemical_basin_frame"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item WARDED_IRON_INGOT = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_ingot"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item WARDED_IRON_NUGGET = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_nugget"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item WARDED_DIAMOND = Registry.register(Registry.ITEM, DoomTree.id("warded_diamond"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static Item WARDED_STICK = Registry.register(Registry.ITEM, DoomTree.id("warded_stick"), new Item((new Item.Settings()).group(DoomTree.GROUP)));
	public static final ArmorMaterial WARDED_IRON_ARMOR_MATERIAL = new SimpleArmorMaterial("warded_iron", 17, new int[]{2, 5, 6, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.5F, () -> {
		return Ingredient.ofItems(WARDED_IRON_INGOT);
	});
	public static final ArmorMaterial ENCRUSTED_ARMOR_MATERIAL = new SimpleArmorMaterial("encrusted_iron", 37, new int[]{3, 6, 8, 3}, 11, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.5F, () -> {
		return Ingredient.ofItems(WARDED_DIAMOND);
	});
	public static final Item WARDED_IRON_HELMET =  Registry.register(Registry.ITEM, DoomTree.id("warded_iron_helmet"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.HEAD, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item WARDED_IRON_CHESTPLATE =  Registry.register(Registry.ITEM, DoomTree.id("warded_iron_chestplate"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.CHEST, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item WARDED_IRON_LEGGINGS =  Registry.register(Registry.ITEM, DoomTree.id("warded_iron_leggings"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.LEGS, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item WARDED_IRON_BOOTS =  Registry.register(Registry.ITEM, DoomTree.id("warded_iron_boots"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.FEET, (new Item.Settings()).group(DoomTree.GROUP)));
	
	public static final Item ENCRUSTED_HELMET =  Registry.register(Registry.ITEM, DoomTree.id("encrusted_helmet"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.HEAD, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item ENCRUSTED_CHESTPLATE =  Registry.register(Registry.ITEM, DoomTree.id("encrusted_chestplate"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.CHEST, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item ENCRUSTED_LEGGINGS =  Registry.register(Registry.ITEM, DoomTree.id("encrusted_leggings"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.LEGS, (new Item.Settings()).group(DoomTree.GROUP)));
	public static final Item ENCRUSTED_BOOTS =  Registry.register(Registry.ITEM, DoomTree.id("encrusted_boots"), new ArmorItem(WARDED_IRON_ARMOR_MATERIAL, EquipmentSlot.FEET, (new Item.Settings()).group(DoomTree.GROUP)));
	
	public static final ToolMaterial WARDED_IRON_TOOL_MATERIAL = new SimpleToolMaterial(2, 300, 6.6F, 2.2F, 15, () -> {
		return Ingredient.ofItems(WARDED_IRON_INGOT);
	});
	
	public static final ToolMaterial ENCRUSTED_TOOL_MATERIAL = new SimpleToolMaterial(3, 1800, 8.8F, 3.3F, 11, () -> {
		return Ingredient.ofItems(WARDED_DIAMOND);
	});
	
	public static final Item WARDED_IRON_HOE = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_hoe"), new HoeItem(WARDED_IRON_TOOL_MATERIAL, -1.0F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item WARDED_IRON_SHOVEL = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_shovel"), new ShovelItem(WARDED_IRON_TOOL_MATERIAL, 1.5F, -3.0F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item WARDED_IRON_PICKAXE = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_pickaxe"), new PickaxeItem(WARDED_IRON_TOOL_MATERIAL, 1, -2.8F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item WARDED_IRON_AXE = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_axe"), new AxeItem(WARDED_IRON_TOOL_MATERIAL, 6.0F, -3.1F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item WARDED_IRON_SWORD = Registry.register(Registry.ITEM, DoomTree.id("warded_iron_sword"), new SwordItem(WARDED_IRON_TOOL_MATERIAL, 3, -2.4F, new Item.Settings().group(DoomTree.GROUP)){});

	public static final Item ENCRUSTED_HOE = Registry.register(Registry.ITEM, DoomTree.id("encrusted_hoe"), new HoeItem(ENCRUSTED_TOOL_MATERIAL, -1.0F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item ENCRUSTED_SHOVEL = Registry.register(Registry.ITEM, DoomTree.id("encrusted_shovel"), new ShovelItem(ENCRUSTED_TOOL_MATERIAL, 1.5F, -3.0F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item ENCRUSTED_PICKAXE = Registry.register(Registry.ITEM, DoomTree.id("encrusted_pickaxe"), new PickaxeItem(ENCRUSTED_TOOL_MATERIAL, 1, -2.8F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item ENCRUSTED_AXE = Registry.register(Registry.ITEM, DoomTree.id("encrusted_axe"), new AxeItem(ENCRUSTED_TOOL_MATERIAL, 6.0F, -3.1F, new Item.Settings().group(DoomTree.GROUP)){});
	public static final Item ENCRUSTED_SWORD = Registry.register(Registry.ITEM, DoomTree.id("encrusted_sword"), new SwordItem(ENCRUSTED_TOOL_MATERIAL, 3, -2.4F, new Item.Settings().group(DoomTree.GROUP)){});

	private DoomItems() {
		// NO-OP
	}

	public static void init() {
		// NO-OP
	}

	static Item.Settings newSettings() {
		return new Item.Settings().group(DoomTree.GROUP);
	}

	protected static <T extends Item> T register(String name, T item) {
		return Registry.register(Registry.ITEM, DoomTree.id(name), item);
	}
}
