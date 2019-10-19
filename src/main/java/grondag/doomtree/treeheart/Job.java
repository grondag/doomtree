package grondag.doomtree.treeheart;

@FunctionalInterface interface Job {
	Job apply(DoomHeartBlockEntity heart);
}