package com.fdoom.level.tile;

import com.fdoom.entity.Entity;
import com.fdoom.entity.ItemEntity;
import com.fdoom.entity.Player;
import com.fdoom.entity.particle.SmashParticle;
import com.fdoom.entity.particle.TextParticle;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.item.Item;
import com.fdoom.item.ResourceItem;
import com.fdoom.item.ToolItem;
import com.fdoom.item.ToolType;
import com.fdoom.item.resource.Resource;
import com.fdoom.level.Level;

public class WindowTile extends Tile {

	public static final int MAX_DAMAGE = 10;
	
	private Tile onType;
	private boolean opened = false;
	private boolean locked = false;

	public WindowTile(int id) {
		this(id, Tile.dirt);
	}
	
	public WindowTile(int id, Tile onType) {
		super(id);
		this.onType = onType;
		connectsToSand = onType.connectsToSand;
		connectsToGrass = onType.connectsToGrass;
		connectsToWater = onType.connectsToWater;
		connectsToLava = onType.connectsToLava;
		connectsToPavement = onType.connectsToPavement;
	}

	public void setOnType(Tile onType)
	{
		this.onType = onType;
	}
	
	public Tile getOnType()
	{
		return this.onType;
	}
	
	public void render(Screen screen, Level level, int x, int y) {
		onType.render(screen, level, x, y);
		int color = Color.get(100, 421, 532, 345);
		screen.render(x * 16 + 0, y * 16 + 0, 17 + (3) * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 18 + (3) * 32, color, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 17 + (4) * 32, color, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 18 + (4) * 32, color, 0);
	}

	public void tick(Level level, int x, int y) {
		
	}

	@Override
	public boolean interact(Level level, int xt, int yt, Player player,
			Item item, int attackDir)
	{
		// deconstruct with axe
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem)item;
			if (tool.type.equals(ToolType.axe)) {
				level.setTile(xt, yt, this.onType, 0);
				level.add(new ItemEntity(new ResourceItem(Resource.window),
						(xt << 4) + random.nextInt(11) - 5, (yt << 4) + random.nextInt(11) - 5));
				return true;
			}
		}
		return false;
	}
	
	public void hurt(Level level, int x, int y, Entity source, int dmg, int attackDir) {
		int damage = level.getData(x, y) + dmg;
		level.add(new SmashParticle(x * 16 + 8, y * 16 + 8));
		level.add(new TextParticle("" + dmg, x * 16 + 8, y * 16 + 8, Color.get(-1, 500, 500, 500)));
		if (damage >= MAX_DAMAGE) {
			level.setTile(x, y, onType, 0);
		} else {
			level.setData(x, y, damage);
		}
	}
	
	@Override
	public boolean mayPass(Level level, int x, int y, Entity e)
	{
		return false;
	}
	
	@Override
	public int getVisibilityBlocking(Level level, int x, int y, Entity e)
	{
		return 10;
	}
	
	@Override
	public int getFireFuelAmount(Level level, int xt, int yt)
	{
		return MAX_DAMAGE - level.getData(xt, yt);
	}

	@Override
	public void burnFireFuel(Level level, int xt, int yt, int burnPower,
			Entity ent)
	{
		int damage = level.getData(xt, yt) + burnPower;
		if (damage >= MAX_DAMAGE) {
			level.setTile(xt, yt, Tile.dirt, 0);
		} else {
			level.setData(xt, yt, damage);
		}
	}
}