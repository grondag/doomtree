package grondag.doomtree.entity;

public interface DoomEntityAccess {
	/** Returns max exposure since last call and resets to zero */
	int getAndClearDoomExposure();

	/** Sets exposure to max of input and existing value */
	int exposeToDoom(int doomExposure);
}
