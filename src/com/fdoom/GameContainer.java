package com.fdoom;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessControlException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.util.logging.Logger;

import com.fdoom.screen.NewPlayer;
import com.fdoom.screen.SplashMenu;
import com.fdoom.screen.TitleMenu;
import com.fdoom.utils.ClientUtils;
import com.fdoom.utils.OptionFile;
import com.fdoom.gfx.*;
/**
 * 		Game Container
 * 
 * Main class used as a manager of a currently running game.
 * 
 * Purpose, responsibilities, functions:
 *  - Entry-point for starting the game.
 *  - Creator and owner of the UI (JFrame).
 *  - Singleton interface, handles one "main" game (though the number of games
 *  	running at the same time is theoretically not limited by this).
 *  - Save and load function (+ GUI), swaps loaded game with the active one. 
 * 
 * @author CrazyWolf
 */



public class GameContainer
{
	private static final Logger LOGGER = Logger.getLogger(
		    Thread.currentThread().getStackTrace()[0].getClassName() );

	public static Game game;
	
	private static GameContainer singleton;
	
	private static JFrame jFrame;
	private static GameSetup setup;
	static String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
	static String location = Data.locationLogs;
	
	private GameContainer()
	{
		this.setup = new GameSetup();
		// TODO: load setup from file
		this.game = new Game();
		//this.game.create();
		OptionFile.create(game);
		//OptionFile.writeOpt();
		//OptionFile.loadParamChanges();
		this.game.initGraphics();
		//OptionFile.readOpt();

		this.game.setMenu(new SplashMenu());

	}
    
    public static void Logger(Level loggingLevel, String string) {
    	File logs = new File(location);
    	ClientUtils.cleanDirectory(logs);
    	
    	Handler fileHandler = null;
		Formatter formatter = null;
		
		try{
			fileHandler = new FileHandler(location + "./" + timeStamp +".log");
			formatter = new SimpleFormatter();
			LOGGER.addHandler(fileHandler);
			fileHandler.setFormatter(formatter);
			fileHandler.setLevel(Level.ALL);
			LOGGER.setLevel(Level.ALL);
			LOGGER.log(loggingLevel, string);
			LOGGER.severe(string);
		}catch(IOException exception){
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
	}
	
	/**
	 * Returns a singleton instance of this class.
	 * 
	 * @return singleton
	 */
	public static GameContainer getInstance()
	{
		if (singleton == null) {
			singleton = new GameContainer();
		}
		return singleton;
	}
	
	/**
	 * Returns the currently active game.
	 * 
	 * @return game
	 */
	public static Game getGame()
	{
		return game;
	}
	
	/**
	 * Changes the currently active game.
	 * 
	 * @param game
	 */
	public static void setGame(Game game)
	{
		game = game;
	}
	
	/**
	 * Returns the current game setup.
	 * 
	 * @return
	 */
	public static GameSetup getSetup()
	{
		return setup;
	}
	
	/**
	 * Changes the game setup.
	 * 
	 * @param setup
	 */
	public static void setSetup(GameSetup setup)
	{
		setup = setup;
	}
	
	/**
	 * Performs a one-time initialization of the environment.
	 * Namely it creates a UI window (JFrame) which is later used to display the game.
	 */
	public void init()
	{
		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		jFrame = frame;
		
	}
	
	/**
	 * The currently active game is attached to the UI window and then started.
	 */
	public static void startGame()
	{
		game.setMinimumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		game.setMaximumSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		game.setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
		
		jFrame.add(game, BorderLayout.CENTER);
		jFrame.pack();
		
		game.start();
	}
	
	/**
	 * Stops the currently active game and removes it from the UI window.
	 */
	public static void stopGame()
	{
		game.stop();
		jFrame.remove(game);
	}
	
	public static void saveGameTest(String worldname){
		//TODO check if folder exists. If-not create it.
		try{
			File file = new File(Data.locationSaves + worldname + "/game");
			System.out.println("Saving: " + file + ".\n");
			try{
				FileOutputStream fileOut = new FileOutputStream(file);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				
				out.writeObject(game);
				out.close();
				fileOut.close();
				
			} catch(FileNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 233. Please send to Dev!");
	        } catch (IOException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 236. Please send to Dev!");
	        }
		} catch (AccessControlException e) {
			// no saving for you!
			e.printStackTrace();
		}
	}
	
	/**
	 * Displays a "Save as" dialog for the user to choose a savegame filename,
	 * then writes the active game to the file.
	 */
	public void saveGame()
	{
		try {			
			// create a file chooser
			final JFileChooser fc = new JFileChooser(location);
			// choose file
			int returnVal = fc.showSaveDialog(null);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
	            System.out.println("Game saving canceled by user.\n");
	            return;
	        }
			
			// get file
			File file = fc.getSelectedFile();
			
	        System.out.println("Saving: " + file.getName() + ".\n");
	        
	        // save game to file
			try {
	            FileOutputStream fileOut = new FileOutputStream(file);
	            ObjectOutputStream out = new ObjectOutputStream(fileOut);
	
	            out.writeObject(this.game);
	            
	            out.close();
	            fileOut.close();
	        } catch(FileNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 233. Please send to Dev!");
	        } catch (IOException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 236. Please send to Dev!");
	        }
			
		} catch (AccessControlException e) {
			// no saving for you!
			e.printStackTrace();
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 242. Please send to Dev!");
		}
	}
	
	public static void loadGameTest(String worldname){
		try{
			File file = new File(Data.locationSaves + worldname + "/game");
			System.out.println("Loading: " + file + ".\n");
			
			 // load game from file
	        Game newGame = null;
			try {
	            FileInputStream fileIn = new FileInputStream(file);
	            ObjectInputStream in = new ObjectInputStream(fileIn);
	
	            newGame = (Game)in.readObject();
	            
	            in.close();
	            fileIn.close();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 280. Please send to Dev!");
	            return;
	        } catch(FileNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 284. Please send to Dev!");
	            return;
	        } catch (IOException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 288. Please send to Dev!");
	            return;
	        }
			
			// swap games
			stopGame();
			game = newGame;
			game.loadGame();
			startGame();
		} catch (AccessControlException e) {
			// no loading for you!
			e.printStackTrace();
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 300. Please send to Dev!");
		}
	}
	
	/**
	 * Displays an "Open file" dialog for the user to choose a savegame file,
	 * then deserializes the saved game and swaps it with the current one.
	 * The loaded game is then started.
	 */
	public void loadGame()
	{
		try {
			// create a file chooser
			final JFileChooser fc = new JFileChooser(location);

			// choose file
			int returnVal = fc.showOpenDialog(null);
			if (returnVal != JFileChooser.APPROVE_OPTION) {
	            System.out.println("Game loading canceled by user.\n");
	            return;
	        }
			
			// get file
			File file = fc.getSelectedFile();
	        System.out.println("Opening: " + file.getName() + ".\n");
	        
	        // load game from file
	        Game newGame = null;
			try {
	            FileInputStream fileIn = new FileInputStream(file);
	            ObjectInputStream in = new ObjectInputStream(fileIn);
	
	            newGame = (Game)in.readObject();
	            
	            in.close();
	            fileIn.close();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 280. Please send to Dev!");
	            return;
	        } catch(FileNotFoundException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 284. Please send to Dev!");
	            return;
	        } catch (IOException e) {
	            e.printStackTrace();
	            GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 288. Please send to Dev!");
	            return;
	        }
			
			// swap games
			this.stopGame();
			this.game = newGame;
			this.game.loadGame();
			this.startGame();
		} catch (AccessControlException e) {
			// no loading for you!
			e.printStackTrace();
			GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in GameContainer - Line 300. Please send to Dev!");
		}
	}
	
	public static void main(String[] args)
	{
		GameContainer cont = GameContainer.getInstance();
		cont.init();
		cont.startGame();
	}
}
