package grondag.fermion.client;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.Sprite;
import net.minecraft.fluid.FluidState;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

import java.util.Arrays;
import java.util.Collection;

import grondag.doomtree.DoomTree;
import grondag.doomtree.registry.DoomFluids;

public class FluidResourceLoader implements SimpleSynchronousResourceReloadListener {
	@Override
	public Identifier getFabricId() {
		return DoomTree.id("fluid_resource_loader");
	}
	
	@Override
	public Collection<Identifier> getFabricDependencies() {
		return Arrays.asList(ResourceReloadListenerKeys.MODELS, ResourceReloadListenerKeys.TEXTURES);
	}
	
	@Override
	public void apply(ResourceManager resourceManager) {
		FluidRenderHandler ichorRenderHandler = new FluidRenderHandler() {
			@Override
			public Sprite[] getFluidSprites(ExtendedBlockView extendedBlockView, BlockPos blockPos, FluidState fluidState) {
				return new Sprite[]{MinecraftClient.getInstance().getSpriteAtlas().getSprite("block/water_still"), MinecraftClient.getInstance().getSpriteAtlas().getSprite("block/water_flow")};
			}
			
			@Override
			public int getFluidColor(ExtendedBlockView view, BlockPos pos, FluidState state) {
				return 0x405020;
			}
		};
		
		FluidRenderHandlerRegistry.INSTANCE.register(DoomFluids.ICHOR, ichorRenderHandler);
		FluidRenderHandlerRegistry.INSTANCE.register(DoomFluids.FLOWING_ICHOR, ichorRenderHandler);
	}
}
