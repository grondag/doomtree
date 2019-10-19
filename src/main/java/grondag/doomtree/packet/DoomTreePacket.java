package grondag.doomtree.packet;

import grondag.doomtree.DoomTree;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public enum DoomTreePacket {
	MIASMA;
	
	private PacketByteBuf newBuffer() {
		final PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeEnumConstant(MIASMA);
		return buf;
	}
	
	public static Packet<?> misama(BlockPos pos) {
		  final PacketByteBuf buf = MIASMA.newBuffer();
		  buf.writeBlockPos(pos);
		  return ServerSidePacketRegistry.INSTANCE.toPacket(IDENTIFIER, buf);
	}
	
	public static Identifier IDENTIFIER = new Identifier(DoomTree.MOD_ID + ":doom_tree");
}
