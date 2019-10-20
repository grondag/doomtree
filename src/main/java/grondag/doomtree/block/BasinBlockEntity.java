package grondag.doomtree.block;

import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.entity.BlockEntityType;

public class BasinBlockEntity extends AlchemicalBlockEntity {

	public BasinBlockEntity(BlockEntityType<?> entityType) {
		super(entityType);
	}

	public BasinBlockEntity() {
		this(DoomBlocks.BASIN_BLOCK_ENTITY);
	}

	@Override
	protected void doActiveParticles() {
		// TODO Auto-generated method stub
		
	}
}
