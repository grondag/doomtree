package grondag.doomtree.model;

import java.util.function.Function;

import net.minecraft.block.BlockState;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;

public class HeartModel extends TerminalModel {
	
	protected HeartModel(Sprite sprite, Function<Identifier, Sprite> spriteMap) {
		super(sprite, spriteMap);
	}
	
	@Override
	protected int getHeight(BlockState state, int y) {
		return 0;
	}

	@Override
	protected int[] makeGlowColors() {
		return null;
	}
	
	@Override
	protected int glowColor(int height) {
		return 0xFF000000 | CHANNEL_HIGH_COLOR;
	}
	
	public static HeartModel create(Function<Identifier, Sprite> spriteMap) {
		return new HeartModel(spriteMap.apply(TERMINAL_TEXTURES.get(0)), spriteMap);
	}
}
