package com.fdoom.crafting;

import com.fdoom.GameContainer;
import com.fdoom.entity.Furniture;
import com.fdoom.entity.Player;
import com.fdoom.item.FurnitureItem;

public class FurnitureRecipe extends Recipe {
	private Class<? extends Furniture> clazz;

	public FurnitureRecipe(Class<? extends Furniture> clazz) throws InstantiationException, IllegalAccessException {
		super(new FurnitureItem(clazz.newInstance()));
		this.clazz = clazz;
	}

	public void craft(Player player) {
		try {
			player.inventory.add(0, new FurnitureItem(clazz.newInstance()));
		} catch (Exception e) {
			//throw new RuntimeException(e);
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in FurnitureRecipe - Line 21. Please send to Dev!");
		}
	}
}
