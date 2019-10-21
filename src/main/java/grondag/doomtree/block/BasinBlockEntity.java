package grondag.doomtree.block;

import java.util.Random;

import grondag.doomtree.particle.WardingParticle;
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
	protected void doActiveParticles(Random rand) {
		if (rand.nextInt(8) == 0) {
			WardingParticle.spawn(world, pos, rand, 1, 1);
		}
	}
}
