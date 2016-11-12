package com.fdoom;

import java.io.File;

public class Data {
	
	public static String OPERATING_SYSTEM = System.getProperty("os.name").toLowerCase();
	public static String location = "";
	public static String locationSaves = "";
	public static String locationRes = "";
	public static String locationOptions = "";
	public static String locationLogs = "";
	public static String locationBin = "";
	static{
		if (OPERATING_SYSTEM.indexOf("win") >= 0) {
			//GameContainer.Logger(java.util.logging.Level.INFO,"Windows detected\n");
			//System.out.println("Windows detected\n");
	        location = System.getenv("APPDATA") + "\\fossickersdoom\\";
	        locationBin = location + "bin\\";
	        locationSaves = location + "saves\\";
	        locationRes = location + "res\\";
	        locationOptions = location + "options\\";
	        locationLogs = location + "logs\\";
		} else if (OPERATING_SYSTEM.indexOf("mac") >= 0) {
			//GameContainer.Logger(java.util.logging.Level.INFO,"Mac detected\n");
			//System.out.println("Mac detected\n");
			location = System.getProperty( "user.home" ) + "/fossickersdoom/";
			locationBin = location + "bin/";
			locationSaves = location + "saves/";
			locationRes = location + "res/";
			locationOptions = location + "options/";
			locationLogs = location + "logs/";
		} else if (OPERATING_SYSTEM.indexOf("nix") >= 0 || OPERATING_SYSTEM.indexOf("nux") >= 0 || OPERATING_SYSTEM.indexOf("aix") > 0) {
			//GameContainer.Logger(java.util.logging.Level.INFO,"Unix or Linux detected\n");
			//System.out.println("Unix or Linux detected\n");
			location = System.getProperty( "user.home" ) + "/fossickersdoom/";
			locationBin = location + "bin/";
			locationSaves = location + "saves/";
			locationRes = location + "res/";
			locationOptions = location + "options/";
			locationLogs = location + "logs/";
		} else if (OPERATING_SYSTEM.indexOf("sunos") >= 0) {
			//System.out.println("Solaris detected\n");
			GameContainer.Logger(java.util.logging.Level.INFO,"Solaris detected\n");
		} else {
			//GameContainer.Logger(java.util.logging.Level.WARNING,"OS not found fallback to Linux\n");
			//System.out.println("OS not found fallback to Linux\n");
			location = System.getProperty( "user.home" ) + "/fossickersdoom/";
			locationBin = location + "bin/";
			locationSaves = location + "saves/";
			locationRes = location + "res/";
			locationOptions = location + "options/";
			locationLogs = location + "logs/";
		}
			File file = new File(location);
			file.mkdirs();
			File bin = new File(locationBin);
			bin.mkdirs();
			File saves = new File(locationSaves);
			saves.mkdirs();
			File res = new File(locationRes);
			res.mkdirs();
			File options = new File(locationOptions);
			options.mkdirs();
			File logs = new File(locationLogs);
			logs.mkdirs();
		System.out.println("Game root directory: " + location);
		System.out.println("Game bin directory: " + locationBin);
		System.out.println("Save directory: " + locationSaves);
		System.out.println("Res directory: " + locationRes);
		System.out.println("Options directory: " + locationOptions);
		System.out.println("Logs directory: " + locationLogs);
		} 
	
}
