package com.fdoom.utils;

import com.fdoom.utils.SystemFile;
import com.fdoom.gfx.Color;
import com.fdoom.screen.NewPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.fdoom.Data;
import com.fdoom.Game;
/*
public class OptionFile
{*/
//static String location = Data.locationOptions;
	/*static String FinalUsername;
	public static String FinalFogSettings;
	public static String FogSettings; //TODO fix.
	static final String username = NewPlayer.worldname;
	static final String OptionsFile = "player.properties";
	
	public static void create(){
		final File file = new File(location + OptionsFile);
		if (!file.exists()) {
	        try {
	            file.createNewFile();
	        }
	        catch (Exception e) {
	            System.out.println("You have an error! -Create()");
	        }
		}
	}
	public static void loadParamChanges() {
		create();
		try (InputStream in = new FileInputStream(location + OptionsFile)) {
			Properties props = new Properties();
			props.load(in);
			FinalUsername = props.getProperty("Username");
			FinalFogSettings = props.getProperty("Fog");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public static void saveParamChanges() {
		create();
	    try {
	        Properties props = new Properties();
	        props.setProperty("Username", username);
	        props.replace("Username", username);
	        props.setProperty("Fog", FogSettings);
	        props.replace("Fog", FogSettings);
	        File f = new File(location + OptionsFile);
	        OutputStream out = new FileOutputStream( f );
	        props.store(out, null);
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}*/
/*
}*/

public class OptionFileOld
{
    public static boolean created;
    public static Game g;
    public static int difficulty;
    public static int Fog;
    public static int FinalFog;
    public static String name;
    public static int color;
    public static int a;
    public static int b;
    public static int c;
    public static int d;
    public static int[] keys;
    public static int[] bkeys;
    static String location = Data.locationOptions;
    
    static {
        OptionFileOld.created = false;
        OptionFileOld.difficulty = 1;
        OptionFileOld.Fog = 1;
        OptionFileOld.name = "Player";
        OptionFileOld.color = Color.get(-1, 100, 220, 532);
        OptionFileOld.a = -1;
        OptionFileOld.b = 100;
        OptionFileOld.c = 220;
        OptionFileOld.d = 532;
        OptionFileOld.keys = new int[] { 87, 65, 83, 68, 81, 86, 27, 67, 88, 89, 69, 82 };
        OptionFileOld.bkeys = new int[] { 87, 65, 83, 68, 81, 86, 27, 67, 88, 89, 69, 82 };
    }
    
    public static void create(final Game game) {
        OptionFileOld.created = true;
        OptionFileOld.g = game;
        final SystemFile sf = new SystemFile(location + "options.txt", game);
        sf.Create();
        sf.Close();
        readOpt();
        for (int i = 0; i < OptionFileOld.keys.length; ++i) {
            if (OptionFileOld.keys[i] == 0) {
                OptionFileOld.keys[i] = OptionFileOld.bkeys[i];
            }
        }
        writeOpt();
    }
    
    public static void readOpt() {
        if (!OptionFileOld.created) {
            return;
        }
        final SystemFile sf = new SystemFile(location + "options.txt", OptionFileOld.g);
        sf.Create();
        sf.Reset();
        sf.Read();
        int i = 0;
        while (!sf.EndOfFile()) {
            final String s = sf.ReadLn();
            if (i == 0) {
                OptionFileOld.difficulty = Integer.parseInt(s);
            }
            if (i == 1) {
            	OptionFileOld.Fog = Integer.parseInt(s);
            	OptionFileOld.FinalFog = OptionFileOld.Fog;
            }
            if (i == 2) {
                OptionFileOld.name = s;
            }
            if (i >= 3 && i <= 6) {
                if (i == 3) {
                    OptionFileOld.a = (int)Double.parseDouble(s);
                }
                if (i == 4) {
                    OptionFileOld.b = (int)Double.parseDouble(s);
                }
                if (i == 5) {
                    OptionFileOld.c = (int)Double.parseDouble(s);
                }
                if (i == 6) {
                    OptionFileOld.d = (int)Double.parseDouble(s);
                    if (OptionFileOld.b < 1) {
                        OptionFileOld.b = 555;
                    }
                    if (OptionFileOld.c < 1) {
                        OptionFileOld.c = 0;
                    }
                    if (OptionFileOld.d < 1) {
                        OptionFileOld.d = 0;
                    }
                    OptionFileOld.color = Color.get(OptionFileOld.a, OptionFileOld.b, OptionFileOld.c, OptionFileOld.d);
                }
            }
            if (i > 6) {
                OptionFileOld.keys[i - 7] = Integer.parseInt(s);
            }
            ++i;
        }
        sf.Close();
    }
    
    public static void writeOpt() {
        if (!OptionFileOld.created) {
            return;
        }
        final SystemFile sf = new SystemFile(location + "options.txt", OptionFileOld.g);
        sf.Create();
        sf.Rewrite();
        sf.WriteLn(String.valueOf(OptionFileOld.difficulty));
        sf.WriteLn(String.valueOf(OptionFileOld.Fog));
        sf.WriteLn(OptionFileOld.name);
       // sf.WriteLn(Game.CLIENTCONNECTHOST);
        sf.WriteLn(String.valueOf((double)OptionFileOld.a));
        sf.WriteLn(String.valueOf((double)OptionFileOld.b));
        sf.WriteLn(String.valueOf((double)OptionFileOld.c));
        sf.WriteLn(String.valueOf((double)OptionFileOld.d));
        for (int i = 0; i < OptionFileOld.keys.length; ++i) {
            sf.WriteLn(String.valueOf(OptionFileOld.keys[i]));
        }
        sf.Write();
        sf.Close();
    }
}
