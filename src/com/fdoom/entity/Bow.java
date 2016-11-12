package com.fdoom.entity;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.fdoom.Data;
import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.entity.Entity;
import com.fdoom.entity.Player;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.item.Item;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;
import com.fdoom.level.tile.Tile;

public class Bow extends Item
{
	public static SpriteSheet ultimateSheet;
	static String location = Data.locationRes;
	public Bow()
	{
		super();
		sheet = this.ultimateSheet;
		try{
		ultimateSheet = new SpriteSheet(ImageIO.read(new File(location + "wan.png")));
		}catch(IOException ex){
		    //System.out.println("Error Downloading the SpriteSheet!");
		    GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in Bow - Line 34. Please send to Dev!");
		}
	}

	public int getColor()
	{
		return Color.get(-1, 100, 321, 555);
	}

	public int getSprite()
	{
		return 1 + 0 * 32;
	}

	public void renderIcon(Screen screen, int x, int y)
	{
		screen.renderNew(x, y, getSprite(), getColor(), 0, this.ultimateSheet);
		System.out.println("this is  it: " + getSprite());
	}

	public boolean canAttack()
	{
		return false;
	}

	public boolean interact(Player player, Entity entity, int attackDir)
	{
		return shoot(player);
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir)
	{
		return shoot(player);
	}
	
	// Takes an arrow item from inventory and adds Arrow entity to the level
	public boolean shoot(Player player)
	{
		if (player.payStamina(1) && player.inventory.hasResources(Resource.arrow, 1))
		{
			player.level.add(new Arrow(player));
			player.inventory.removeResource(Resource.arrow, 1);
			return true;
		}
		return false;
	}

	public void renderInventory(Screen screen, int x, int y)
	{
		screen.renderNew(x, y, getSprite(), getColor(), 0, Game.ultimateSheet);
		Font.draw(this.getName(), screen, x + 8, y, Color.get(-1, 555, 555, 555));
	}
	
	public void renderActive(Player player, Screen screen, int x, int y)
	{
		int numOfArrows = player.inventory.getNumOfResources(Resource.arrow);
		if(numOfArrows > 99) numOfArrows = 99;
		screen.renderNew(x, y, getSprite(), getColor(), 0, Game.ultimateSheet);
		Font.draw(this.getName() + " X" + numOfArrows, screen, x + 8, y, Color.get(-1, 555, 555, 555));
	}

	public String getName()
	{
		return "bow";
	}
}