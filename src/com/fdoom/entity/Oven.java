package com.fdoom.entity;

import com.fdoom.crafting.Crafting;
import com.fdoom.gfx.Color;
import com.fdoom.screen.CraftingMenu;

public class Oven extends Furniture {
	public Oven() {
		super("Oven");
		col = Color.get(-1, 000, 332, 442);
		sprite = 2;
		xr = 3;
		yr = 2;
	}

	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new CraftingMenu(Crafting.ovenRecipes, player));
		return true;
	}
}