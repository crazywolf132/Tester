package com.fdoom.level.tile;

import com.fdoom.entity.Entity;
import com.fdoom.entity.ItemEntity;
import com.fdoom.entity.Player;
import com.fdoom.entity.particle.SmashParticle;
import com.fdoom.entity.particle.TextParticle;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.item.Item;
import com.fdoom.item.ResourceItem;
import com.fdoom.item.ToolItem;
import com.fdoom.item.ToolType;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;

public class RockTile extends Tile {
	public RockTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(444, 444, 333, 333);
		int transitionColor = Color.get(111, 444, 555, level.dirtColor);

		boolean u = level.getTile(x, y - 1) != this;
		boolean d = level.getTile(x, y + 1) != this;
		boolean l = level.getTile(x - 1, y) != this;
		boolean r = level.getTile(x + 1, y) != this;

		boolean ul = level.getTile(x - 1, y - 1) != this;
		boolean dl = level.getTile(x - 1, y + 1) != this;
		boolean ur = level.getTile(x + 1, y - 1) != this;
		boolean dr = level.getTile(x + 1, y + 1) != this;

		// attach to door
		if (level.getTile(x, y - 1).equals(Tile.door)
				|| level.getTile(x, y - 1).equals(Tile.window)) {
			u = false;
		}
		if (level.getTile(x, y + 1).equals(Tile.door)
				|| level.getTile(x, y + 1).equals(Tile.window)) {
			d = false;
		}
		if (level.getTile(x - 1, y).equals(Tile.door)
				|| level.getTile(x - 1, y).equals(Tile.window)) {
			l = false;
		}
		if (level.getTile(x + 1, y).equals(Tile.door)
				|| level.getTile(x + 1, y).equals(Tile.window)) {
			r = false;
		}
		
		if (!u && !l) {
			if (!ul)
				screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
			else
				screen.render(x * 16 + 0, y * 16 + 0, 7 + 0 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 6 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

		if (!u && !r) {
			if (!ur)
				screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
			else
				screen.render(x * 16 + 8, y * 16 + 0, 8 + 0 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 4 : 5) + (u ? 2 : 1) * 32, transitionColor, 3);

		if (!d && !l) {
			if (!dl)
				screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
			else
				screen.render(x * 16 + 0, y * 16 + 8, 7 + 1 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 6 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
		if (!d && !r) {
			if (!dr)
				screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
			else
				screen.render(x * 16 + 8, y * 16 + 8, 8 + 1 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 4 : 5) + (d ? 0 : 1) * 32, transitionColor, 3);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return false;
	}

	public void hurt(Level level, int x, int y, Entity source, int dmg, int attackDir) {
		hurt(level, x, y, dmg);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (ToolType.pickaxe.equals(tool.type)) {
				if (player.payStamina(4 - tool.level)) {
					hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10);
					return true;
				}
			}
		}
		return false;
	}

	public void hurt(Level level, int x, int y, int dmg) {
		int damage = level.getData(x, y) + dmg;
		level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
		if (damage >= 50) {
			int count = random.nextInt(4) + 1;
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new ResourceItem(Resource.stone), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
			count = random.nextInt(2);
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new ResourceItem(Resource.coal), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
			if (random.nextInt(10) == 0) {
				level.add(new ItemEntity(new ResourceItem(Resource.flint), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
			}
			level.setTile(x, y, Tile.dirt, 0);
		} else {
			level.setData(x, y, damage);
		}
	}

	public void tick(Level level, int xt, int yt) {
		int damage = level.getData(xt, yt);
		if (damage > 0) level.setData(xt, yt, damage - 1);
	}
}
