package com.fdoom.level.tile;

import com.fdoom.entity.ItemEntity;
import com.fdoom.entity.Player;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.item.Item;
import com.fdoom.item.ResourceItem;
import com.fdoom.item.ToolItem;
import com.fdoom.item.ToolType;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;
import com.fdoom.sound.Sound;

public class RockFloorTile extends Tile {
	public RockFloorTile(int id) {
		super(id);
		this.connectsToPavement = true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int baseCol = (((level.grassColor - (level.grassColor / 100) * 100) / 10)-1) * 111; 
		int col = Color.get(baseCol, baseCol, baseCol + 111, baseCol + 111);
		int transitionColor = Color.get(baseCol - 111, baseCol, baseCol + 111, level.dirtColor);

		boolean u = !level.getTile(x, y - 1).connectsToPavement;
		boolean d = !level.getTile(x, y + 1).connectsToPavement;
		boolean l = !level.getTile(x - 1, y).connectsToPavement;
		boolean r = !level.getTile(x + 1, y).connectsToPavement;

		if (!u && !l) {
			screen.render(x * 16 + 0, y * 16 + 0, 24, col, 0);
		} else
			screen.render(x * 16 + 0, y * 16 + 0, (l ? 11 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

		if (!u && !r) {
			screen.render(x * 16 + 8, y * 16 + 0, 25, col, 0);
		} else
			screen.render(x * 16 + 8, y * 16 + 0, (r ? 13 : 12) + (u ? 0 : 1) * 32, transitionColor, 0);

		if (!d && !l) {
			screen.render(x * 16 + 0, y * 16 + 8, 26, col, 0);
		} else
			screen.render(x * 16 + 0, y * 16 + 8, (l ? 11 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
		if (!d && !r) {
			screen.render(x * 16 + 8, y * 16 + 8, 27, col, 0);
		} else
			screen.render(x * 16 + 8, y * 16 + 8, (r ? 13 : 12) + (d ? 2 : 1) * 32, transitionColor, 0);
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (ToolType.pickaxe.equals(tool.type)) {
				if (player.payStamina(4 - tool.level)) {
					level.setTile(xt, yt, Tile.dirt, 0);
					level.add(new ItemEntity(new ResourceItem(Resource.stoneTile), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					Sound.monsterHurt.play();
					return true;
				}
			}
		}
		return false;
	}
}
