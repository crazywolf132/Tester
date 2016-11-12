package com.fdoom.utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import com.fdoom.Data;
import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.screen.update.ForceMenu;

public class ClientUtils {
	
	public static int Status;
	static Logger logger;
	
	static String location = Data.location;
	static String locationRes = Data.locationRes;
	
	private static boolean AllNeedsDownload;
	private static boolean PlayerNeedsDownload;
	private static boolean ItemsNeedsDownload;
	private static boolean LandNeedsDownload;
	
	static String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
	
	/**@BEGGINING OF UPDATER**/
	
	/**
	 * @return 0 if you're good to go, 1 if an update is available, and -1 if you could not connect to the webserver.
	 * */
	public static int isClientUpToDate() {
		String lVersion = getLatestGameVersion();
		return lVersion.equals("Could not connect to webserver.") ? -1:Game.GAME_VERSION.equalsIgnoreCase(getLatestGameVersion()) ? 0:1;
	}
	
	public static String getLatestGameVersion() {
		try {
			//Connection c = new Connection(new URL("http://game.zectr.com/client/version"));
			Connection c = new Connection(new URL("http://zectr.com/fdoom/VERSION"));
			c.createConnection();
			for(String s : c.readURL()) {
				return s.trim();
			}
			Status = -1;
		} catch (MalformedURLException ex) {
			//e.printStackTrace();
			//return "Could not connect to webserver.";
			GameContainer.Logger(Level.WARNING,"Could not connect to WebServer");
		} catch (IOException ex) {
			//e.printStackTrace();
			//return "Could not connect to webserver.";
			GameContainer.Logger(Level.WARNING,"Could not connect to WebServer");
		}
		return Game.GAME_VERSION;
	}
	
	public static void DownloadUpdates(){
		/**
		 * @Download the Game.jar
		 */
		Status = 0;
		try(
			ReadableByteChannel in=Channels.newChannel(
					  
			new URL("https://zectr.com/fdoom/jar/FDOOM.jar").openStream());	
			FileChannel out=new FileOutputStream(location + Game.CLIENT_TITLE + "_update.jar").getChannel() ) {
			out.transferFrom(in, 0, Long.MAX_VALUE);
			Status = 1;
		}
		catch(IOException ex){
		    GameContainer.Logger(Level.SEVERE,"Error downloading the game!");
		}
		/**
		 * @Download the icons.png
		 */
		try(
			ReadableByteChannel in=Channels.newChannel(
					  
			new URL("https://zectr.com/fdoom/res/icons.png").openStream());	
			FileChannel out=new FileOutputStream(locationRes + "icons.png").getChannel() ) {
			out.transferFrom(in, 0, Long.MAX_VALUE);
			Status = 1;
		}
		catch(IOException ex){
		    GameContainer.Logger(Level.SEVERE,"Error Downloading the SpriteSheet! icons.png");
		}
		/**
		 * @Download the ultimate.png
		 */
		try(
			ReadableByteChannel in=Channels.newChannel(
					  
			new URL("https://zectr.com/fdoom/res/ultimate.png").openStream());	
			FileChannel out=new FileOutputStream(locationRes + "icons.png").getChannel() ) {
			out.transferFrom(in, 0, Long.MAX_VALUE);
			Status = 1;
		}
		catch(IOException ex){
		    GameContainer.Logger(Level.SEVERE,"Error Downloading the SpriteSheet! ultimate.png");
		}
		Status = 1;
	}
	
	/**@END OF UPDATER**/
	/**@BEGGINING OF DATE CHECKER**/
	public static boolean isChristmas;
	public static boolean isHalloween;
	
	public static void DateChecker(){
		Calendar var1 = Calendar.getInstance();
		if (var1.get(2) + 1 == 12 && var1.get(5) >= 20 && var1.get(5) <= 26)
        {
            isChristmas = true;
        }
		else if (var1.get(2) + 1 == 10 && var1.get(5) >= 25 && var1.get(5) <= 31)
		{
			isHalloween = true;
		}
		else if (var1.get(2) + 1 == 11 && var1.get(5) >= 01 && var1.get(5) <= 3)
		{
			isHalloween = true;
		}
	}
	/**@END OF DATE CHECKER**/
	/**@BEGGINING OF IMAGE CHECKER**/
	@SuppressWarnings("unused")
	public static void check(){
		try{
			/*File player = new File(locationRes + OptionFile.FinalUsername +".png");
			if(player.exists()){
				Game.playerSprite = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + OptionFile.FinalUsername +".png"))));
			}else{
				PlayerNeedsDownload = true;
			}*/
			
			File items = new File(locationRes + "items.png");
			if(items.exists()){
				Game.itemsSprite = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + "items.png"))));
			}else{
				ItemsNeedsDownload = true;
			}
			
			File land = new File(locationRes + "land.png");
			if(land.exists()){
				Game.landSprite = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + "land.png"))));
			}else{
				LandNeedsDownload = true;
			}
			
			File all = new File(locationRes + "all.png");
			if(land.exists()){
				Game.screen = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + "all.png"))));
				Game.fogScreen = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + "all.png"))));
				Game.lightScreen = new Screen(Game.WIDTH, Game.HEIGHT, new SpriteSheet(ImageIO.read(new File(locationRes + "all.png"))));
			}else{
				AllNeedsDownload = true;
			}
			
			File ultimate = new File(locationRes + "ultimate.png");
			if(ultimate.exists()){
				Game.ultimateSheet = new SpriteSheet(ImageIO.read(new File(locationRes + "ultimate.png")));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in Game - Line 222. Please send to Dev!");
		}
	}
	
	private static void download(){
		OptionFile.readOpt();
		if (PlayerNeedsDownload){
			try(
				ReadableByteChannel in=Channels.newChannel(
				new URL("https://zectr.com/fdoom/res/"+ OptionFile.name +".png").openStream());	
				FileChannel out=new FileOutputStream(locationRes + OptionFile.name + ".png").getChannel() ) {
				out.transferFrom(in, 0, Long.MAX_VALUE);
			}
			catch(IOException ex){
			    GameContainer.Logger(Level.SEVERE,"Error Downloading..." + OptionFile.name + ".png");
			}
		}
		if (ItemsNeedsDownload){
			try(
				ReadableByteChannel in=Channels.newChannel(
				new URL("https://zectr.com/fdoom/res/items.png").openStream());	
				FileChannel out=new FileOutputStream(locationRes + "items.png").getChannel() ) {
				out.transferFrom(in, 0, Long.MAX_VALUE);
			}
			catch(IOException ex){
			    GameContainer.Logger(Level.SEVERE,"Error Downloading... items.png");
			}
		}
		if (LandNeedsDownload){
			try(
				ReadableByteChannel in=Channels.newChannel(
				new URL("https://zectr.com/fdoom/res/land.png").openStream());	
				FileChannel out=new FileOutputStream(locationRes + "land.png").getChannel() ) {
				out.transferFrom(in, 0, Long.MAX_VALUE);
			}
			catch(IOException ex){
			    GameContainer.Logger(Level.SEVERE,"Error Downloading... land.png");
			}
		}
		if (AllNeedsDownload){
			try(
				ReadableByteChannel in=Channels.newChannel(
				new URL("https://zectr.com/fdoom/res/all.png").openStream());	
				FileChannel out=new FileOutputStream(locationRes + "all.png").getChannel() ) {
				out.transferFrom(in, 0, Long.MAX_VALUE);
			}
			catch(IOException ex){
			    GameContainer.Logger(Level.SEVERE,"Error Downloading... all.png");
			}
		}
	}
	/**@END OF IMAGE CHECKER**/
	
	public static void cleanDirectory(File dir) {
	    for (File file: dir.listFiles()) {
	        if(file.getName().equals(timeStamp)) {
	            //do nothing
	        } else {
	            //delete file
	            file.delete();
	        }

	    }
	}
}
