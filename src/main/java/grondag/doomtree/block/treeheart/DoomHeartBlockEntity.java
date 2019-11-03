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
package grondag.doomtree.block.treeheart;

import java.util.Comparator;

import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class DoomHeartBlockEntity extends BlockEntity implements Tickable {
	private static final ChunkTicketType<ChunkPos> DOOM_TREE_TICKET = ChunkTicketType.create("doom_tree", Comparator.comparingLong(ChunkPos::toLong));

	int tickCounter = 0;
	long power = 1000;
	boolean leafTick = false;
	boolean itMe = false;

	LogTracker logs = null;
	TrunkBuilder builds = null;
	BranchBuilder branches = null;
	Troll troll = null;

	Job job = null;

	final BlockPos.Mutable mPos = new BlockPos.Mutable();

	public DoomHeartBlockEntity(BlockEntityType<?> entityType) {
		super(entityType);
	}

	public DoomHeartBlockEntity() {
		this(DoomBlocks.DOOM_HEART);
	}

	@Override
	public void setPos(BlockPos pos) {
		super.setPos(pos);

		// NBT deserialization happens before this
		if (logs == null) logs = new LogTracker(pos);
		if (builds == null) builds = new TrunkBuilder(pos);
		if (branches == null) branches = new BranchBuilder(pos);
		if (troll == null) troll = new Troll(pos);
	}

	@Override
	public void validate() {
		super.validate();

		if (world !=null  && !world.isClient) {
			DoomTreeTracker.track(world, pos);
			forceChunks((ServerWorld) world, new ChunkPos(pos), true);
		}
	}

	static void forceChunks(ServerWorld world, ChunkPos chunkPos, boolean enable) {
		final ServerChunkManager scm = world.method_14178();

		if (enable) {
			scm.addTicket(DOOM_TREE_TICKET, chunkPos, 4, chunkPos);
		} else {
			scm.removeTicket(DOOM_TREE_TICKET, chunkPos, 4, chunkPos);
		}
	}

	@Override
	public void invalidate() {
		if (world !=null  && !world.isClient) {
			DoomTreeTracker.untrack(world, pos);
			forceChunks((ServerWorld) world, new ChunkPos(pos), false);
		}

		super.invalidate();
	}

	@Override
	public void tick() {
		if (world == null || world.isClient) {
			return;
		}

		++power;
		--tickCounter;

		itMe = true;

		if (job == null) {
			idle();
		} else {
			job = job.apply(this);
		}

		itMe = false;
	}

	void idle() {
		if (tickCounter <= 0 && power >= 2000) {
			final boolean noBuilds = builds.isEmpty() && branches.isEmpty();
			if((leafTick || noBuilds) && logs.hasBranches()) {
				LeafGrower.growLeaves(this);
				leafTick = false;
				return;
			} else if (!noBuilds) {
				if (builds.buildDistanceSquared() > branches.buildDistanceSquared()) {
					branches.build(this);
				} else {
					builds.build(this);
				}
				leafTick = true;
				return;
			}
		}

		troll.troll(this);
	}

	void resetTickCounter() {
		tickCounter = 10;
	}

	public void setTemplate(long[] blocks) {
		final LogTracker logs = this.logs;
		for (final long p : blocks) {
			logs.add(p);
		}

		job = new BuildPopulator(this);

		markDirty();
	}

	static final String LOG_KEY = "logs";
	static final String BRANCH_KEY = "branches";
	static final String POWER_KEY = "power";
	static final String TROLL_KEY = "troll";

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);

		power = tag.getLong(POWER_KEY);

		if (logs == null) {
			logs = new LogTracker(getPos());
		}

		if  (tag.containsKey(LOG_KEY)) {
			logs.fromArray(tag.getIntArray(LOG_KEY));
		}

		if (builds == null) {
			builds = new TrunkBuilder(getPos());
		}

		if (branches == null) {
			branches = new BranchBuilder(getPos());
		}

		if (tag.containsKey(BRANCH_KEY)) {
			branches.fromArray(tag.getIntArray(BRANCH_KEY));
		}

		if (troll == null) {
			troll = new Troll(getPos());
		}

		if (tag.containsKey(TROLL_KEY)) {
			troll.fromArray(tag.getIntArray(TROLL_KEY));
		}

		job = new BuildPopulator(this);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag = super.toTag(tag);
		tag.putLong(POWER_KEY, power);

		if (logs != null) {
			tag.putIntArray(LOG_KEY, logs.toIntArray());
		}

		if (branches != null) {
			tag.putIntArray(BRANCH_KEY, branches.toIntArray());
		}

		if (troll != null) {
			tag.putIntArray(TROLL_KEY, troll.toIntArray());
		}

		return tag;
	}

	public void reportBreak(BlockPos pos, boolean isLog) {
		if (itMe) return;

		if (isLog && logs.contains(pos)) {
			builds.enqueue(pos.asLong());
		} else {
			troll.enqueue(pos.asLong());
		}
	}
}
