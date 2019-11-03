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
package grondag.doomtree.block.player;

import java.util.Random;

import grondag.doomtree.packet.AlchemyCraftS2C;
import grondag.doomtree.packet.XpDrainS2C;
import grondag.doomtree.registry.DoomParticles;
import grondag.fermion.client.RenderRefreshProxy;
import grondag.fermion.varia.XpHelper;
import io.netty.util.internal.ThreadLocalRandom;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlchemicalBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable, RenderAttachmentBlockEntity {
	public enum Mode {
		IDLE,
		WAKING,
		ACTIVE
	}

	protected static Mode[] MODES = Mode.values();

	public static final int UNITS_PER_BUCKET = 1000  * 32 * 27;
	public static final int MAX_BUCKETS = 8;
	public static final int MAX_UNITS = UNITS_PER_BUCKET * MAX_BUCKETS;
	public static final int MAX_VISIBLE_LEVEL = 32;
	public static final int UNITS_PER_LEVEL = MAX_UNITS / MAX_VISIBLE_LEVEL;
	public static final int UNITS_PER_INGOT = UNITS_PER_BUCKET / 9;
	public static final int UNITS_PER_NUGGET = UNITS_PER_INGOT / 9;
	public static final int UNITS_PER_BOTTLE = UNITS_PER_BUCKET / 3;
	public static final int UNITS_PER_FRAGMENT = UNITS_PER_BUCKET / 4;

	public static final int COOKTIME_TICKS_PER_INGOT = 1600;
	public static final int UNITS_PER_COOKTIME = UNITS_PER_INGOT / COOKTIME_TICKS_PER_INGOT;

	public static final int XP_COST = 8;

	protected static final String TAG_MODE = "mode";
	protected static final String TAG_UNITS = "units";

	protected Mode mode = Mode.IDLE;

	protected int units = 0;

	protected final ParticleEffect particle;

	public AlchemicalBlockEntity(BlockEntityType<?> blockEntityType, ParticleEffect particle) {
		super(blockEntityType);
		this.particle = particle;
	}

	public Mode mode() {
		return mode;
	}

	public int units() {
		return units;
	}

	public int visibleLevel() {
		return (units + UNITS_PER_LEVEL - 1) / UNITS_PER_LEVEL;
	}

	public boolean setState(Mode mode, int units) {
		if (world != null && !world.isClient) {
			if(mode != this.mode || units != this.units) {
				this.mode = mode;
				this.units = units;
				markDirty();
				sync();
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setPos(BlockPos pos) {
		super.setPos(pos);
	}

	@Override
	public void validate() {
		super.validate();
	}

	@Override
	public void invalidate() {
		super.invalidate();
	}

	int tickCounter = 0;

	@Override
	public void tick() {
		if (world == null) {
			return;
		}

		if (world.isClient) {
			final Random rand = ThreadLocalRandom.current();
			switch (mode) {
			case ACTIVE:
				doIdleParticles(rand);
				doActiveParticles(rand);
				break;
			case IDLE:
				doIdleParticles(rand);
				break;
			case WAKING:
				doWakingParticles(rand);
				break;
			default:
				break;

			}
		} else {
			if (mode == Mode.WAKING && tickCounter++ >= 10) {
				doWaking();
				tickCounter = 0;
			}
		}
	}

	protected static final TargetPredicate HAS_XP = new TargetPredicate().includeTeammates().setBaseMaxDistance(3).setPredicate(p -> {
		if (!(p instanceof PlayerEntity)) return false;
		final PlayerEntity player = (PlayerEntity) p;
		return player.isCreative() || player.totalExperience >= XP_COST;
	});

	//	int soundCounter = 0;

	private void doWaking() {
		final World world = this.world;
		final BlockPos pos = this.pos;
		final PlayerEntity player = world.getClosestPlayer(HAS_XP, pos.getX(), pos.getY(), pos.getZ());

		if (player != null && player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 16) {
			if (player.isCreative() || player.totalExperience >= XP_COST) {
				if (!player.isCreative()) {
					XpHelper.changeXpNoScore(player, -XP_COST);
				}

				XpDrainS2C.send(world, player.x, player.y, player.z, pos);
				units -= UNITS_PER_LEVEL;

				if(units == 0) {
					setState(Mode.IDLE, 0);
					sendCraftingParticles();

				} else {
					markDirty();
					sync();
				}
			}

		}
	}

	private void doWakingParticles(Random rand) {
		final double x = pos.getX();
		final double y = pos.getY();
		final double z = pos.getZ();
		world.addParticle(ParticleTypes.SMOKE, x + rand.nextDouble(), y + 1.0 + rand.nextDouble() * 0.2, z + rand.nextDouble(), 0.0D, 0.0D, 0.0D);

		//		final int counter = soundCounter++;
		//
		//		if (counter == 0) {
		//				((ClientWorld) world).playSound(pos,
		//						DoomSounds.BOIL,
		//						SoundCategory.BLOCKS,
		//						1f,
		//						1f,
		//						false);
		////			}
		//		} else if (counter >= 90) {
		//			soundCounter = 0;
		//		}

		if (rand.nextInt(4) == 0) {
			world.addImportantParticle(ParticleTypes.BUBBLE_POP,
				x + 0.5 + rand.nextDouble() * 0.25,
				y + units / 32.0,
				z + 0.5 + rand.nextDouble() * 0.25,
				0.0D, 0.04D, 0.0D);
		}
	}

	protected void doActiveParticles(Random rand) {
		if (rand.nextInt(8) == 0) {
			spawnActiveParticles(rand, 1, 1);
		}
	}

	public void spawnActiveParticles(Random rand, int howMany, double speedFactor) {
		final double px = pos.getX() + 0.5;
		final double py = pos.getY() + 1;
		final double pz = pos.getZ() + 0.5;

		for (int i = 0; i < howMany; i++) {
			final double dx = rand.nextGaussian() * 0.2;
			final double dy = Math.abs(rand.nextGaussian()) * 0.2;
			final double dz = rand.nextGaussian() * 0.2;
			final double v = 0.01 + rand.nextDouble() * 0.02 * speedFactor;
			world.addParticle(particle, px + dx, py + dy, pz + dz, dx * v, dy * v, dz * v);
		}
	}

	public void sendCraftingParticles() {
		AlchemyCraftS2C.send(world, pos);
	}

	protected void doIdleParticles(Random rand) {
		if (rand.nextInt(8) == 0) {
			spawnIdleParticle(rand);
		}
	}

	public void spawnIdleParticle(Random rand) {
		final double y = pos.getY() + 0.15f + rand.nextFloat() * 0.8f;
		final double x, z;

		if (rand.nextBoolean()) {
			x = pos.getX() + (rand.nextBoolean() ? -0.05 : 1.05);
			z = pos.getZ() + rand.nextFloat();
		} else {
			x = pos.getX() + rand.nextFloat();
			z = pos.getZ() + (rand.nextBoolean() ? -0.05 : 1.05);
		}

		world.addParticle(DoomParticles.ALCHEMY_IDLE, x, y, z, 0, 0, 0);
	}

	protected CompoundTag writeTag(CompoundTag tag) {
		tag.putInt(TAG_MODE, mode.ordinal());
		tag.putInt(TAG_UNITS, units);
		return tag;
	}

	protected void readTag(CompoundTag tag) {
		mode = MODES[tag.getInt(TAG_MODE)];
		units = tag.getInt(TAG_UNITS);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		readTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return writeTag(super.toTag(tag));
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		final int oldLevel = visibleLevel();
		readTag(tag);
		final BlockPos pos = this.pos;

		if (pos != null && oldLevel != visibleLevel()) {
			RenderRefreshProxy.refresh(pos);
		}
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		if (tag == null) {
			tag = new CompoundTag();
		}
		return writeTag(tag);
	}

	@Override
	public Object getRenderAttachmentData() {
		return this;
	}
}
