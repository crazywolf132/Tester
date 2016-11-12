package com.fdoom;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Externalizable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.fdoom.entity.Entity;
import com.fdoom.entity.Mob;
import com.fdoom.entity.Player;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.level.*;
import com.fdoom.level.tile.Tile;
import com.fdoom.screen.DeadMenu;
import com.fdoom.screen.LevelTransitionMenu;
import com.fdoom.screen.Menu;
import com.fdoom.screen.NewPlayer;
import com.fdoom.screen.SplashMenu;
import com.fdoom.screen.TitleMenu;
import com.fdoom.screen.WonMenu;
import com.fdoom.screen.WorldSelectMenuTest;
import com.fdoom.utils.ClientUtils;
import com.fdoom.utils.OptionFile;

public class Game extends Canvas implements Runnable, Externalizable
{
	private static final long serialVersionUID = 2L;
	
	private Random random = new Random();
	
	public static final String NAME = "Fossickers Doom";
	public static final String CLIENT_TITLE = "FDOOM";
	public static final String GAME_VERSION = "1.0";
	public static final String RES_VERSION = "1.0";
	public static final String SOUND_VERSION = "1.0";
	public static final int HEIGHT = 200;
	public static final int WIDTH = 300;
	public static final int SCALE = 3;
	
	public static boolean hasSaved = false;
	
	public static final String OptionsFile = "options.txt";
	
	public static boolean devmode = false;

	private GameSetup setup = new GameSetup();
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private boolean running = false;
	
	public static Screen lightScreen;
	public static Screen fogScreen;
	public static Screen playerSprite;
	public static Screen itemsSprite;
	public static Screen landSprite;
	public static Screen screen;
	
	public static SpriteSheet ultimateSheet;
	private InputHandler input = new InputHandler(this);

	private int[] colors = new int[256];
	private int tickCount = 0;
	public int gameTime = 0;

	private Level level;
	private Level[] levels = new Level[5];
	private int currentLevel = 3;
	public Player player;

	static String location = Data.locationRes;
	static String locationOpt = Data.locationOptions;
	
	public Menu menu;
	private int playerDeadTime;
	private int pendingLevelChange;
	private int wonTimer = 0;
	public boolean hasWon = false;

	public static final int DAY_LENGTH = 20000;
	
	static String locationRes = Data.locationRes;
	static String locationSaves = Data.locationRes;
	
	public void setMenu(Menu menu) {
		this.menu = menu;
		if (menu != null) menu.init(this, input);
	}
	
	public GameSetup getSetup()
	{
		return this.setup;
	}
	
	public void setSetup(GameSetup setup)
	{
		this.setup = setup;
	}
	
	/**
	 * Returns the part of the day.
	 * 
	 * @return 0 is midnight, 0.5 is noon, ...
	 */
	public double getDayCycle()
	{
		// the game time is shifted by a few hours so we start in the morning
		int dayTicks = (gameTime + DAY_LENGTH / 4) % DAY_LENGTH;
		return dayTicks / (double)DAY_LENGTH;
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void resetGame() {
		playerDeadTime = 0;
		wonTimer = 0;
		gameTime = 0;
		hasWon = false;

		levels = new Level[5];
		currentLevel = 3;

		levels[4] = new Level(128, 128, 1, null);
		levels[3] = new Level(128, 128, 0, levels[4]);
		levels[2] = new Level(128, 128, -1, levels[3]);
		levels[1] = new Level(128, 128, -2, levels[2]);
		levels[0] = new Level(128, 128, -3, levels[1]);

		level = levels[currentLevel];
		player = new Player(this, input);
		player.findStartPos(level);

		level.add(player);

		for (int i = 0; i < 5; i++) {
			OptionFile.readOpt();
			int count = 5000;
			count += count * OptionFile.difficulty;
			if (OptionFile.difficulty == 0) count = 0;
			levels[i].trySpawn(count);
		}
	}
	
	/**
	 * Performs a full initialization of the game - graphics, generated levels,
	 * etc. The result is a fresh new game ready to be played.
	 * 
	 * This method should NOT be used for loaded games. Loaded games are inited
	 * as the player starts a new game. After loading them we call initGraphics
	 * and we are done.
	 */
	public void init() {
		
		ClientUtils.DateChecker();
		initGraphics();

		resetGame();
		setMenu(new SplashMenu());
	}
	
	
	
	/**
	 * Performs initialization of the game graphics.
	 */
	protected void initGraphics() {
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					colors[pp++] = r1 << 16 | g1 << 8 | b1;

				}
			}
		}
		ClientUtils.check();
		//create();
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}
			
			if (!running) {
				shouldRender = false;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
				GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in Game - Line 280. Please send to Dev!");
			}

			if (shouldRender) {
				frames++;
				try {
					render();
				} catch (IllegalStateException e) {
					// this is where it gets messed up so we bail out!
					System.err.println("Game thread exiting, rendering failed:");
					GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in Game - Line 289. Please send to Dev!");
					e.printStackTrace();
					running = false;
					break;
				}
			}

			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
				//System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		tickCount++;
		//TODO add it here.
		
		if (!hasFocus()) {
			input.releaseAll();
		} else {
			input.tick();
			if (menu != null) {
				menu.tick();
			} else {
				if (gameTime > 0) {
					if (player.removed) {
						playerDeadTime++;
						if (playerDeadTime > 60) {
							setMenu(new DeadMenu());
						}
					} else {
						if (pendingLevelChange != 0) {
							setMenu(new LevelTransitionMenu(pendingLevelChange));
							pendingLevelChange = 0;
						}
					}
					if (wonTimer > 0) {
						if (--wonTimer == 0) {
							setMenu(new WonMenu());
						}
					}
					if (!player.removed && !hasWon) gameTime++;
				} else {
					gameTime++;
				}
				level.tick();
				Tile.tickCount++;
			}
		}
	}

	public void changeLevel(int dir) {
		level.remove(player);
		currentLevel += dir;
		level = levels[currentLevel];
		player.x = (player.x >> 4) * 16 + 8;
		player.y = (player.y >> 4) * 16 + 8;
		level.add(player);

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		renderView();

		renderGui();
		if(!devmode){
			if (!hasFocus()) renderFocusNagger();
		}

		for (int y = 0; y < screen.h; y++) {
			for (int x = 0; x < screen.w; x++) {
				int cc = screen.pixels[x + y * screen.w];
				if (cc < 255) pixels[x + y * WIDTH] = colors[cc];
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		int ww = WIDTH * 3;
		int hh = HEIGHT * 3;
		int xo = (getWidth() - ww) / 2;
		int yo = (getHeight() - hh) / 2;
		g.drawImage(image, xo, yo, ww, hh, null);
		g.dispose();
		bs.show();
	}
	
	private void renderView()
	{
		if (this.gameTime <= 0) {
			return;
		}
		
		int xScroll = player.x - screen.w / 2;
		int yScroll = player.y - (screen.h - 8) / 2;
		// we have a nice border, so the player stays in the center!
		//if (xScroll < 16) xScroll = 16;
		//if (yScroll < 16) yScroll = 16;
		//if (xScroll > level.w * 16 - screen.w - 16) xScroll = level.w * 16 - screen.w - 16;
		//if (yScroll > level.h * 16 - screen.h - 16) yScroll = level.h * 16 - screen.h - 16;
		
		if (currentLevel > 3) {
			int col = Color.get(20, 20, 121, 121);
			for (int y = 0; y < 14; y++)
				for (int x = 0; x < 24; x++) {
					screen.render(x * 8 - ((xScroll / 4) & 7), y * 8 - ((yScroll / 4) & 7), 0, col, 0);
				}
		}

	    // render level tiles
		level.renderBackground(screen, xScroll, yScroll);
		
		// render level sprites
		level.renderSprites(screen, xScroll, yScroll);
		
		// prepare light-map
		lightScreen.clear(0);
		level.renderLight(lightScreen, xScroll, yScroll);
		OptionFile.readOpt();
		// render fog-of-war
		if (OptionFile.FinalFog.equals("1")) {
			fogScreen.clear(0);
			level.renderFog(fogScreen, lightScreen, xScroll, yScroll);
			screen.overlay(fogScreen, xScroll, yScroll);
		}
		
		// render darkness
		if (currentLevel < 3 && OptionFile.FinalFog.equals("0")) {
			screen.overlay(lightScreen, xScroll, yScroll);
		}
	}

	private void renderGui() {
		if (this.gameTime > 0) {
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 20; x++) {
					screen.render(x * 8, screen.h - 16 + y * 8, 0 + 12 * 32, Color.get(-1, -1, -1, -1), 0);
				}
			}
	
			for (int i = 0; i < 10; i++) {
				if (i < player.health)
					screen.render(i * 8, screen.h - 200, 0 + 12 * 32, Color.get(-1, 200, 500, 533), 0);
				else
					screen.render(i * 8, screen.h - 200, 0 + 12 * 32, Color.get(-1, 100, 000, 000), 0);
	
				if (player.staminaRechargeDelay > 0) {
					if (player.staminaRechargeDelay / 4 % 2 == 0)
						screen.render(i * 8, screen.h - 192, 1 + 12 * 32, Color.get(-1, 555, 000, 000), 0);
					else
						screen.render(i * 8, screen.h - 192, 1 + 12 * 32, Color.get(-1, 110, 000, 000), 0);
				} else {
					if (i < player.stamina)
						screen.render(i * 8, screen.h - 192, 1 + 12 * 32, Color.get(-1, 220, 550, 553), 0);
					else
						screen.render(i * 8, screen.h - 192, 1 + 12 * 32, Color.get(-1, 110, 000, 000), 0);
				}
			}
			if (player.activeItem != null) {
				for (int x = 0; x <= this.player.activeItem.getName().length(); ++x) {
	                //this.screen.render(x * 8 + 80, this.screen.h - 8, 384, Color.get(0, 0, 0, 0), 0); // This is the old one...
					this.screen.render(x * 8, this.screen.h -8, 384, Color.get(0, 0, 0, 0), 0);
				}
				//player.activeItem.renderInventory(screen, 10 * 8, screen.h - 8); // This is the old one...
				player.activeItem.renderInventory(screen, 0, screen.h - 8);
			}
		}
		
		if (menu != null) {
			menu.render(screen);
		}
	}

	private void renderFocusNagger() {
		String msg = "GAME PAUSED!";
		int xx = (WIDTH - msg.length() * 8) / 2;
		int yy = (HEIGHT - 8) / 2;
		int w = msg.length();
		int h = 1;

		screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
		screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
		for (int x = 0; x < w; x++) {
			screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		}
		for (int y = 0; y < h; y++) {
			screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		}

		if ((tickCount / 20) % 2 == 0) {
			Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
		} else {
			Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
		}
	}

	public void scheduleLevelChange(int dir) {
		pendingLevelChange = dir;
	}

	public void won() {
		wonTimer = 60 * 3;
		hasWon = true;
	}

	/**
	 * Called after loading a saved game.
	 * 
	 * This method is responsible for bringing a newly de-serialized game
	 * back to life.
	 */
	public void loadGame()
	{
		this.initGraphics();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException
	{
		this.colors = (int[])in.readObject();
		this.currentLevel = in.readInt();
		this.gameTime = in.readInt();
		this.hasWon = in.readBoolean();
		this.level = (Level)in.readObject();
		this.levels = (Level[])in.readObject();
		this.lightScreen = (Screen)in.readObject();
		//this.menu = (Menu)in.readObject();
		this.pendingLevelChange = in.readInt();
		//this.pixels = (int[])in.readObject(); // generated in initGraphics()
		this.player = (Player)in.readObject();
		this.playerDeadTime = in.readInt();
		this.tickCount = in.readInt();
		this.wonTimer = in.readInt();
		this.running = in.readBoolean();
		this.setup = (GameSetup)in.readObject();
		this.screen = (Screen)in.readObject();
		
		this.player.setGame(this);
		this.player.setInput(this.input);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(this.colors);
		out.writeInt(this.currentLevel);
		out.writeInt(this.gameTime);
		out.writeBoolean(this.hasWon);
		out.writeObject(this.level);
		out.writeObject(this.levels);
		out.writeObject(this.lightScreen);
		//out.writeObject(this.menu);
		out.writeInt(this.pendingLevelChange);
		//out.writeObject(this.pixels); // generated in initGraphics()
		out.writeObject(this.player);
		out.writeInt(this.playerDeadTime);
		out.writeInt(this.tickCount);
		out.writeInt(this.wonTimer);
		out.writeBoolean(this.running);
		out.writeObject(this.setup);
		out.writeObject(this.screen);
	}
}