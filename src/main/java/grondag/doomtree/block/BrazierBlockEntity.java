package grondag.doomtree.block;

import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.entity.BlockEntityType;

public class BrazierBlockEntity extends AlchemicalBlockEntity {

	public BrazierBlockEntity(BlockEntityType<?> entityType) {
		super(entityType);
	}

	public BrazierBlockEntity() {
		this(DoomBlocks.BRAZIER_BLOCK_ENTITY);
	}

	@Override
	protected void doActiveParticles() {
		// TODO Auto-generated method stub
		
	}
}
