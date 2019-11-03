package grondag.doomtree.block.treeheart;

import java.util.ArrayList;

import grondag.doomtree.entity.DoomEffect;
import grondag.doomtree.packet.DoomS2C;
import grondag.doomtree.registry.DoomBlockStates;
import grondag.doomtree.registry.DoomBlocks;
import grondag.doomtree.registry.DoomEffects;
import grondag.doomtree.registry.DoomTags;
import grondag.fermion.position.PackedBlockPos;
import grondag.fermion.position.PackedBlockPosList;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntHeapPriorityQueue;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

@SuppressWarnings("serial")
class Troll extends IntHeapPriorityQueue {
	private static final int MAX_INDEX;
	private static final int[] OFFSETS;

	static {
		final IntArrayList offsets = new IntArrayList();

		for (int x = -TreeUtils.RADIUS; x <= TreeUtils.RADIUS; x++) {
			for (int z = -TreeUtils.RADIUS; z <= TreeUtils.RADIUS; z++) {
				final int sqd = x * x + z * z;

				if(sqd > 0 && sqd <= TreeUtils.RADIUS * TreeUtils.RADIUS) {
					offsets.add(RelativePos.relativePos(x, 0, z));
				}
			}
		}

		offsets.sort((i0, i1) -> Integer.compare(RelativePos.squaredDistance(i0), RelativePos.squaredDistance(i1)));

		OFFSETS = offsets.toIntArray();
		MAX_INDEX = OFFSETS.length;
	}

	private final int originX;
	private final int originY;
	private final int originZ;

	private final int maxY;

	private final IntOpenHashSet set = new IntOpenHashSet();
	private final ArrayList<Entity> targets =  new ArrayList<>();
	private final PackedBlockPosList reports  = new PackedBlockPosList();

	int y;
	int index;

	Troll(BlockPos origin) {
		super((i0, i1) -> Integer.compare(RelativePos.squaredDistance(i0), RelativePos.squaredDistance(i1)));

		originX = origin.getX();
		originY = origin.getY();
		originZ = origin.getZ();

		maxY = ((originY + TreeUtils.MAX_TRUNK_HEIGHT + 2) & ~15) + 16;
	}

	void enqueue(long pos) {
		enqueue(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	void enqueue(BlockPos pos) {
		enqueue(RelativePos.relativePos(originX, originY, originZ, pos));
	}

	@Override
	public void enqueue(int relativePos) {
		if (set.add(relativePos)) {
			super.enqueue(relativePos);
		}
	}

	@Override
	public int dequeueInt() {
		final int result = super.dequeueInt();
		set.remove(result);
		return result;
	}

	void troll(DoomHeartBlockEntity heart) {
		reports.clear();
		targets.clear();

		if (isEmpty()) {
			trollNext(heart);
		} else {
			trollQueue(heart);
		}

		if (!reports.isEmpty()) {
			DoomS2C.send(heart.getWorld(), reports);
		}
	}

	private void trollNext(DoomHeartBlockEntity heart) {
		if (y >= maxY) {
			y = 0;
			index++;
			heart.markDirty();
		}

		if(index >= MAX_INDEX) {
			index = 0;
		}

		final int offset = OFFSETS[index];
		final int x = RelativePos.rx(offset);
		final int z = RelativePos.rz(offset);

		final BlockPos.Mutable mPos = heart.mPos;
		final World world = heart.getWorld();

		for (int i = 0; i < 16; i++) {
			trollBlock(world, mPos, heart, RelativePos.relativePos(x, -originY + y + i, z));
		}

		final int px = originX + x;
		final int pz = originZ + z;
		WorldChunk chunk  = world.getWorldChunk(mPos.set(px, y, pz));

		if (chunk != null) {
			chunk.appendEntities((Entity)null, new Box(px, y, pz, px + 1, y + 16, pz + 1), targets, e -> DoomEffect.canDoom(e));
		}

		doDamage(world);

		y += 16;
	}



	private void trollQueue(DoomHeartBlockEntity heart) {
		final BlockPos.Mutable mPos = heart.mPos;
		final World world = heart.getWorld();

		boolean didUpdate = false;

		for (int i = 0; i < 32; i++) {
			if(isEmpty()) {
				return;
			}

			didUpdate |= trollBlock(world, mPos, heart, dequeueInt());
		}

		if (didUpdate) {
			heart.resetTickCounter();
		}
		heart.markDirty();
	}

	private boolean trollBlock(World world, BlockPos.Mutable mPos, DoomHeartBlockEntity heart, int pos) {
		RelativePos.set(mPos, originX, originY, originZ, pos);

		if (!World.isValid(mPos) || !world.isBlockLoaded(mPos)) {
			return false;
		}

		final BlockState currentState = world.getBlockState(mPos);

		final FluidState fluidState = currentState.getFluidState();
		if (!(fluidState.isEmpty() || fluidState.isStill())) {
			return false;
		}

		final BlockState trollState = Troll.trollState(world, currentState, mPos);

		if (trollState == null || trollState == currentState) return false;

		final Block newBlock = trollState.getBlock();

		if (!currentState.isAir() && newBlock != DoomBlocks.ICHOR_BLOCK) {
			heart.power += 2;
		}

		if (newBlock == DoomBlocks.MIASMA_BLOCK) {
			placeMiasma(mPos, world);
		} else {
			world.setBlockState(mPos, trollState, 19);
			reports.add(PackedBlockPos.pack(mPos, newBlock == DoomBlocks.ICHOR_BLOCK ? DoomS2C.ICHOR : DoomS2C.DOOM));
		}

		return true;
	}

	void placeMiasma(BlockPos pos, World world) {
		BlockState state = (HashCommon.mix(pos.asLong()) & 31) == 0 ? DoomBlockStates.GLEAM_STATE : DoomBlockStates.MIASMA_STATE;
		world.setBlockState(pos, state);

		reports.add(PackedBlockPos.pack(pos, DoomS2C.MIASMA));
	}

	void doDamage(World world) {
		if (targets.isEmpty()) return;

		BooleanRule lootRule = world.getGameRules().get(GameRules.DO_MOB_LOOT);
		final boolean loot = lootRule.get();

		if (loot) lootRule.set(false, null);

		for (Entity e : targets) {
			//TODO: collect energy
			harvestEntity((LivingEntity) e);
		}

		if (loot) lootRule.set(true, null);
	}

	/** 
	 * Assumes valid entity, takes percentage of health. 
	 * Does not prevent loot drops.
	 * Returns hearts harvested. 
	 */
	public static float harvestEntity(LivingEntity e) {
		final float damage = e.getHealthMaximum() * (1 - DoomEffect.doomResistance(e));

		if (damage > 0.1f) {
			e.damage(DoomEffects.DOOM, damage);
		}

		return damage;
	}

	int[] toIntArray() {
		int[] result = new int[size + 2];
		result[0] = y;
		result[1] = index;
		System.arraycopy(heap, 0, result, 2, size);
		return result;
	}

	void fromArray(int[] data) {
		clear();

		y = data[0];
		index = data[1];
		final int newSize = data.length;

		if (newSize > 2) {
			for (int i = 2; i < newSize; i++) {
				enqueue(data[i]);
			}
		}
	}

	/**
	 * Null means can't troll - tendril blocked
	 */
	static BlockState trollState(World world, BlockState fromState, BlockPos pos) {
		final Block block = fromState.getBlock();

		if (block == Blocks.BEDROCK || block.matches(DoomTags.IGNORED_BLOCKS)) {
			return fromState;
		}

		final Material material = fromState.getMaterial();

		if (material.isLiquid() && block instanceof FluidBlock) {
			final FluidState fluidState = fromState.getFluidState();
			if (fluidState != null) {
				if (fluidState.isStill()) {
					if (material == Material.LAVA) {
						return DoomBlockStates.DOOMED_STONE_STATE;
					} else {
						return DoomBlocks.ICHOR_BLOCK.getDefaultState();
					}
				}
			} else {
				return fromState;
			}
		}

		if (TreeUtils.canReplace(fromState)) {
			if (block.matches(BlockTags.LOGS) && fromState.contains(PillarBlock.AXIS)) {
				return DoomBlockStates.DOOMED_LOG_STATE.with(PillarBlock.AXIS, fromState.get(PillarBlock.AXIS));
			} else if (block.matches(BlockTags.DIRT_LIKE)) {
				return DoomBlockStates.DOOMED_EARTH_STATE;
			} else if (block.isFullOpaque(fromState, world, pos)) {
				if (material == Material.STONE) {
					return DoomBlockStates.DOOMED_STONE_STATE;
				} else {
					return DoomBlockStates.DOOMED_DUST_STATE;
				}
			}
			return DoomBlockStates.MIASMA_STATE;
		}

		return fromState;
	}
}
