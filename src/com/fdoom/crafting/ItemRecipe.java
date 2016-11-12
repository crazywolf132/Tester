package com.fdoom.crafting;

import com.fdoom.GameContainer;
import com.fdoom.entity.Player;
import com.fdoom.item.Item;

public class ItemRecipe extends Recipe
{
	private Class<? extends Item> clazz;

	public ItemRecipe(Class<? extends Item> clazz) throws InstantiationException, IllegalAccessException
	{
		super(clazz.newInstance());
		this.clazz = clazz;
	}

	public void craft(Player player)
	{
		try
		{
			player.inventory.add(0, clazz.newInstance());
		}
		catch (Exception e)
		{
			//throw new RuntimeException(e);
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in ItemRecipe. Please send to Dev!");
		}
	}

}