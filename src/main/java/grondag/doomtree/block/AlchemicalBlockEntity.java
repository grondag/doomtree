package grondag.doomtree.block;

import java.util.Random;

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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AlchemicalBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable, RenderAttachmentBlockEntity {
	public static enum Mode {
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

	protected static final String TAG_MODE = "mode";
	protected static final String TAG_UNITS = "units";

	protected Mode mode = Mode.IDLE;

	protected int units = 0;
	
	public AlchemicalBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
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
				this.markDirty();
				this.sync();
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
			if (mode == Mode.WAKING && tickCounter++ >= 20) {
				doWaking();
				tickCounter = 0;
			}
		}
	}

	public static final int XP_COST = 16;

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
				this.units -= UNITS_PER_LEVEL;

				if(this.units == 0) {
					setState(Mode.IDLE, 0);
				} else {
					this.markDirty();
					this.sync();
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

	protected abstract void doActiveParticles(Random rand);

	private void doIdleParticles(Random rand) {
		if (rand.nextInt(8) == 0) {
			final double y = pos.getY() + 0.15f + rand.nextFloat() * 0.8f;
			final double x, z;

			if (rand.nextBoolean()) {
				x = pos.getX() + (rand.nextBoolean() ? -0.05 : 1.05);
				z = pos.getZ() + rand.nextFloat();
			} else {
				x = pos.getX() + rand.nextFloat();
				z = pos.getZ() + (rand.nextBoolean() ? -0.05 : 1.05);

			}

			world.addParticle(DoomParticles.BASIN_IDLE, x, y, z, 0, 0, 0);
			//			
			//			world.addParticle(DoomParticles.BASIN_IDLE, x, y, z, 
			//					(rand.nextFloat() - 0.5f) * 0.2f, 
			//					rand.nextFloat() * 0.05f + 0.1f, 
			//					(rand.nextFloat() - 0.5f) * 0.2f);
		}
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
