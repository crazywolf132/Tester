package com.fdoom.utils;

import com.fdoom.utils.SystemFile;
import com.fdoom.gfx.Color;
import com.fdoom.screen.Menu;
import com.fdoom.screen.NewPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.fdoom.Data;
import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.GameSetup;

public class OptionFile
{
    public static boolean created;
    public static Game g;
    public static int difficulty;
    public static String WorldSave;
    public static String CurrentWorld;
    public static String Fog;
    public static String FinalFog;
    public static String name;
    public static int color;
    public static int a;
    public static int b;
    public static int c;
    public static int d;
    public static int[] keys;
    public static int[] bkeys;
    static String location = Data.locationOptions;
    private static final String file = "config.properties";
    private GameSetup setup;
	private Menu parent;
	public static boolean onoff;
	
    
    static {
        OptionFile.created = false;
        OptionFile.difficulty = 1;
        OptionFile.WorldSave = "world";
        OptionFile.CurrentWorld = "null";
        OptionFile.Fog = "1";
        OptionFile.name = "Player";
        OptionFile.color = Color.get(-1, 100, 220, 532);
        OptionFile.a = -1;
        OptionFile.b = 100;
        OptionFile.c = 220;
        OptionFile.d = 532;
        OptionFile.keys = new int[] { 87, 65, 83, 68, 81, 86, 27, 67, 88, 89, 69, 82 };
        OptionFile.bkeys = new int[] { 87, 65, 83, 68, 81, 86, 27, 67, 88, 89, 69, 82 };
    }
    
    public static void create(final Game game) {
        OptionFile.created = true;
        OptionFile.g = game;
        final SystemFile sf = new SystemFile(location + file, game);
        sf.Create();
        sf.Close();
        readOpt();
        for (int i = 0; i < OptionFile.keys.length; ++i) {
            if (OptionFile.keys[i] == 0) {
                OptionFile.keys[i] = OptionFile.bkeys[i];
            }
        }
        writeOpt();
    }
    
    public static void readOpt() {
        if (!OptionFile.created) {
            return;
        }
        final SystemFile sf = new SystemFile(location + file, OptionFile.g);
        sf.Create();
        sf.Reset();
        sf.Read();
        int i = 0;
        while (!sf.EndOfFile()) {
            final String s = sf.ReadLn();
            if (i == 0) {
                OptionFile.difficulty = Integer.parseInt(s);
            }
            if (i == 1) {
            	OptionFile.Fog = s;
            	OptionFile.FinalFog = s;
            }
            if (i == 2) {
                OptionFile.name = s;
            }
            if (i >= 3 && i <= 6) {
                if (i == 3) {
                    OptionFile.a = (int)Double.parseDouble(s);
                }
                if (i == 4) {
                    OptionFile.b = (int)Double.parseDouble(s);
                }
                if (i == 5) {
                    OptionFile.c = (int)Double.parseDouble(s);
                }
                if (i == 6) {
                    OptionFile.d = (int)Double.parseDouble(s);
                    if (OptionFile.b < 1) {
                        OptionFile.b = 555;
                    }
                    if (OptionFile.c < 1) {
                        OptionFile.c = 0;
                    }
                    if (OptionFile.d < 1) {
                        OptionFile.d = 0;
                    }
                    OptionFile.color = Color.get(OptionFile.a, OptionFile.b, OptionFile.c, OptionFile.d);
                }
            }
            if (i == 7) {
                OptionFile.keys[i - 7] = Integer.parseInt(s);
            }
            if (i == 8) {
            	OptionFile.WorldSave = s;
            }
            if (i > 8){
            	OptionFile.CurrentWorld = s;
            }
            ++i;
        }
        sf.Close();
    }
    
    public static void writeOpt() {
        if (!OptionFile.created) {
            return;
        }
        final SystemFile sf = new SystemFile(location + file, OptionFile.g);
        sf.Create();
        sf.Rewrite();
        sf.WriteLn(String.valueOf(OptionFile.difficulty));
        sf.WriteLn(OptionFile.Fog);
        sf.WriteLn(OptionFile.name);
       // sf.WriteLn(Game.CLIENTCONNECTHOST);
        sf.WriteLn(String.valueOf((double)OptionFile.a));
        sf.WriteLn(String.valueOf((double)OptionFile.b));
        sf.WriteLn(String.valueOf((double)OptionFile.c));
        sf.WriteLn(String.valueOf((double)OptionFile.d));
        for (int i = 0; i < OptionFile.keys.length; ++i) {
            sf.WriteLn(String.valueOf(OptionFile.keys[i]));
        }
        sf.WriteLn(OptionFile.WorldSave);
        sf.WriteLn(OptionFile.CurrentWorld);
        sf.Write();
        sf.Close();
        
    }
    
    public OptionFile(Menu parent) {
		this.parent = parent;
		
		this.setup = GameContainer.getInstance().getSetup();
	}
    

}
