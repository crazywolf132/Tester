package com.fdoom.crafting;

import com.fdoom.entity.Player;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.item.ResourceItem;
import com.fdoom.item.resource.Resource;

public class ResourceRecipe extends Recipe {
	private Resource resource;
	private int count;

	public ResourceRecipe(Resource resource, int count) {
		super(new ResourceItem(resource, count));
		this.resource = resource;
		this.count = count;
	}
	
	public ResourceRecipe(Resource resource) {
		this(resource, 1);
	}

	public void craft(Player player) {
		player.inventory.add(0, new ResourceItem(resource, count));
	}
}
