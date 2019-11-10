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

import java.util.EnumSet;
import java.util.Optional;

import grondag.doomtree.packet.WalkerPulseS2C;
import grondag.doomtree.registry.DoomEffects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class WalkerAttackGoal extends Goal {
	public static final double FIRE_HEIGHT_OFFSET = - 0.25;
	protected static final double SPEED_FACTOR = 1.0;
	protected static final int MELEE_COOLDOWN_TICKS = 20;
	protected static final int RANGE_COOLDOWN_TICKS = 100;
	protected static final int RANGE_CHARGE_TICKS = 16;
	protected static final int MAX_RANGE = 32;
	protected static final int MAX_RANGE_SQ = MAX_RANGE * MAX_RANGE;

	protected final MobEntityWithAi walker;
	protected int meleeCooldown;
	protected int rangeCooldown;
	protected int chargeCounter;
	protected boolean isRangedInProgress = false;

	private Path path;
	private int retargetCooldown;
	private double lastTargetX;
	private double lastTargetY;
	private double lastTargetZ;
	private long lastStartAttemptTime;

	public WalkerAttackGoal(MobEntityWithAi walkerIn) {
		walker = walkerIn;
		setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	@Override
	public boolean canStart() {
		final long t = walker.world.getTime();

		if (t - lastStartAttemptTime < 20L) {
			return false;
		} else {
			lastStartAttemptTime = t;
			final LivingEntity target = walker.getTarget();

			if (!WalkerEntity.isValidTarget(target)) {
				return false;
			}

			final double dist = walker.squaredDistanceTo(target.x, target.getBoundingBox().minY, target.z);

			if ((dist <= MAX_RANGE_SQ && walker.getVisibilityCache().canSee(target)) || dist <= getSquaredMeleeAttackRange(target)) {
				// could do a ranged attack or already close enough for melee
				return true;
			}

			// see if can get close enough for melee
			path = walker.getNavigation().findPathTo(target, 0);
			return path != null;

			// TODO; try to move to a spot where can see if can't move
		}
	}

	@Override
	public boolean shouldContinue() {
		final LivingEntity target = walker.getTarget();
		if (!WalkerEntity.isValidTarget(target)) {
			return false;
		}

		if(!walker.getNavigation().isIdle() || walker.isInWalkTargetRange(new BlockPos(target))) {
			// have chance at melee
			return true;
		}

		// only chance is ranged
		return walker.squaredDistanceTo(target.x, target.getBoundingBox().minY, target.z) <= MAX_RANGE_SQ;
	}

	@Override
	public void start() {
		walker.getNavigation().startMovingAlong(path, SPEED_FACTOR);
		walker.setAttacking(true);
		retargetCooldown = 0;
	}

	@Override
	public void stop() {
		//		final LivingEntity target = walker.getTarget();

		//		if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
		walker.setTarget(null);
		//		}

		walker.setAttacking(false);
		walker.getNavigation().stop();
		((WalkerEntity) walker).setPulsing(false);
	}

	@Override
	public void tick() {

		if (isRangedInProgress) {
			tickRanged();
			return;
		}

		--meleeCooldown;
		--rangeCooldown;
		--retargetCooldown;

		final LivingEntity target = walker.getTarget();
		walker.getLookControl().lookAt(target, 30.0F, 30.0F);
		final double sqDistToTarget = walker.squaredDistanceTo(target.x, target.getBoundingBox().minY, target.z);

		if (meleeCooldown <= 0 && doMeleeAttack(target, sqDistToTarget)) {
			meleeCooldown = MELEE_COOLDOWN_TICKS;
			return;
		}

		if (sqDistToTarget <= MAX_RANGE_SQ && rangeCooldown <= 0) {
			isRangedInProgress = true;

			final LookControl looker  = walker.getLookControl();
			// save - will compute observed velocity on next tick
			lastTargetX = looker.getLookX();
			lastTargetY = looker.getLookY();
			lastTargetZ = looker.getLookZ();
			walker.getNavigation().stop();
			chargeCounter = RANGE_CHARGE_TICKS;
			((WalkerEntity) walker).setPulsing(true);
			return;
		}

		if (retargetCooldown <= 0 || target.squaredDistanceTo(lastTargetX, lastTargetY, lastTargetZ) >= 1.0D || walker.getRand().nextFloat() < 0.05F) {
			lastTargetX = target.x;
			lastTargetY = target.getBoundingBox().minY;
			lastTargetZ = target.z;
			retargetCooldown = 4 + walker.getRand().nextInt(7);

			if (sqDistToTarget > 1024.0D) {
				retargetCooldown += 10;
			} else if (sqDistToTarget > 256.0D) {
				retargetCooldown += 5;
			}

			if (!walker.getNavigation().startMovingTo(target, SPEED_FACTOR)) {
				retargetCooldown += 15;
			}
		}
	}

	protected boolean findSplashTarget(World world, BlockPos pos) {
		final double d = findSplashTargetInner(world, pos);

		if(d >= 0) {
			lastTargetX = pos.getX() + 0.5;
			lastTargetY = pos.getY() + d;
			lastTargetZ = pos.getZ() + 0.5;
			return true;
		} else {
			return false;
		}
	}

	protected double findSplashTargetInner(World world, BlockPos pos) {
		final BlockState state = world.getBlockState(pos);

		if (state.isAir() || state.getMaterial().isReplaceable()) {
			return -1;
		}

		if (state.isSimpleFullBlock(world, pos)) {
			return 1;
		}

		final VoxelShape shape = state.getCollisionShape(world, pos);

		if (shape.isEmpty()) return -1;

		final double dx = shape.getMaximum(Direction.Axis.X) - shape.getMinimum(Direction.Axis.X);
		final double dy = shape.getMaximum(Direction.Axis.Y) - shape.getMinimum(Direction.Axis.Y);
		final double dz = shape.getMaximum(Direction.Axis.Z) - shape.getMinimum(Direction.Axis.Z);
		return (dx * dy * dz >= 0.49) ? shape.getMaximum(Direction.Axis.Y) : -1;
	}

	protected boolean findSplashTargetAround(final World world, BlockPos.Mutable pos) {
		final int y = pos.getY();

		if (y < 0 || y > 255) return false;

		if (findSplashTarget(world, pos)) return true;

		final int x = pos.getX();
		final int z = pos.getZ();

		if (findSplashTarget(world, pos.set(x - 1, y, z - 1))) return true;
		if (findSplashTarget(world, pos.set(x - 1, y, z))) return true;
		if (findSplashTarget(world, pos.set(x - 1, y, z + 1))) return true;

		if (findSplashTarget(world, pos.set(x, y, z - 1))) return true;
		if (findSplashTarget(world, pos.set(x, y, z + 1))) return true;

		if (findSplashTarget(world, pos.set(x + 1, y, z - 1))) return true;
		if (findSplashTarget(world, pos.set(x + 1, y, z))) return true;
		if (findSplashTarget(world, pos.set(x + 1, y, z + 1))) return true;

		return false;
	}

	protected void findSplashTarget() {
		final World world = walker.world;

		final BlockPos.Mutable pos = new BlockPos.Mutable();
		final int x = (int) Math.round(lastTargetX);
		final int y = (int) Math.round(lastTargetY);
		final int z = (int) Math.round(lastTargetZ);

		if (findSplashTarget(world, pos.set(x, y, z))) return;
		if (findSplashTarget(world, pos.set(x, y - 1, z))) return;
		if (findSplashTarget(world, pos.set(x, y - 2, z))) return;
		if (findSplashTarget(world, pos.set(x, y - 3, z))) return;
	}

	protected void tickRanged() {
		final int count = chargeCounter--;

		if (count == RANGE_CHARGE_TICKS) {
			walker.getLookControl().lookAt(walker.getTarget(), 30.0F, 30.0F);
			final LookControl looker  = walker.getLookControl();
			final double tx = looker.getLookX();
			final double ty = looker.getLookY();
			final double tz = looker.getLookZ();

			final double dx = tx - lastTargetX;
			final double dy = ty - lastTargetY;
			final double dz = tz - lastTargetZ;

			lastTargetX = tx + dx * (RANGE_CHARGE_TICKS);
			lastTargetY = ty + dy * (RANGE_CHARGE_TICKS);
			lastTargetZ = tz + dz * (RANGE_CHARGE_TICKS);

			// Splash damage is more reliable - look for nearby block
			if (walker.getVisibilityCache().canSee(walker.getTarget())) {
				findSplashTarget();
			}
		}

		walker.getLookControl().lookAt(lastTargetX, lastTargetY, lastTargetZ);

		if(count <= 0) {
			final World world = walker.world;

			final Vec3d from = new Vec3d(walker.x, walker.y + walker.getStandingEyeHeight() + FIRE_HEIGHT_OFFSET, walker.z);
			final double dx = lastTargetX - from.x;
			final double dy = lastTargetY - from.y;
			final double dz = lastTargetZ - from.z;
			final double n = 1.0 / Math.sqrt(dx * dx + dy * dy + dz * dz);
			Vec3d to = new Vec3d(from.x + dx * n * MAX_RANGE, from.y + dy * n * MAX_RANGE, from.z + dz * n * MAX_RANGE);
			final Entity dummy = new ArrowEntity(walker.world, from.x, from.y, from.z);
			final HitResult blockHit = world.rayTrace(new RayTraceContext(from, to, RayTraceContext.ShapeType.COLLIDER, RayTraceContext.FluidHandling.ANY, dummy));
			to = (blockHit == null || blockHit.getType() == Type.MISS) ? to : blockHit.getPos();
			final EntityHitResult entityHit = ProjectileUtil.getEntityCollision(walker.world, dummy, from, to, new Box(from, to).expand(1.0D), WalkerEntity::isValidTargetEntity);

			if (entityHit != null && entityHit.getType() == Type.ENTITY && entityHit.getEntity() != null) {
				final Box box = entityHit.getEntity().getBoundingBox();

				if (box != null) {
					final Optional<Vec3d> h = box.rayTrace(from, to);

					if (h.isPresent()) {
						to = h.get();

						final Entity victim = entityHit.getEntity();
						victim.damage(DoomEffects.PLASMA, 10.0F);
						((WalkerEntity) walker).dealDamage(victim);
					}
				}
			}

			final Explosion.DestructionType destructionType =  world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
			final Explodinator ex = Explodinator.get().prepare(world, walker, to.x, to.y, to.z, 2, true, false, destructionType);
			ex.collectBlocksAndDamageEntities();
			ex.affectWorld(false);

			if (destructionType == Explosion.DestructionType.NONE) {
				ex.clearAffectedBlocks();
			}

			WalkerPulseS2C.send(walker.world, (WalkerEntity) walker, to, ex);

			rangeCooldown = RANGE_COOLDOWN_TICKS;
			walker.getNavigation().startMovingTo(walker.getTarget(), SPEED_FACTOR);
			// playSound(SoundEvents.ENTITY_DROWNED_SHOOT, 1.0F, 1.0F / (getRand().nextFloat() * 0.4F + 0.8F));
			isRangedInProgress = false;
			((WalkerEntity) walker).setPulsing(false);
		}
	}

	protected boolean doMeleeAttack(LivingEntity target, double sqDistToTarget) {
		final double maxSqDist = getSquaredMeleeAttackRange(target);

		if (sqDistToTarget <= maxSqDist) {
			walker.swingHand(Hand.MAIN_HAND);
			walker.tryAttack(target);
			return true;
		} else {
			return false;
		}
	}

	protected double getRangedTargetY(Entity target) {
		return target instanceof LivingEntity ? target.y + target.getStandingEyeHeight() : (target.getBoundingBox().minY + target.getBoundingBox().maxY) / 2.0D;
	}

	protected Vec3d getRangedTargetPos(Entity target) {
		return new Vec3d(target.x, getRangedTargetY(target), target.z);
	}

	protected double getSquaredMeleeAttackRange(LivingEntity target) {
		final float w = walker.getWidth();
		return w * w * 4.0F + target.getWidth();
	}

	protected void doTheThing(HitResult hitResult_1) {
		// TODO: do the thing
		//		if (!mob.world.isClient) {
		//			if (hitResult_1.getType() == HitResult.Type.ENTITY) {
		//				final Entity entity_1 = ((EntityHitResult)hitResult_1).getEntity();
		//				entity_1.damage(DamageSource.explosiveProjectile(this, mob), 6.0F);
		//				mob.dealDamage(mob, entity_1);
		//			}
		//
		//			final boolean boolean_1 = world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
		//			world.createExplosion((Entity)null, x, y, z, (float)explosionPower, boolean_1, boolean_1 ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE);
		//			this.remove();
		//		}
	}
}
