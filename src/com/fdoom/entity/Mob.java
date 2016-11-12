package com.fdoom.entity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


public class Mob extends LivingEntity {
	
	public Mob() {
		karma = -100;
		x = y = 8;
		xr = 4;
		yr = 3;
	}

	public boolean blocks(Entity e) {
		return e.isBlockableBy(this);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException
	{
		super.readExternal(in);
		// TODO
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
		// TODO
	}
}
