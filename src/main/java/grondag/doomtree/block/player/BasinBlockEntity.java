package grondag.doomtree.block.player;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomParticles;
import net.minecraft.block.entity.BlockEntityType;

public class BasinBlockEntity extends AlchemicalBlockEntity {

	public BasinBlockEntity(BlockEntityType<?> entityType) {
		super(entityType, DoomParticles.BASIN_ACTIVE);
	}

	public BasinBlockEntity() {
		this(DoomBlocks.BASIN_BLOCK_ENTITY);
	}
}
