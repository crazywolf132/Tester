package com.fdoom.level.tile;

import java.io.Serializable;
import java.util.Random;

import com.fdoom.entity.Entity;
import com.fdoom.entity.Player;
import com.fdoom.gfx.Screen;
import com.fdoom.item.Item;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;

public class Tile implements Serializable {

	private static final long serialVersionUID = 2L;
	
	public static int tickCount = 0;
	protected Random random = new Random();

	public static Tile[] tiles = new Tile[256];
	public static Tile grass = new GrassTile(0);
	public static Tile rock = new RockTile(1);
	public static Tile water = new WaterTile(2);
	public static Tile flower = new FlowerTile(3);
	public static Tile tree = new TreeTile(4);
	public static Tile dirt = new DirtTile(5);
	public static Tile sand = new SandTile(6);
	public static Tile cactus = new CactusTile(7);
	public static Tile hole = new HoleTile(8);
	public static Tile treeSapling = new SaplingTile(9, grass, tree);
	public static Tile cactusSapling = new SaplingTile(10, sand, cactus);
	public static Tile farmland = new FarmTile(11);
	public static Tile wheat = new WheatTile(12);
	public static Tile lava = new LavaTile(13);
	public static Tile stairsDown = new StairsTile(14, false);
	public static Tile stairsUp = new StairsTile(15, true);
	public static Tile infiniteFall = new InfiniteFallTile(16);
	public static Tile cloud = new CloudTile(17);
	public static Tile hardRock = new HardRockTile(18);
	public static Tile ironOre = new OreTile(19, Resource.ironOre);
	public static Tile goldOre = new OreTile(20, Resource.goldOre);
	public static Tile gemOre = new OreTile(21, Resource.gem);
	public static Tile cloudCactus = new CloudCactusTile(22);
	
	public static Tile woodenWall = new WoodenWallTile(100);
	public static Tile rockWall = new RockWallTile(101);
	public static Tile fence = new FenceTile(102);
	public static Tile rockFloor = new RockFloorTile(103);
	public static Tile door = new DoorTile(104, dirt);
	public static Tile window = new WindowTile(105, dirt);

	public final byte id;

	public boolean connectsToGrass = false;
	public boolean connectsToSand = false;
	public boolean connectsToLava = false;
	public boolean connectsToWater = false;
	public boolean connectsToPavement = false;

	public Tile(int id) {
		this.id = (byte) id;
		if (tiles[id] != null) throw new RuntimeException("Duplicate tile ids!");
		tiles[id] = this;
	}

	public void render(Screen screen, Level level, int x, int y) {
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public int getLightRadius(Level level, int x, int y) {
		return 0;
	}
	
	/**
	 * Returns the amount of visibility blocking power.
	 * 
	 * The default implementation is that the entity can see through what it
	 * can pass through.
	 * 
	 * @param level
	 * @param x
	 * @param y
	 * @param e
	 * @return Value from 0 (see-through) to 100 (completely blocks), 
	 */
	public int getVisibilityBlocking(Level level, int x, int y, Entity e) {
		return mayPass(level, x, y, e) ? 0 : 100;
	}

	public void hurt(Level level, int x, int y, Entity source, int dmg, int attackDir) {
	}

	public void bumpedInto(Level level, int xt, int yt, Entity entity) {
	}

	public void tick(Level level, int xt, int yt) {
	}

	public void steppedOn(Level level, int xt, int yt, Entity entity) {
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		return false;
	}

	public boolean use(Level level, int xt, int yt, Player player, int attackDir) {
		return false;
	}

	public boolean connectsToLiquid() {
		return connectsToWater || connectsToLava;
	}

	/**
	 * Returns the number of units remaining to burn.
	 * 
	 * @param level
	 * @param xt
	 * @param yt
	 * @return
	 */
	public int getFireFuelAmount(Level level, int xt, int yt)
	{
		return 0;
	}

	/**
	 * Burns the given amount of fire fuel.
	 * 
	 * @param level
	 * @param xt
	 * @param yt
	 * @param burnPower
	 * @param fire
	 */
	public void burnFireFuel(Level level, int xt, int yt, int burnPower,
			Entity ent)
	{
	}
	
	/**
	 * Checks whether the tile is flammable (has fuel to burn).
	 * 
	 * @param level
	 * @param xt
	 * @param yt
	 * @return
	 */
	public boolean isFlammable(Level level, int xt, int yt)
	{
		return getFireFuelAmount(level, xt, yt) > 0;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Tile)) {
			return false;
		}
		Tile tile = (Tile)obj;
		if (this.id != tile.id) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}
}