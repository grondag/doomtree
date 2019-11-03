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
package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.particle.DefaultParticleType;

public enum DoomParticles {
	;

	public static final DefaultParticleType ALCHEMY_IDLE = REG.particle("alchemy_idle", true);
	public static final DefaultParticleType ALCHEMY_WAKING = REG.particle("alchemy_waking", true);
	public static final DefaultParticleType BASIN_ACTIVE = REG.particle("basin_active", true);
	public static final DefaultParticleType BRAZIER_ACTIVE = REG.particle("brazier_active", true);
	public static final DefaultParticleType WARDED_FLAME = REG.particle("warded_flame", true);
	public static final DefaultParticleType SUMMONING = REG.particle("summoning", true);
}
