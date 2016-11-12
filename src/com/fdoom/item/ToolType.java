package com.fdoom.item;

import java.io.Serializable;

public class ToolType implements Serializable {
	
	private static final long serialVersionUID = 8977442344072519611L;
	
	public static ToolType shovel = new ToolType("Shvl", 0);
	public static ToolType hoe = new ToolType("Hoe", 1);
	public static ToolType sword = new ToolType("Swrd", 2);
	public static ToolType pickaxe = new ToolType("Pick", 3);
	public static ToolType axe = new ToolType("Axe", 4);

	public final String name;
	public final int sprite;

	private ToolType(String name, int sprite) {
		this.name = name;
		this.sprite = sprite;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof ToolType)) {
			return false;
		}
		ToolType type = (ToolType)obj;
		if (!this.name.equals(type.name)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
}
