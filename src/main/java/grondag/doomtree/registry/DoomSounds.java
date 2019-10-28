package grondag.doomtree.registry;

import static grondag.doomtree.DoomTree.REG;

import net.minecraft.sound.SoundEvent;

public enum DoomSounds {
	;

	public static final SoundEvent BOIL = REG.sound("boil");
	public static final SoundEvent MIASMA = REG.sound("miasma");
	public static final SoundEvent DOOM_START = REG.sound("doom_start");
	public static final SoundEvent DOOM_SUMMON = REG.sound("doom_summon");
}
