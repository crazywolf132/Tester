package com.fdoom.entity;

import java.util.List;

import com.fdoom.Game;
import com.fdoom.entity.Entity;
import com.fdoom.entity.Mob;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.level.tile.Tile;

// This is the entity version of actual items that are on the ground in the level
public class Arrow extends Entity
{
	private int lifeTime;
	public double xa, ya;
	public double xx, yy;
	private int time;
	private Player owner;
	private int dir;

	public Arrow(Player player)
	{
		this.owner = player;

		xx = this.x = player.x;
		yy = this.y = player.y;
		xr = 0;
		yr = 0;

		xa = ya = 0;
		this.dir = player.dir;

		// Determine the arrow direction
		if (player.dir == 2)
		{
			this.xa = -1;
		}
		else if (player.dir == 3)
		{
			this.xa = 1;
		}
		else if (player.dir == 1)
		{
			this.ya = -1;
		}
		else
		{
			this.ya = 1;
		}

		// Set the arrow speed
		xa *= 3.5;
		ya *= 3.5;

		lifeTime = 60 * 10 + random.nextInt(30);
	}

	public void tick()
	{
		time++;

		if (time >= lifeTime)
		{
			remove();
			return;
		}

		xx += xa;
		yy += ya;

		x = (int)xx;
		y = (int)yy;

		List<Entity> toHit = level.getEntities(x, y, x, y);
		for (int i = 0; i < toHit.size(); i++)
		{
			Entity e = toHit.get(i);
			if (e instanceof Mob && !(e.getClass() == owner.getClass()))
			{
				e.hurt(owner, random.nextInt(5) + 1, dir);
				this.remove();
			}
		}

		if (owner.level.getTile(x / 16, y / 16).id != Tile.water.id && owner.level.getTile(x / 16, y / 16).id != Tile.lava.id && !(owner.level.getTile(x / 16, y / 16).mayPass(level, x / 16, y / 16, this)))
		{
			owner.level.getTile(x / 16, y / 16).hurt(level, x / 16, y / 16, owner, 1, dir);
			remove();
		}
	}

	public boolean isBlockableBy(Mob mob)
	{
		return false;
	}

	public void render(Screen screen)
	{
		if (time >= lifeTime - 6 * 20)
		{
			if (time / 6 % 2 == 0) return;
		}

		int xt = 2;

		int flipper = 0;
		if (xa < 0) flipper = 1;
		if (ya != 0) xt++;

		if (ya > 0) flipper = 2;

		screen.renderNew(x - 4, y - 4 - 2, xt, Color.get(-1, 555, 555, 321), flipper, Game.ultimateSheet);
	}

}