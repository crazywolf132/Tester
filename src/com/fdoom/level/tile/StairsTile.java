package com.fdoom.level.tile;

import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.level.Level;

public class StairsTile extends Tile {
	private boolean leadsUp;

	public StairsTile(int id, boolean leadsUp) {
		super(id);
		this.leadsUp = leadsUp;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int color = Color.get(level.dirtColor, 000, 333, 444);
		int xt = 0;
		if (leadsUp) xt = 2;
		screen.render(x * 16 + 0, y * 16 + 0, xt + 2 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 0, xt + 1 + 2 * 32, color, 0);
		screen.render(x * 16 + 0, y * 16 + 8, xt + 3 * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 8, xt + 1 + 3 * 32, color, 0);
	}
}
