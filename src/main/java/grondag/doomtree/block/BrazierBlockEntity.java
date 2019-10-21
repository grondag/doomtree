package grondag.doomtree.block;

import java.util.Random;

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
	protected void doActiveParticles(Random rand) {
		// TODO Auto-generated method stub
		
	}
}
