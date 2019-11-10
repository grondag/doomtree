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

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.player.PlayerEntity;

public class WalkerTargetGoal extends TrackTargetGoal {
	static final int RECHECK_TICKS = 17;

	protected LivingEntity targetEntity;
	protected TargetPredicate targetPredicate;

	protected int recheckCounter = 0;

	public WalkerTargetGoal(WalkerEntity walker) {
		super(walker, true, false);
		setMaxTimeWithoutVisibility(200);
		setControls(EnumSet.of(Goal.Control.TARGET));
		targetPredicate = (new TargetPredicate()).setBaseMaxDistance(getFollowRange()).setPredicate(WalkerEntity::isValidTarget);
	}

	@Override
	public boolean canStart() {
		targetEntity = ((WalkerEntity) mob).findBestTarget();
		return targetEntity != null;
	}

	@Override
	public void start() {
		mob.setTarget(targetEntity);
		recheckCounter = RECHECK_TICKS;
		super.start();
	}

	@Override
	public void tick() {
		super.tick();

		if (--recheckCounter <= 0) {
			recheckCounter = RECHECK_TICKS;

			final LivingEntity bestTarget = ((WalkerEntity) mob).findBestTarget();

			if (bestTarget != null && bestTarget != targetEntity
				&& (targetEntity == null
				|| (bestTarget instanceof PlayerEntity && !(targetEntity instanceof PlayerEntity))
				|| Math.sqrt(targetEntity.squaredDistanceTo(mob)) - Math.sqrt(bestTarget.squaredDistanceTo(mob)) > 2)) {

				targetEntity = bestTarget;
				mob.setTarget(bestTarget);
			}
		}
	}
}
