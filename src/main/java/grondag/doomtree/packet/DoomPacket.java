package grondag.doomtree.packet;

import grondag.doomtree.DoomTree;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum DoomPacket {
	MIASMA,
	XP_DRAIN,
	BASIN_CRAFT;
	
	private PacketByteBuf newBuffer() {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeEnumConstant(this);
		return buf;
	}
	
	public static void misama(World world, BlockPos pos) {
		  final PacketByteBuf buf = MIASMA.newBuffer();
		  buf.writeBlockPos(pos);
		  sendAround(world, pos, buf);
	}
	
	public static void xpDrain(World world, double x, double y, double z, BlockPos pos) {
		  final PacketByteBuf buf = XP_DRAIN.newBuffer();
		  buf.writeDouble(x);
		  buf.writeDouble(y);
		  buf.writeDouble(z);
		  buf.writeBlockPos(pos);
		  sendAround(world, pos, buf);
	}
	
	public static void basinCraft(World world, BlockPos pos) {
		  final PacketByteBuf buf = BASIN_CRAFT.newBuffer();
		  buf.writeBlockPos(pos);
		  sendAround(world, pos, buf);
	}
	
	public static Identifier IDENTIFIER = new Identifier(DoomTree.MOD_ID + ":doom_tree");
	
	public static void sendAround(World world, BlockPos pos, PacketByteBuf buf) {
		final Packet<?> packet = ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
		PlayerStream.around(world, pos, 32).forEach(p -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(p, packet));
	}
}
