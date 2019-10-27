package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import grondag.doomtree.block.ForebodingShrubFeature;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public enum DoomFeatures {
	;

	public static final Feature<DefaultFeatureConfig> FOREBODING_SHRUB = Registry.register(Registry.FEATURE, REG.id("sweet_berry_bush"), new ForebodingShrubFeature(DefaultFeatureConfig::deserialize));

	static {
		addShrub(Biomes.BADLANDS);
		addShrub(Biomes.BADLANDS_PLATEAU);
		addShrub(Biomes.ERODED_BADLANDS);
		addShrub(Biomes.MODIFIED_BADLANDS_PLATEAU);
		addShrub(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU);
		addShrub(Biomes.SAVANNA);
		addShrub(Biomes.SAVANNA_PLATEAU);
		addShrub(Biomes.SHATTERED_SAVANNA);
		addShrub(Biomes.SHATTERED_SAVANNA_PLATEAU);
		addShrub(Biomes.WOODED_BADLANDS_PLATEAU);
	}

	static void addShrub(Biome target) {
		target.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, Biome.configureFeature(FOREBODING_SHRUB, new DefaultFeatureConfig(), Decorator.COUNT_CHANCE_HEIGHTMAP, new CountChanceDecoratorConfig(1, 0.1F)));
	}
}
