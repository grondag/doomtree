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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;

public class Explodinator extends Explosion {

	public Explodinator() {
		super(null, null, 0, 0, 0, 0, false, null);
	}

	private static final ThreadLocal<Explodinator> POOL = ThreadLocal.withInitial(Explodinator::new);

	public static Explodinator get() {
		return POOL.get();
	}

	public void expodinate() {
		//		final Explosion explosion_1 = new Explosion(this, entity_1, double_1, double_2, double_3, float_1, boolean_1, explosion$DestructionType_1);
		//
		//		explosion_1.collectBlocksAndDamageEntities();
		//		explosion_1.affectWorld(true);
	}

	protected boolean blockFire;
	protected boolean playerFire;
	protected final List<BlockPos> affectedBlocks = new ArrayList<>();
	protected final Map<PlayerEntity, Vec3d> affectedPlayers = new HashMap<>();
	protected final BlockPos.Mutable mPos = new BlockPos.Mutable();
	protected final LongOpenHashSet blockSet = new LongOpenHashSet();

	protected Explosion.DestructionType blockDestructionType;
	protected final Random random = new Random();
	protected World world;
	protected double x;
	protected double y;
	protected double z;
	protected Entity entity;
	protected float power;

	@Nullable protected ExplosionFX fx = DEFAULT_FX;
	@Nullable protected DefaultParticleType smokeParticle = ParticleTypes.SMOKE;
	@Nullable protected DefaultParticleType poofParticle = ParticleTypes.POOF;

	public void toBuffer(PacketByteBuf buf) {
		buf.writeFloat(power);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeEnumConstant(blockDestructionType);
		buf.writeBoolean(blockFire);
		buf.writeBoolean(playerFire);
		buf.writeVarInt(affectedBlocks.size());

		final int ox = MathHelper.floor(x);
		final int oy = MathHelper.floor(y);
		final int oz = MathHelper.floor(z);
		final Iterator<BlockPos> it = affectedBlocks.iterator();

		while(it.hasNext()) {
			final BlockPos pos = it.next();
			final int rx = pos.getX() - ox;
			final int ry = pos.getY() - oy;
			final int rz = pos.getZ() - oz;
			buf.writeByte(rx);
			buf.writeByte(ry);
			buf.writeByte(rz);
		}
	}

	public Explodinator fromBuffer(PacketByteBuf buf) {
		power = buf.readFloat();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		blockDestructionType = buf.readEnumConstant(Explosion.DestructionType.class);
		blockFire = buf.readBoolean();
		playerFire = buf.readBoolean();

		clearAffectedBlocks();

		final int ox = MathHelper.floor(x);
		final int oy = MathHelper.floor(y);
		final int oz = MathHelper.floor(z);
		final int limit = buf.readVarInt();

		for(int i = 0; i < limit; i++) {
			final int rx = buf.readByte();
			final int ry = buf.readByte();
			final int rz = buf.readByte();
			affectedBlocks.add(BlockPos.PooledMutable.get().set(ox + rx, oy + ry, oz + rz));
		}

		return this;
	}

	public Explodinator prepare(World world,
		@Nullable Entity entity,
		double x,
		double y,
		double z,
		float power,
		boolean playerFire,
		boolean blockFire,
		Explosion.DestructionType destructionType)
	{
		this.world = world;
		this.entity = entity;
		this.power = power;
		this.x = x;
		this.y = y;
		this.z = z;
		this.blockFire = blockFire;
		this.playerFire = playerFire;
		blockDestructionType = destructionType;
		setDamageSource(DamageSource.explosion(this));
		clearAffectedBlocks();
		affectedPlayers.clear();
		fx = DEFAULT_FX;
		smokeParticle = ParticleTypes.SMOKE;
		poofParticle = ParticleTypes.POOF;
		return this;
	}

	public Explodinator setParticles(
		@Nullable ExplosionFX fx,
		@Nullable DefaultParticleType smokeParticle,
		@Nullable DefaultParticleType poofParticle) {

		this.fx =  fx;
		this.smokeParticle =  smokeParticle;
		this.poofParticle =  poofParticle;

		return this;
	}

	@Override
	public void collectBlocksAndDamageEntities() {
		final BlockPos.Mutable mPos = this.mPos;
		final LongOpenHashSet blocks = blockSet;
		blocks.clear();

		for(int xPos = 0; xPos < 16; ++xPos) {
			for(int yPos = 0; yPos < 16; ++yPos) {
				for(int zPos = 0; zPos < 16; ++zPos) {
					if (xPos == 0 || xPos == 15 || yPos == 0 || yPos == 15 || zPos == 0 || zPos == 15) {
						float sx = xPos / 15.0F * 2.0F - 1.0F;
						float sy = yPos / 15.0F * 2.0F - 1.0F;
						float sz = zPos / 15.0F * 2.0F - 1.0F;
						final float dist = (float) Math.sqrt(sx * sx + sy * sy + sz * sz);
						sx /= dist;
						sy /= dist;
						sz /= dist;
						float px = (float) x;
						float py = (float) y;
						float pz = (float) z;

						for(float p = power * (0.7F + world.random.nextFloat() * 0.6F); p > 0.0F; p -= 0.22500001F) {
							mPos.set(px, py, pz);
							final BlockState blockState = world.getBlockState(mPos);
							final FluidState fluidState = world.getFluidState(mPos);

							if (!blockState.isAir() || !fluidState.isEmpty()) {
								float res = Math.max(blockState.getBlock().getBlastResistance(), fluidState.getBlastResistance());

								if (entity != null) {
									res = entity.getEffectiveExplosionResistance(this, world, mPos, blockState, fluidState, res);
								}

								p -= (res + 0.3F) * 0.3F;
							}

							if (p > 0.0F && (entity == null || entity.canExplosionDestroyBlock(this, world, mPos, blockState, p))) {
								blocks.add(mPos.asLong());
							}

							px += sx * 0.30000001192092896F;
							py += sy * 0.30000001192092896F;
							pz += sz * 0.30000001192092896F;
						}
					}
				}
			}
		}

		for(final long l : blocks) {
			affectedBlocks.add(BlockPos.PooledMutable.get().set(l));
		}

		final float radius = power * 2.0F;
		final int xMin = MathHelper.floor(x - radius - 1);
		final int xMax = MathHelper.floor(x + radius + 1);
		final int yMin = MathHelper.floor(y - radius - 1);
		final int yMax = MathHelper.floor(y + radius + 1);
		final int zMin = MathHelper.floor(z - radius - 1);
		final int zMax = MathHelper.floor(z + radius + 1);
		final List<Entity> victims = world.getEntities(entity, new Box(xMin, yMin, zMin, xMax, yMax, zMax));
		final Vec3d center = new Vec3d(x, y, z);

		for(int i = 0; i < victims.size(); ++i) {
			final Entity victim = victims.get(i);

			if (!victim.isImmuneToExplosion()) {
				final double distSq = MathHelper.sqrt(victim.squaredDistanceTo(new Vec3d(x, y, z))) / radius;

				if (distSq <= 1.0D) {
					double dx = victim.x - x;
					double dy = victim.y + victim.getStandingEyeHeight() - y;
					double dz = victim.z - z;
					final double dist = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);

					if (dist != 0.0D) {
						dx /= dist;
						dy /= dist;
						dz /= dist;
						final float exposure = getExposure(center, victim);

						if (exposure > 0.05f) {
							final float dmg = (float) ((1 - distSq) * exposure);

							if (dmg > 0.05f) {
								victim.damage(getDamageSource(), ((int)((dmg * dmg + dmg) / 2f * 7f * radius + 1)));
								double knockback = dmg;

								if (victim instanceof LivingEntity) {
									knockback = ProtectionEnchantment.transformExplosionKnockback((LivingEntity)victim, dmg);

									if (playerFire && !victim.isFireImmune()) {
										victim.setOnFireFor(Math.round(power * 4 * exposure));
									}
								}

								victim.setVelocity(victim.getVelocity().add(dx * knockback, dy * knockback, dz * knockback));

								if (victim instanceof PlayerEntity) {
									final PlayerEntity player = (PlayerEntity)victim;

									if (!player.isSpectator() && (!player.isCreative() || !player.abilities.flying)) {
										affectedPlayers.put(player, new Vec3d(dx * dmg, dy * dmg, dz * dmg));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void affectWorld(boolean doParticles) {
		world.playSound((PlayerEntity)null, x, y, z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
		final boolean grief = blockDestructionType != Explosion.DestructionType.NONE;

		if (grief && doParticles && fx != null) {
			fx.apply(world, x, y, z, power, random);
		}

		if (grief) {
			final Iterator<BlockPos> it = affectedBlocks.iterator();

			while(it.hasNext()) {
				final BlockPos pos = it.next();
				final BlockState blockState = world.getBlockState(pos);
				final Block block = blockState.getBlock();

				if (doParticles) {
					final double px = pos.getX() + random.nextFloat();
					final double py = pos.getY() + random.nextFloat();
					final double pz = pos.getZ() + random.nextFloat();
					double vx = px - x;
					double vy = py - y;
					double vz = pz - z;
					final double dist = MathHelper.sqrt(vx * vx + vy * vy + vz * vz);
					vx /= dist;
					vy /= dist;
					vz /= dist;
					double speed = 0.5D / (dist / power + 0.1D);
					speed *= random.nextFloat() * random.nextFloat() + 0.3F;
					vx *= speed;
					vy *= speed;
					vz *= speed;

					if (poofParticle != null) {
						world.addParticle(poofParticle, (px + x) / 2.0D, (py + y) / 2.0D, (pz + z) / 2.0D, vx, vy, vz);
					}

					if (smokeParticle != null) {
						world.addParticle(smokeParticle, px, py, pz, vx, vy, vz);
					}
				}

				if (!blockState.isAir()) {
					if (block.shouldDropItemsOnExplosion(this) && world instanceof ServerWorld) {
						final BlockEntity blockEntity_1 = block.hasBlockEntity() ? world.getBlockEntity(pos) : null;
						final LootContext.Builder lootContext = (new LootContext.Builder((ServerWorld)world)).setRandom(world.random).put(LootContextParameters.POSITION, pos).put(LootContextParameters.TOOL, ItemStack.EMPTY).putNullable(LootContextParameters.BLOCK_ENTITY, blockEntity_1);

						if (blockDestructionType == Explosion.DestructionType.DESTROY) {
							lootContext.put(LootContextParameters.EXPLOSION_RADIUS, power);
						}

						Block.dropStacks(blockState, lootContext);
					}

					world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
					block.onDestroyedByExplosion(world, pos, this);
				}
			}
		}

		if (blockFire) {
			final Iterator<BlockPos> it = affectedBlocks.iterator();

			while(it.hasNext()) {
				final BlockPos pos = it.next();

				if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isFullOpaque(world, pos.down()) && random.nextInt(3) == 0) {
					world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}

	@Override
	public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
		return affectedPlayers;
	}

	@Override
	@Nullable
	public LivingEntity getCausingEntity() {
		if (entity == null) {
			return null;
		} else if (entity instanceof TntEntity) {
			return ((TntEntity)entity).getCausingEntity();
		} else {
			return entity instanceof LivingEntity ? (LivingEntity)entity : null;
		}
	}

	@Override
	public void clearAffectedBlocks() {
		for(final BlockPos p : affectedBlocks) {
			((PooledMutable) p).close();
		}

		affectedBlocks.clear();
	}

	@Override
	public List<BlockPos> getAffectedBlocks() {
		return affectedBlocks;
	}

	public Explodinator setWorld(World world) {
		this.world = world;
		return this;
	}

	public interface ExplosionFX {
		void apply(World world, double x, double y, double z, float power, Random rand);
	}

	public static final ExplosionFX DEFAULT_FX = (w, x, y, z, p, rand)  -> {
		if (p >= 2.0F) {
			w.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 1.0D, 0.0D, 0.0D);
		} else {
			w.addParticle(ParticleTypes.EXPLOSION, x, y, z, 1.0D, 0.0D, 0.0D);
		}
	};
}
