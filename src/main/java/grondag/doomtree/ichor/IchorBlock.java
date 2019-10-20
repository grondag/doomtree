package grondag.doomtree.ichor;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

import grondag.doomtree.recipe.IchorRecipe;

public class IchorBlock extends FluidBlock {
	public IchorBlock(BaseFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public void onEntityCollision(BlockState blockState, World world, BlockPos pos, Entity entity) {
		if(!world.isClient) {
			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity = (LivingEntity) entity;
				if (!livingEntity.isUndead()) {
					livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.POISON, 20));
					livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 20));
					livingEntity.addPotionEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20));
				}
			}

			if(entity instanceof ItemEntity) {
				List<ItemEntity> entities = world.getEntities(ItemEntity.class, new Box(pos));
				BasicInventory inventory = new BasicInventory(entities.size());

				entities.forEach(itemEntity -> {
					ItemStack stack = itemEntity.getStack();
					inventory.add(stack);
				});

				Optional<IchorRecipe> match = world.getRecipeManager()
					.getFirstMatch(IchorRecipe.Type.INSTANCE, inventory, world);

				if (match.isPresent()) {
					spawnCraftingResult(world, pos, match.get().getOutput());

					for (Ingredient ingredient : match.get().getIngredients()) {
						for (ItemEntity testEntity : entities) {
							if (ingredient.method_8093(testEntity.getStack())) {
								testEntity.getStack().decrement(1);
								break;
							}
						}
					}
				}
			} else {
				if (pos.equals(entity.getBlockPos())) {
					if (entity.getType() == EntityType.SKELETON) {
						WitherSkeletonEntity witherSkeletonEntity = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, world);
						witherSkeletonEntity.copyPositionAndRotation(entity);
						entity.remove();

						world.spawnEntity(witherSkeletonEntity);
					} else if (entity.getType() == EntityType.SPIDER) {
						CaveSpiderEntity caveSpiderEntity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
						caveSpiderEntity.copyPositionAndRotation(entity);
						entity.remove();

						world.spawnEntity(caveSpiderEntity);
					} else if (entity.getType() == EntityType.VILLAGER && world.getDifficulty() != Difficulty.PEACEFUL) {
						WitchEntity witchEntity = new WitchEntity(EntityType.WITCH, world);
						witchEntity.copyPositionAndRotation(entity);
						entity.remove();

						world.spawnEntity(witchEntity);
					}
				}
			}
		}

		super.onEntityCollision(blockState, world, pos, entity);
	}

	private void spawnCraftingResult(World world, BlockPos pos, ItemStack result) {
		ItemEntity itemEntity = new ItemEntity(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, result);
		world.spawnEntity(itemEntity);
	}
}
