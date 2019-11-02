package grondag.doomtree.render;

import grondag.doomtree.DoomTreeClient;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomEntities;
import grondag.fermion.client.ClientRegistrar;
import ladysnake.satin.api.event.ResolutionChangeCallback;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.experimental.managed.Uniform2f;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.fabric.api.client.render.InvalidateRenderStateCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;

public enum DoomEffectRender {
	;

	private static final ManagedShaderEffect GREYSCALE_SHADER = ShaderEffectManager.getInstance()
		.manage(DoomTreeClient.REGISTRAR.id("shaders/post/doom.json"));

	private static MinecraftClient MC =MinecraftClient.getInstance();

	private static Uniform2f DOOM_UNIFORM = GREYSCALE_SHADER.findUniform2f("Doom");

	private static int frameCount = 0;

	private static void render (final float tickDelta) {

		// Workaround for Satin #1 - comment out this block to reproduce
		if (frameCount < 2)  {
			if (frameCount++ == 1) {
				ResolutionChangeCallback.EVENT.invoker().onResolutionChanged(MC.window.getFramebufferWidth(), MC.window.getFramebufferHeight());
			}
		}

		if (MC.player != null) {
			final StatusEffectInstance doom = MC.player.getStatusEffect(DoomEntities.DOOM_EFFECT);

			if (doom != null) {
				final int a = doom.getAmplifier();
				final float scale = a + (float)doom.getDuration() / DoomEffect.durationTicks(a);
				DOOM_UNIFORM.set(Math.min(1f, scale / 6f), Math.max(0f, (scale - 6f) / 3f));
				GREYSCALE_SHADER.render(tickDelta);
			}
		}
	}

	public static void init(final ClientRegistrar registrar) {
		ShaderEffectRenderCallback.EVENT.register(DoomEffectRender::render);

		InvalidateRenderStateCallback.EVENT.register(() -> {
			GREYSCALE_SHADER.release();
			frameCount = 0;
		});
	}
}
