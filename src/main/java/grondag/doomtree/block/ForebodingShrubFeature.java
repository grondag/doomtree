package grondag.doomtree.block;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ForebodingShrubFeature extends Feature<DefaultFeatureConfig> {
	public ForebodingShrubFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> function_1) {
		super(function_1);
	}

	@Override
	public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGen, Random rand, BlockPos pos, DefaultFeatureConfig config) {
		final BlockState blockState = DoomBlocks.FOREBODING_SHRUB.getDefaultState();

		for(int i = 0; i < 64; ++i) {
			BlockPos p = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

			if (world.isAir(p) && (!world.getDimension().isNether() || p.getY() < 255) && blockState.canPlaceAt(world, p)) {
				world.setBlockState(p, blockState, 2);
				return true;
			}
		}

		return false;
	}
}
