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
		inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
	}

	public WardedBarrelBlockEntity() {
		this(DoomBlocks.WARDED_BARREL_BLOCK_ENTITY);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		if (!serializeLootTable(tag)) {
			Inventories.toTag(tag, inventory);
		}

		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		inventory = DefaultedList.ofSize(getInvSize(), ItemStack.EMPTY);
		if (!deserializeLootTable(tag)) {
			Inventories.fromTag(tag, inventory);
		}

	}

	@Override
	public int getInvSize() {
		return 27;
	}

	@Override
	public boolean isInvEmpty() {
		final Iterator<ItemStack> it = inventory.iterator();

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
		return inventory.get(slot);
	}

	@Override
	public ItemStack takeInvStack(int slot, int count) {
		return Inventories.splitStack(inventory, slot, count);
	}

	@Override
	public ItemStack removeInvStack(int slot) {
		return Inventories.removeStack(inventory, slot);
	}

	@Override
	public void setInvStack(int slot, ItemStack stack) {
		inventory.set(slot, stack);

		if (stack.getCount() > getInvMaxStackAmount()) {
			stack.setCount(getInvMaxStackAmount());
		}
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> stacks) {
		inventory = stacks;
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
			if (viewerCount < 0) {
				viewerCount = 0;
			}

			++viewerCount;
			final BlockState blockState = getCachedState();
			final boolean flag = blockState.get(WardedBarrelBlock.OPEN);
			if (!flag) {
				playSound(blockState, SoundEvents.BLOCK_BARREL_OPEN);
				setOpen(blockState, true);
			}

			scheduleUpdate();
		}

	}

	private void scheduleUpdate() {
		world.getBlockTickScheduler().schedule(getPos(), getCachedState().getBlock(), 5);
	}

	public void tick() {
		final int x = pos.getX();
		final int y = pos.getY();
		final int z = pos.getZ();
		viewerCount = ChestBlockEntity.countViewers(world, this, x, y, z);
		if (viewerCount > 0) {
			scheduleUpdate();
		} else {
			final BlockState blockState = getCachedState();
			if (blockState.getBlock() != DoomBlocks.WARDED_BARREL) {
				invalidate();
				return;
			}

			final boolean flag = blockState.get(WardedBarrelBlock.OPEN);
			if (flag) {
				playSound(blockState, SoundEvents.BLOCK_BARREL_CLOSE);
				setOpen(blockState, false);
			}
		}
	}

	@Override
	public void onInvClose(PlayerEntity player) {
		if (!player.isSpectator()) {
			--viewerCount;
		}
	}

	private void setOpen(BlockState blockState, boolean flag) {
		world.setBlockState(getPos(), blockState.with(WardedBarrelBlock.OPEN, flag), 3);
	}

	private void playSound(BlockState blockState, SoundEvent soundEvent) {
		final Vec3i vec3i = (blockState.get(WardedBarrelBlock.FACING)).getVector();
		final double x = pos.getX() + 0.5D + vec3i.getX() / 2.0D;
		final double y = pos.getY() + 0.5D + vec3i.getY() / 2.0D;
		final double z = pos.getZ() + 0.5D + vec3i.getZ() / 2.0D;
		world.playSound(null, x, y, z, soundEvent, SoundCategory.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
	}
}
