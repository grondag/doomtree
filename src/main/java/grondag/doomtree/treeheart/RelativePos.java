package grondag.doomtree.treeheart;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public enum RelativePos {
	;

	private static final int MIN = -255;
	private static final int COUNT = Math.abs(MIN) * 2 + 1;
	private static final int MASK = MathHelper.smallestEncompassingPowerOfTwo(COUNT) - 1;
	private static final int SHIFT = Integer.bitCount(MASK);
	
	public static int relativePos(final BlockPos originPos, final BlockPos blockPos) {
		return relativePos(
				originPos.getX(), originPos.getY(), originPos.getZ(),
				blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static int relativePos(final BlockPos originPos, final long blockPos) {
		return relativePos(
				originPos.getX(), originPos.getY(), originPos.getZ(),
				BlockPos.unpackLongX(blockPos), BlockPos.unpackLongY(blockPos), BlockPos.unpackLongZ(blockPos));
	}

	public static int relativePos(final long originPos, final long blockPos) {
		return relativePos(
				BlockPos.unpackLongX(originPos), BlockPos.unpackLongY(originPos), BlockPos.unpackLongZ(originPos),
				BlockPos.unpackLongX(blockPos), BlockPos.unpackLongY(blockPos), BlockPos.unpackLongZ(blockPos));
	}

	public static int relativePos(final int originX, final int originY, final int originZ, final long blockPos) {
		return relativePos(originX, originY, originZ, BlockPos.unpackLongX(blockPos), BlockPos.unpackLongY(blockPos), BlockPos.unpackLongZ(blockPos));
	}
	
	public static int relativePos(final int originX, final int originY, final int originZ, final BlockPos blockPos) {
		return relativePos(originX, originY, originZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static int relativePos(final int originX, final int originY, final int originZ, final int x, final int y, final int z) {
		return relativePos(x - originX, y - originY, z - originZ);
	}

	public static int relativePos(final int dx, final int dy, final int dz) {
		return (dx - MIN) | ((dy - MIN) << SHIFT) | ((dz - MIN) << (SHIFT * 2));
	}

	public static int rx(int relPos) {
		return (relPos & MASK) + MIN;
	}

	public static int ry(int relPos) {
		return ((relPos >> SHIFT) & MASK) + MIN;
	}

	public static int rz(int relPos) {
		return ((relPos >> (SHIFT * 2)) & MASK) + MIN;
	}

	public static long absolutePos(BlockPos originPos, int relativePos) {
		return absolutePos(originPos.getX(), originPos.getY(), originPos.getZ(), relativePos);
	}

	public static long absolutePos(long originPos, int relativePos) {
		return BlockPos.add(originPos, rx(relativePos), ry(relativePos), rz(relativePos));
	}

	public static long absolutePos(int originX, int originY, int originZ, int relativePos) {
		return BlockPos.asLong(originX + rx(relativePos), originY + ry(relativePos), originZ + rz(relativePos));
	}

	public static BlockPos.Mutable set(BlockPos.Mutable pos, int originX, int originY, int originZ, int relativePos) {
		return pos.set(originX + rx(relativePos), originY + ry(relativePos), originZ + rz(relativePos));
	}

	public static BlockPos.Mutable set(BlockPos.Mutable pos, long originPos, int relativePos) {
		return set(pos, BlockPos.unpackLongX(originPos), BlockPos.unpackLongY(originPos), BlockPos.unpackLongZ(originPos), relativePos);
	}

	public static BlockPos.Mutable set(BlockPos.Mutable pos, BlockPos originPos, int relativePos) {
		return set(pos, originPos.getX(), originPos.getY(), originPos.getZ(), relativePos);
	}

	public static int squaredDistance(int relPos) {
		final int x = rx(relPos);
		final int y = ry(relPos);
		final int z = rz(relPos);
		return x * x + y * y + z * z;
	}

	public static int horizontalSquaredDistance(int relPos) {
		final int x = rx(relPos);
		final int z = rz(relPos);
		return x * x + z * z;
	}
}
