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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class WalkerEntity extends HostileEntity {
	public WalkerEntity(EntityType<? extends WalkerEntity> type, World world) {
		super(type, world);
		stepHeight = 1.0F;
	}

	@Override
	protected void initAttributes() {
		super.initAttributes();
		getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
		getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(2.5D);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		goalSelector.add(8, new LookAroundGoal(this));
		initCustomGoals();
	}

	protected void initCustomGoals() {
		goalSelector.add(2, new AttackGoal(this, 1.0D, false));
		targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 2.55F;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public int getLightmapCoordinates() {
		return super.getLightmapCoordinates();
		//		return 15728880;
	}

	@Override
	public float getBrightnessAtEyes() {
		return super.getBrightnessAtEyes();
		//		return 1.0F;
	}

	public static class AttackGoal extends MeleeAttackGoal {
		private final WalkerEntity actor;
		private int counter;

		public AttackGoal(WalkerEntity actor, double speed, boolean wanderMaybe) {
			super(actor, speed, wanderMaybe);
			this.actor = actor;
		}

		@Override
		public void start() {
			super.start();
			counter = 0;
		}

		@Override
		public void stop() {
			super.stop();
			actor.setAttacking(false);
		}

		@Override
		public void tick() {
			super.tick();
			++counter;
			if (counter >= 5 && ticksUntilAttack < 10) {
				actor.setAttacking(true);
			} else {
				actor.setAttacking(false);
			}
		}
	}
}