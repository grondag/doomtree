package grondag.doomtree.block.player;

import grondag.doomtree.registry.DoomBlocks;
import grondag.fermion.block.sign.OpenSignBlockEntity;
import net.minecraft.util.DyeColor;

public class WardedWoodSignBlockEntity extends OpenSignBlockEntity {
	public WardedWoodSignBlockEntity() {
		super(DoomBlocks.WARDED_WOOD_SIGN_BLOCK_ENTITY);
		setLit(true);
		setTextColor(DyeColor.WHITE);
	}
}
