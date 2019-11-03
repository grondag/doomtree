package grondag.doomtree.block.player;

import java.util.Iterator;

import grondag.doomtree.registry.DoomBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.math.Vec3i;

public class WardedBarrelBlockEntity extends LootableContainerBlockEntity {
	protected DefaultedList<ItemStack> inventory;
	protected int viewerCount;

	public WardedBarrelBlockEntity(BlockEntityType<?> beType) {
		super(beType);
		this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
	}

	public WardedBarrelBlockEntity() {
		this(DoomBlocks.WARDED_BARREL_BLOCK_ENTITY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (!this.serializeLootTable(tag)) {
			Inventories.toTag(tag, this.inventory);
		}

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(tag)) {
			Inventories.fromTag(tag, this.inventory);
		}

	}

	@Override
	public int getInvSize() {
		return 27;
	}

	@Override
	public boolean isInvEmpty() {
		Iterator<ItemStack> it = this.inventory.iterator();

		ItemStack stack;

		do {
			if (!it.hasNext()) {
				return true;
			}

			stack = it.next();
		} while(stack.isEmpty());

		return false;
	}

	@Override
	public ItemStack getInvStack(int slot) {
		return (ItemStack)this.inventory.get(slot);
	}

	@Override
	public ItemStack takeInvStack(int slot, int count) {
		return Inventories.splitStack(this.inventory, slot, count);
	}

	@Override
	public ItemStack removeInvStack(int slot) {
		return Inventories.removeStack(this.inventory, slot);
	}

	@Override
	public void setInvStack(int slot, ItemStack stack) {
		this.inventory.set(slot, stack);

		if (stack.getCount() > this.getInvMaxStackAmount()) {
			stack.setCount(this.getInvMaxStackAmount());
		}
	}

	@Override
	public void clear() {
		this.inventory.clear();
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return this.inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> stacks) {
		this.inventory = stacks;
	}

	@Override
	protected Text getContainerName() {
		return new TranslatableText("container.barrel", new Object[0]);
	}

	@Override
	protected Container createContainer(int count, PlayerInventory inventory) {
		return GenericContainer.createGeneric9x3(count, inventory, this);
	}

	@Override
	public void onInvOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.viewerCount < 0) {
				this.viewerCount = 0;
			}

			++this.viewerCount;
			BlockState blockState = this.getCachedState();
			boolean flag = (Boolean)blockState.get(WardedBarrelBlock.OPEN);
			if (!flag) {
				this.playSound(blockState, SoundEvents.BLOCK_BARREL_OPEN);
				this.setOpen(blockState, true);
			}

			this.scheduleUpdate();
		}

	}

	private void scheduleUpdate() {
		this.world.getBlockTickScheduler().schedule(this.getPos(), this.getCachedState().getBlock(), 5);
	}

	public void tick() {
		final int x = this.pos.getX();
		final int y = this.pos.getY();
		final int z = this.pos.getZ();
		this.viewerCount = ChestBlockEntity.countViewers(this.world, this, x, y, z);
		if (this.viewerCount > 0) {
			this.scheduleUpdate();
		} else {
			BlockState blockState = this.getCachedState();
			if (blockState.getBlock() != DoomBlocks.WARDED_BARREL) {
				this.invalidate();
				return;
			}

			boolean flag = (Boolean)blockState.get(WardedBarrelBlock.OPEN);
			if (flag) {
				this.playSound(blockState, SoundEvents.BLOCK_BARREL_CLOSE);
				this.setOpen(blockState, false);
			}
		}
	}

	@Override
	public void onInvClose(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.viewerCount;
		}
	}

	private void setOpen(BlockState blockState, boolean flag) {
		this.world.setBlockState(this.getPos(), blockState.with(WardedBarrelBlock.OPEN, flag), 3);
	}

	private void playSound(BlockState blockState, SoundEvent soundEvent) {
		final Vec3i vec3i = (blockState.get(WardedBarrelBlock.FACING)).getVector();
		final double x = this.pos.getX() + 0.5D + vec3i.getX() / 2.0D;
		final double y = this.pos.getY() + 0.5D + vec3i.getY() / 2.0D;
		final double z = this.pos.getZ() + 0.5D + vec3i.getZ() / 2.0D;
		this.world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
	}
}
