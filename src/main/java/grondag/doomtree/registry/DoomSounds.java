package grondag.doomtree.registry;

import grondag.doomtree.DoomTree;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

public class DoomSounds {
	public static final SoundEvent CREAK = register("creak");
	
	private DoomSounds() {
		// NO-OP
	}
	
	public static void init() {
		// NO-OP
	}
	
	private static SoundEvent register(String name) {
		return Registry.register(Registry.SOUND_EVENT, DoomTree.id(name), new SoundEvent(DoomTree.id(name)));
	}
}
