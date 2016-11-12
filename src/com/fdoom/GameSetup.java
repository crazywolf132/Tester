package com.fdoom;

import java.io.Serializable;

import com.fdoom.utils.OptionFile;

/**
 * Structure to hold game configuration.
 * 
 * @author CrazyWolf
 */
public class GameSetup implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static boolean disableFogOfWar = false;
	
	//System.err.println("DOES THIS EQUAL TRUE YET?!??");

	//TODO load the settings from the file and set accordingly.
	//public boolean disableFogOfWar = false;
	/*public GameSetup(){
		if (OptionFile.FinalFogSettings == "true"){
			disableFogOfWar = true;
		}
		else if (OptionFile.FinalFogSettings == "false"){
			disableFogOfWar = false;
		}
		System.err.println("Fog should be" + disableFogOfWar);
	}*/
}
