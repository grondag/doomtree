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
package grondag.doomtree.entity;

import java.util.List;

import grondag.doomtree.registry.DoomParticles;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.TypeFilterableList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

public class WalkerEntity extends HostileEntity {
	public static final int TARGET_RANGE = 32;

	private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(WalkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private boolean targetingUnderwater;
	protected final SwimNavigation waterNavigation;
	protected final MobNavigation landNavigation;
	protected int pulseCount = 0;

	public WalkerEntity(EntityType<? extends WalkerEntity> type, World world) {
		super(type, world);
		stepHeight = 1.0F;
		waterNavigation = new SwimNavigation(this, world);
		landNavigation = new MobNavigation(this, world);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		dataTracker.startTracking(CHARGING, false);
	}

	public boolean isPulsing() {
		return dataTracker.get(CHARGING);
	}

	public int pulseCount() {
		return pulseCount;
	}

	protected void setPulsing(boolean val) {
		dataTracker.set(CHARGING, val);
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> trackedData) {
		if (CHARGING.equals(trackedData) && world.isClient) {
			// TODO: play charging sound
			pulseCount = isPulsing() ? 1 : 0;
		}

		super.onTrackedDataSet(trackedData);
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(60.0D);
		getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
		getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(7.0D);
		getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(3.0D);
		getAttributeInstance(EntityAttributes.FOLLOW_RANGE).setBaseValue(64.0D);
		getAttributeInstance(EntityAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.add(8, new LookAroundGoal(this));
		goalSelector.add(2, new WalkerAttackGoal(this));
		targetSelector.add(2, new WalkerTargetGoal(this));
	}

	public GoalSelector targetSelector() {
		return targetSelector;
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 2.55F;
	}

	@Override
	public boolean cannotDespawn() {
		return true;
	}

	public static boolean isValidTarget(LivingEntity target) {
		if(target == null || !target.isAlive()) return false;

		if(target instanceof PlayerEntity) {
			return !target.isSpectator() && !((PlayerEntity) target).isCreative();
		}

		return target instanceof AbstractTraderEntity || target instanceof IronGolemEntity
			|| target instanceof WolfEntity;
	}

	public static boolean isValidTargetEntity(Entity target) {
		return target instanceof LivingEntity && isValidTarget((LivingEntity) target);
	}

	protected LivingEntity findBestTarget() {
		final List<? extends PlayerEntity> players = world.getPlayers();

		LivingEntity best = null;
		double bestDist = Integer.MAX_VALUE;

		if (!players.isEmpty()) {
			final int limit = players.size();

			for (int i = 0; i < limit; i++) {
				final LivingEntity e = players.get(i);

				if (isValidTarget(e)) {
					final double d = e.squaredDistanceTo(this);

					if (d < bestDist) {
						best = e;
						bestDist = d;
					}
				}
			}
		}

		if (best == null) {
			final int minX = MathHelper.floor((x - TARGET_RANGE - 2.0D) / 16.0D);
			final int maxX = MathHelper.floor((x + TARGET_RANGE + 2.0D) / 16.0D);
			final int minZ = MathHelper.floor((z - TARGET_RANGE - 2.0D) / 16.0D);
			final int maxZ = MathHelper.floor((z + TARGET_RANGE + 2.0D) / 16.0D);
			final int minY = Math.max(0, MathHelper.floor((y - 6.0D) / 16.0D));
			final int maxY = Math.min(15, MathHelper.floor((y + 6.0D) / 16.0D));

			for(int x = minX; x <= maxX; ++x) {
				for(int z = minZ; z <= maxZ; ++z) {
					final WorldChunk chunk = world.getChunkManager().getWorldChunk(x, z, false);

					if (chunk == null) continue;

					for(int y = minY; y <= maxY; ++y) {
						final TypeFilterableList<Entity> list = chunk.getEntitySectionArray()[y];

						if(list == null) continue;

						for (final Entity e : chunk.getEntitySectionArray()[y]) {
							if (e instanceof LivingEntity && isValidTarget((LivingEntity) e)) {
								final double d = e.squaredDistanceTo(this);

								if (d < bestDist) {
									best = (LivingEntity) e;
									bestDist = d;
								}
							}
						}
					}
				}
			}
		}

		return best;
	}

	@Override
	public void tick() {
		if(world.isClient && isPulsing()) {
			pulseCount++;
			doChargeParticles();
		}
		super.tick();
	}

	private void doChargeParticles() {
		final double yLook = (y + getStandingEyeHeight() + WalkerAttackGoal.FIRE_HEIGHT_OFFSET);

		final Vec3d look = getRotationVec(1.0F).normalize();

		final double px = x + look.x * 0.5;
		final double py = yLook + look.y * 0.5;
		final double pz = z + look.z * 0.5;

		world.addParticle(DoomParticles.WALKER_PULSE, px + random.nextGaussian() * 0.1, py + random.nextGaussian() * 0.1, pz + random.nextGaussian() * 0.1, 0, 0, 0);
	}

	@Override
	public void updateSwimming() {
		if (!world.isClient) {
			if (canMoveVoluntarily() && isInsideWater() && isTargetingUnderwater()) {
				navigation = waterNavigation;
				setSwimming(true);
			} else {
				navigation = landNavigation;
				setSwimming(false);
			}
		}
	}

	private boolean isTargetingUnderwater() {
		if (targetingUnderwater) {
			return true;
		} else {
			final LivingEntity livingEntity_1 = getTarget();
			return livingEntity_1 != null && livingEntity_1.isInsideWater();
		}
	}

	public void dealDamage(Entity victim) {
		super.dealDamage(this, victim);
	}
}
