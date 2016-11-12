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

public class WoodenWallTile extends Tile {

	public static final int MAX_DAMAGE = 20;
	
	public WoodenWallTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(310, 420, 530, 333);
		int transitionColor = Color.get(310, 420, 530, level.dirtColor);

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
				screen.render(x * 16 + 0, y * 16 + 0, 24, col, 0);
			else
				screen.render(x * 16 + 0, y * 16 + 0, 28 + 0 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 23 : 22) + (u ? 2 : 1) * 32, transitionColor, 3);

		if (!u && !r) {
			if (!ur)
				screen.render(x * 16 + 8, y * 16 + 0, 25, col, 0);
			else
				screen.render(x * 16 + 8, y * 16 + 0, 29 + 0 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 21 : 22) + (u ? 2 : 1) * 32, transitionColor, 3);

		if (!d && !l) {
			if (!dl)
				screen.render(x * 16 + 0, y * 16 + 8, 26, col, 0);
			else
				screen.render(x * 16 + 0, y * 16 + 8, 28 + 1 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 23 : 22) + (d ? 0 : 1) * 32, transitionColor, 3);
		if (!d && !r) {
			if (!dr)
				screen.render(x * 16 + 8, y * 16 + 8, 27, col, 0);
			else
				screen.render(x * 16 + 8, y * 16 + 8, 29 + 1 * 32, transitionColor, 3);
		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 21 : 22) + (d ? 0 : 1) * 32, transitionColor, 3);
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
			if (ToolType.axe.equals(tool.type)) {
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
		if (damage >= MAX_DAMAGE) {
			int count = random.nextInt(2);
			for (int i = 0; i < count; i++) {
				level.add(new ItemEntity(new ResourceItem(Resource.wood), x * 16 + random.nextInt(10) + 3, y * 16 + random.nextInt(10) + 3));
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
	
	@Override
	public int getFireFuelAmount(Level level, int xt, int yt)
	{
		return MAX_DAMAGE - level.getData(xt, yt);
	}

	@Override
	public void burnFireFuel(Level level, int xt, int yt, int burnPower,
			Entity ent)
	{
		int damage = level.getData(xt, yt) + burnPower;
		if (damage >= MAX_DAMAGE) {
			level.setTile(xt, yt, Tile.dirt, 0);
		} else {
			level.setData(xt, yt, damage);
		}
	}
}
