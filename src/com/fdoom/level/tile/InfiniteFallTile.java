package com.fdoom.level.tile;

import com.fdoom.entity.AirWizard;
import com.fdoom.entity.Entity;
import com.fdoom.gfx.Screen;
import com.fdoom.level.Level;

public class InfiniteFallTile extends Tile {
	public InfiniteFallTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
	}

	public void tick(Level level, int xt, int yt) {
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		if (e instanceof AirWizard) return true;
		return false;
	}
}
