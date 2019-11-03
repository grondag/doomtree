package grondag.doomtree.block.player;

import java.util.Random;

import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomParticles;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;

public class BrazierBlockEntity extends AlchemicalBlockEntity {

	public BrazierBlockEntity(BlockEntityType<?> entityType) {
		super(entityType, DoomParticles.BRAZIER_ACTIVE);
	}

	public BrazierBlockEntity() {
		this(DoomBlocks.BRAZIER_BLOCK_ENTITY);
	}
	
	@Override
	protected void doActiveParticles(Random rand) {
		super.doActiveParticles(rand);
		
		if (rand.nextInt(8) == 0) {
			final BlockPos pos = this.pos;
			world.addParticle(ParticleTypes.SMOKE, pos.getX() + rand.nextDouble(), pos.getY() + 1.0 + rand.nextDouble() * 0.2, pos.getZ() + rand.nextDouble(), 0.0D, 0.0D, 0.0D);	
		}
	}
}
