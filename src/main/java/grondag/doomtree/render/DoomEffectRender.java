/*******************************************************************************
 * Copyright (C) 2019 grondag
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package grondag.doomtree.render;

import grondag.doomtree.DoomTreeClient;
import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.registry.DoomEffects;
import grondag.fermion.client.ClientRegistrar;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.experimental.managed.Uniform2f;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;

public enum DoomEffectRender {
	;

	private static final ManagedShaderEffect GREYSCALE_SHADER = ShaderEffectManager.getInstance()
		.manage(DoomTreeClient.REGISTRAR.id("shaders/post/doom.json"));

	private static MinecraftClient MC =MinecraftClient.getInstance();

	private static Uniform2f DOOM_UNIFORM = GREYSCALE_SHADER.findUniform2f("Doom");

	private static void render (final float tickDelta) {
		if (MC.player != null) {
			final StatusEffectInstance doom = MC.player.getStatusEffect(DoomEffects.DOOM_EFFECT);

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
	}
}
