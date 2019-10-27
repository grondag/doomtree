package grondag.doomtree.ichor;

import java.util.List;
import java.util.Optional;

import grondag.doomtree.recipe.IchorRecipe;
import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.treeheart.DoomTreeTracker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.BaseFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
			}
		}

		super.onEntityCollision(blockState, world, pos, entity);
	}

	private void spawnCraftingResult(World world, BlockPos pos, ItemStack result) {
		ItemEntity itemEntity = new ItemEntity(world, pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5, result);
		world.spawnEntity(itemEntity);
	}

	@Override
	public Fluid tryDrainFluid(IWorld world, BlockPos pos, BlockState blockState) {
		if (blockState.get(LEVEL) == 0) {
			world.setBlockState(pos, DoomTreeTracker.isNear(world.getWorld(), pos) ? DoomBlockStates.MIASMA_STATE : Blocks.AIR.getDefaultState(), 11);
			return this.fluid;
		} else {
			return Fluids.EMPTY;
		}
	}

	@Override
	public void onBlockRemoved(BlockState myState, World world, BlockPos blockPos, BlockState newState, boolean someFlag) {
		super.onBlockRemoved(myState, world, blockPos, newState, someFlag);

		if (!world.isClient) {
			final FluidState fluidState = newState.getFluidState();
			
			if(fluidState != null && !fluidState.isEmpty() && fluidState.isStill() && newState.getBlock() instanceof FluidBlock) {
				return;
			} 
			
			DoomTreeTracker.reportBreak(world, blockPos, false);
		}
	}
}
