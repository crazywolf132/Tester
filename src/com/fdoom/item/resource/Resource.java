package com.fdoom.item.resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import com.fdoom.Game;
import com.fdoom.entity.Fire;
import com.fdoom.entity.Player;
import com.fdoom.entity.Spark;
import com.fdoom.entity.Torch;
import com.fdoom.entity.particle.TextParticle;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.level.Level;
import com.fdoom.level.tile.DirtTile;
import com.fdoom.level.tile.DoorTile;
import com.fdoom.level.tile.GrassTile;
import com.fdoom.level.tile.RockWallTile;
import com.fdoom.level.tile.Tile;
import com.fdoom.level.tile.WoodenWallTile;

public class Resource implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private static HashMap<String, Resource> resources = new HashMap<String, Resource>();
	
	public static Resource mshrm = new Resource("star", 11, Color.get(-1, 110, 440, 440), Game.ultimateSheet);
	public static Resource arrow = new Resource("Arrow", 33, Color.get(-1, 555, 555, 321), Game.ultimateSheet);
	
	public static Resource wood = new Resource("Wood", 1 + 4 * 32, Color.get(-1, 300, 421, 321));
	public static Resource stone = new Resource("Stone", 2 + 4 * 32, Color.get(-1, 111, 333, 555));
	public static Resource flower = new PlantableResource("Flower", 0 + 4 * 32, Color.get(-1, 10, 444, 330), Tile.flower, Tile.grass);
	public static Resource acorn = new PlantableResource("Acorn", 3 + 4 * 32, Color.get(-1, 100, 531, 320), Tile.treeSapling, Tile.grass);
	public static Resource dirt = new PlantableResource("Dirt", 2 + 4 * 32, Color.get(-1, 100, 322, 432), Tile.dirt, Tile.hole, Tile.water, Tile.lava);
	public static Resource sand = new PlantableResource("Sand", 2 + 4 * 32, Color.get(-1, 110, 440, 550), Tile.sand, Tile.grass, Tile.dirt);
	public static Resource cactusFlower = new PlantableResource("Cactus", 4 + 4 * 32, Color.get(-1, 10, 40, 50), Tile.cactusSapling, Tile.sand);
	public static Resource seeds = new PlantableResource("Seeds", 5 + 4 * 32, Color.get(-1, 10, 40, 50), Tile.wheat, Tile.farmland);
	public static Resource wheat = new Resource("Wheat", 6 + 4 * 32, Color.get(-1, 110, 330, 550));
	public static Resource bread = new FoodResource("Bread", 8 + 4 * 32, Color.get(-1, 110, 330, 550), 2, 5);
	public static Resource apple = new FoodResource("Apple", 9 + 4 * 32, Color.get(-1, 100, 300, 500), 1, 5);
	public static Resource mshr = new FoodResource("Mushy", 9 + 4 * 32, Color.get(-1, 100, 300, 500), Game.ultimateSheet, 1, 5);

	public static Resource coal = new Resource("COAL", 10 + 4 * 32, Color.get(-1, 000, 111, 111));
	public static Resource ironOre = new Resource("I.ORE", 10 + 4 * 32, Color.get(-1, 100, 322, 544));
	public static Resource goldOre = new Resource("G.ORE", 10 + 4 * 32, Color.get(-1, 110, 440, 553));
	public static Resource ironIngot = new Resource("IRON", 11 + 4 * 32, Color.get(-1, 100, 322, 544));
	public static Resource goldIngot = new Resource("GOLD", 11 + 4 * 32, Color.get(-1, 110, 330, 553));

	public static Resource slime = new Resource("SLIME", 10 + 4 * 32, Color.get(-1, 10, 30, 50));
	public static Resource glass = new Resource("glass", 12 + 4 * 32, Color.get(-1, 555, 555, 555));
	public static Resource cloth = new Resource("cloth", 1 + 4 * 32, Color.get(-1, 25, 252, 141));
	public static Resource cloud = new PlantableResource("cloud", 2 + 4 * 32, Color.get(-1, 222, 555, 444), Tile.cloud, Tile.infiniteFall);
	public static Resource gem = new Resource("gem", 13 + 4 * 32, Color.get(-1, 101, 404, 545));

	public static Resource plank = new Resource("Plank", 1 + 4 * 32, Color.get(-1, 200, 531, 430));
	public static Resource stoneTile = new Resource("tile", 1 + 4 * 32, Color.get(-1, 222, 555, 444));
	public static Resource door = new Resource("door", 6 + 10 * 32, Color.get(-1, 300, 522, 532));
	public static Resource window = new Resource("window", 6 + 10 * 32, Color.get(-1, 224, 225, 224));
	
	public static Resource torch = new Resource("torch", 7 + 10 * 32, Color.get(-1, 200, 441, 554));
	public static Resource flint = new Resource("Flint", 2 + 4 * 32, Color.get(-1, 111, 222, 333));

	public static Resource bottle = new Resource("Bottle", 14 + 4 * 32, Color.get(-1, 225, -1, 335));
	public static Resource ale = new FoodResource("Ale", 14 + 4 * 32, Color.get(-1, 421, 521, 335), 5, 8);

	public final String name;
	public final int sprite;
	public final int color;
	public SpriteSheet sheet;

	public Resource(String name, int sprite, int color, SpriteSheet sheet) {
		if (name.length() > 6) throw new RuntimeException("Name cannot be longer than six characters!");
		this.name = name;
		this.sprite = sprite;
		this.color = color;
		this.sheet = sheet;
	}
	
	public Resource(String name, int sprite, int color) {
		if (name.length() > 6) throw new RuntimeException("Name cannot be longer than six characters!");
		this.name = name;
		this.sprite = sprite;
		this.color = color;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		boolean sameTile = (xt == (player.x >> 4)) && (yt == (player.y >> 4));
		Random rand = new Random();
		if (this.equals(wood)) {
			// build wooden wall on dirt and grass
			if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)) && !sameTile) {
				level.setTile(xt, yt, Tile.woodenWall, 0);
				return true;
			}
		}
		if (this.equals(stone)) {
			// build rock wall on dirt and grass
			if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)) && !sameTile) {
				level.setTile(xt, yt, Tile.rockWall, 0);
				return true;
			}
		}
		if (this.equals(plank)) {
			// build fence on dirt and grass
			if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)) && !sameTile) {
				level.setTile(xt, yt, Tile.fence, 0);
				return true;
			}
		}
		if (this.equals(stoneTile)) {
			// build paved road on dirt and grass
			if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)) && !sameTile) {
				level.setTile(xt, yt, Tile.rockFloor, 0);
				return true;
			}
		}
		if (this.equals(door) || this.equals(window)) {
			// check for a frame
			Tile tl = level.getTile(xt-1, yt);
			Tile tr = level.getTile(xt+1, yt);
			Tile tu = level.getTile(xt, yt-1);
			Tile td = level.getTile(xt, yt+1);
			boolean l = xt > 0 && (tl.equals(Tile.rockWall) || tl.equals(Tile.woodenWall) || tl.equals(Tile.rock) || tl.equals(Tile.door) || tl.equals(Tile.window));
			boolean r = xt < level.w && (tr.equals(Tile.rockWall) || tr.equals(Tile.woodenWall) || tr.equals(Tile.rock) || tr.equals(Tile.door) || tr.equals(Tile.window));
			boolean u = yt > 0 && (tu.equals(Tile.rockWall) || tu.equals(Tile.woodenWall) || tu.equals(Tile.rock) || tu.equals(Tile.door) || tu.equals(Tile.window));
			boolean d = yt < level.h && (td.equals(Tile.rockWall) || td.equals(Tile.woodenWall) || td.equals(Tile.rock) || td.equals(Tile.door) || td.equals(Tile.window));
			System.out.println("l " + l + " r " + r + " u " + u + " d " + d);
			if (l&&r || u&&d) {
				// build door on dirt and grass
				if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)) && !sameTile) {
					Tile t = this.equals(door) ? Tile.door : Tile.window;
					level.setTile(xt, yt, t, 0);
					return true;
				}
			}
		}
		if (this.equals(torch)) {
			// place torch on dirt and grass and stone floor and sand
			if ((Tile.dirt.equals(tile) || Tile.grass.equals(tile)
					|| Tile.rockFloor.equals(tile)
					|| Tile.flower.equals(tile)
					|| Tile.sand.equals(tile)) && !sameTile) {
				level.add(new Torch(player, (xt << 4)+8, (yt << 4)+8));
				return true;
			}
		}
		if (this.equals(flint)) {
			// put a pile of wood (wall) on fire (maybe)
			if ((Tile.woodenWall.equals(tile)) && !sameTile) {
				// make sparks
				int sparks = rand.nextInt(5)+2;
				for (int i = 0; i < sparks; i++) {
					String sparkChars = ".,-+x";
					String sparkType = ""+sparkChars.charAt(rand.nextInt(sparkChars.length()));
					level.add(new TextParticle(sparkType, (xt << 4)+8, (yt << 4)+8, Color.get(-1, 554, 554, 554)));
				}
				// try to light it
				if (rand.nextInt(8) == 0) {
					level.add(new Fire(player, (xt << 4)+8, (yt << 4)+8, 1, 100));
				}
				// randomly loose the item
				if (rand.nextInt(5) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Resource)) {
			return false;
		}
		Resource res = (Resource)obj;
		if (!this.name.equals(res.name)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
}