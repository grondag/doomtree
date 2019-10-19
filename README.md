# Lore

Scholarly records of the doom tree are few.  Most attempts to study it end quickly in tragedy.

Most accounts suggest it has an other-worldly origin.  Also consistent is the notion that it has access to the infernal dimension, but most scholars doubt it originated there.

All known appearances have resulted in terrible destruction.  If left undisturbed, they eventually disappear after several centuries.  Documented attempts to remove a doom tree by force have been uniformly unsuccessful.

An unconfirmed account mentions an alchemist named Lerm the Odiferous who found a way to overcome the tree after much experimentation with its various components, afterwards bragging in the local tavern that the manuscript documenting his method was a masterwork of alchemical craft.  He was then promptly eaten by a "grue."  Lerm's manuscript, if ever there was one, has never surfaced in the historical record.

# Mechanics

The Doom Tree contributes to gameplay in multiple ways:

- Environmental hazard
- Mob spawning (difficult to build a mob farm from it, more like a mob arena)
- Source of unique crafting materials
- Potential focal point for quests
- Alternate mid-game access for Nether mobs/materials
- Stationary boss mob 

## Doom and Warding
The tree feeds on the "positive energy" of Overworld matter.  What little it leaves behind is called "doomed residue", obtained by breaking blocks affected by the tree.  This material is *toxic* to the tree but encapsulated using alien magics.

If the material is removed from the area of the tree and purified with obscure alchemical methods it eventually becomes "warding essence," and can be used to craft armor, tools and potions that help to defend against some of the effects of the tree.

## Alchemical Basin (Work in Progress)
The basin is "primed" using a bucket of water (for purification) or ditchwater (for infusing). It cannot be re-primed without emptying - either by using the contents or by breaking the basin.

### Purifying
- Prime the basin with water
- Place up to 8 Doomed Residue blocks immediately next to (same Y level) the basin
- Place 32 Doom Fragments or 8 Doom Logs into the basin (mix and match is OK)
- Basin will start "burning" the doom fragments.  
- When the burning is complete, the surrounding blocks are transformed into Warding Essence blocks
- If the basin is not empty, more doom fragments can be added. Once empty, it must be re-primed.
- There is no way to pause a burn.

 ### Infusing
- Prime the basin with witchwater
- Add Warding Essence items or blocks (blocks count for 4 items)
- Basin can accept up to 32 warded essence at one time
- Toss in iron, diamond, stone, glass, etc. (or use on basin) to infuse the items. Cost depends on the item.
- Use an empty bottle to create a warding potion (may require extra brewing step after)
- If the basin is not empty, more warded essence can be added. Once empty, it must be re-primed.

### Warded Equipment
- Tools and weapons are more durable and can affect Doom Tree materials without enchantment. (But can be enchanted.)
- Can be repaired by infusion in a basin
- Armor is more durable and has slightly better protection than base material. 
- A full set of armor and a warding potion protect fully against doom rays (future) and doom vine, and possibly other effects

# Implemented Features
- Tree grows and gets very big.  It extends up to 56 blocks up from where it was planted and down to bedrock.
- Nearby overworked blocks are converted to doomed variants to increase tree power level
- Nearby water source blocks are converted to Witchwater
- Full lava blocks are converted to doomed stone 
- Non-full blocks are simply destroyed
- *All* blocks are affected unless they in a protected block tag.  By default only a handful of especially rare / special blocks are included (dragon egg, portals, beacons, etc.)
- Doomed blocks break instantly and drop doomed residue
- Radius of the doom effect glows slowly, eventually reaching a 48-block radius
- Any non-doomed blocks placed in the doomed area will be reclaimed by the tree, usually almost instantly
- The doomed area is filled with an air block named "miasma" with some random low-light areas. (This air will eventually have game-play effects.)
- Doom logs (the tree structure) can only be broken by enchanted or warded axes
- Tree uses its power to repair any logs that are somehow broken. If the tree has power this happens almost instantly. 
- Doom logs do not burn, will not spawn mobs and are very resistant to explosions.
- Harvested doom logs can be used as a novel and durable building material. They are easier to harvest after they are removed from the tree.
- Doom "logs" are not real logs. They are not made of real wood and cannot be stripped or turned into planks.
- Doom "leaves" are similarly resistant to breaking and require an enchanted or warded tool.
- Shears or Silk Touch will harvest leaf blocks intact. Otherwise they drop "doom fragments" which can be used in alchemy.  Fortune doesn't help.
- Doomed Residue item/block with crafting recipes
- Warded Essence item/block with crafting recipes
- Alchemical Basin Recipe and Render 
- Alchemical Basin can be primed with water (for purifying) or Witchwater (for infusing)
- Tree loads surrounding chunks
- Overload protection: only three trees can exist at one time (could be configurable)
- Trees will not grow if trunk is above some non-breakable block (end portal) or if its radius would intersect with an existing tree

# Planned or Potential Features (Not all will be completed)
- Alchemical Basin Purifying
- Alchemical Basin Infusing
- Warded Iron ingots, tools and armor
- Warded Diamond ingots, tools and armor
- Warded Stone and Glass - reinforced building materials
- Potion of Warding
- Doom vine (grows from tree limbs, damaging on touch)
- Active mob spawning: tree uses power to  painting a minimum number of nearby hostile mobs)
- Upgraded mob spawning: when tree is threatened and at interesting times (full moon, as example) will spawn angry pigmen, charged creepers, blazes, magma cubes and ghasts.  
- Pet cats: the tree somehow loves cats and will allow no cat near the tree to come to harm, imbuing them with extra resistance and strange powers
- Doom ray: the "eyes" on the trunk of the tree zap nearby living mobs and players (except cats). The ray instantly destroys doomed blocks and dooms mundane overworld blocks. It can only be stopped by warded blocks.  The effects are quickly fatal without warded gear.
- Doom sickness: spending time in miasma has a chance to cause (non-fatal) negative effects
- Doom heart: drop received when doom tree heart is destroyed. Effects/use TBD.
