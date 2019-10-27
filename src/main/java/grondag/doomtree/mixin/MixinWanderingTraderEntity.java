package grondag.doomtree.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import grondag.doomtree.registry.DoomItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;

@Mixin(WanderingTraderEntity.class)
public abstract class MixinWanderingTraderEntity extends AbstractTraderEntity {

	public MixinWanderingTraderEntity(EntityType<? extends AbstractTraderEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "fillRecipes", at = @At(value = "RETURN"))
	private void onfillRecipes(CallbackInfo ci) {
		if (this.random.nextBoolean()) {
			offers.add(new TradeOffer(new ItemStack(Items.EMERALD, 7), new ItemStack(DoomItems.ALCHEMICAL_ENGINE, 1), 1, 12, 1));
		}
	}
}
