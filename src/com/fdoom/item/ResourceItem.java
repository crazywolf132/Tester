package com.fdoom.item;

import com.fdoom.entity.ItemEntity;
import com.fdoom.entity.Player;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;
import com.fdoom.level.tile.Tile;

public class ResourceItem extends Item {
	public Resource resource;
	public int count = 1;

	public ResourceItem(){
		
	}
	
	public ResourceItem(Resource resource) {
		this.resource = resource;
		this.sheet = resource.sheet;
	}

	public ResourceItem(Resource resource, int count) {
		this.resource = resource;
		this.count = count;
		this.sheet = resource.sheet;
	}

	public int getColor() {
		return resource.color;
	}

	public int getSprite() {
		return resource.sprite;
	}
	

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, resource.sprite, resource.color, 0);
	}

	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, resource.sprite, resource.color, 0);
		Font.draw(resource.name, screen, x + 32, y, Color.get(-1, 555, 555, 555));
		int cc = count;
		if (cc > 999) cc = 999;
		Font.draw("" + cc, screen, x + 8, y, Color.get(-1, 444, 444, 444));
	}

	public String getName() {
		return resource.name;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		if (resource.interactOn(tile, level, xt, yt, player, attackDir)) {
			count--;
			return true;
		}
		return false;
	}

	public boolean isDepleted() {
		return count <= 0;
	}

}