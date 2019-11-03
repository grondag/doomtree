package grondag.doomtree.block.treeheart;

@FunctionalInterface interface Job {
	Job apply(DoomHeartBlockEntity heart);
}