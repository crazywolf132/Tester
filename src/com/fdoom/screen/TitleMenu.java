package com.fdoom.screen;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import com.fdoom.Data;
import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.level.*;
import com.fdoom.sound.Sound;
import com.fdoom.utils.ClientUtils;
import com.fdoom.utils.Connection;
import com.fdoom.utils.OptionFile;
import com.fdoom.level.Level;
import com.fdoom.level.tile.Tile;

public class TitleMenu extends Menu {
	private int selected = 0;
	public static final int MINIGAME_WIDTH = Game.WIDTH >> 4;
	public static final int MINIGAME_HEIGHT = Game.HEIGHT >> 4;
	private Level miniGame;
	private Screen miniScreen;
	private int tickCount;
	
	public static int DEFAULT_TEXT_COLOR = Color.get(-1, 555, 555, 555);
	public static int DEFAULT_TITLE_COLOR = Color.get(112, 445, 445, 445);
	public static int DEFAULT_BACKGROUND_COLOR = Color.get(112, 112, 112, 112);
	public static int DEFAULT_BORDER_COLOR = Color.get(-1, 2, 112, 445);
	
	private AtomicBoolean miniLoaded;
	
	private static final String[] options = { "Start game", "Load game", "How to play", "Setup", "About", "Quit" };
	
	public TitleMenu() {
		
		Calendar var1 = Calendar.getInstance();
		
		if(var1.get(2) + 1 == 12 && var1.get(5) >= 20 && var1.get(5) <= 26)
		{
			//this.isChristmas = true;
		}
				miniLoaded = new AtomicBoolean();
				miniLoaded.set(false);
				Thread thread = new Thread() {
					public void run() {
						miniGame = new Level(128, 128, 0, null);
						miniGame.trySpawn(10000);
						// simulate some history
						for (int i = 0; i < 1000; i++) {
							miniGame.tick();
						}
						miniLoaded.set(true);
					};
				};
				thread.start();
				
				try {
					miniScreen = new Screen(MINIGAME_WIDTH * 16, MINIGAME_HEIGHT * 16, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
				} catch (IOException e) {
					e.printStackTrace();
					GameContainer.Logger(java.util.logging.Level.SEVERE,"We have an error in TitleMenuTest - Line 78. Please send to Dev!");
				}
	}

	public void tick() {
		
		tickCount++;
		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;
		
		int len = options.length;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.attack.clicked || input.menu.clicked) {
			if (selected == 0) game.setMenu(new NewWorldMenu(this));
			if (selected == 1) game.setMenu(new LoadWorldMenu(this));
			if (selected == 2) game.setMenu(new InstructionsMenu(this));
			if (selected == 3) game.setMenu(new SetupMenu(this));
			if (selected == 4) game.setMenu(new AboutMenu(this));
			if (selected == 5) System.exit(0);
			//if (selected == 6) game.setMenu(new WorldSelectMenuTest(this));
		}
		
		if (miniLoaded.get()) {
			miniGame.tick();
			Tile.tickCount++;
		}
		
	}

	public void render(Screen screen) {
		screen.clear(0);
		int marginX = 10;
		if (miniLoaded.get()) {
			int xScroll = (int)(Math.cos((tickCount / 10000.0) * 2*Math.PI) * (miniGame.w * 8) / 2) + (miniGame.w * 8) / 2 + MINIGAME_WIDTH * 16 / 2;
			int yScroll = (int)(Math.sin((tickCount / 10000.0) * 2*Math.PI) * (miniGame.h * 8) / 2) + (miniGame.h * 8) / 2 + MINIGAME_HEIGHT * 16 / 2;
			miniGame.renderBackground(miniScreen, xScroll, yScroll);
			miniGame.renderSprites(miniScreen, xScroll, yScroll);
			miniScreen.copyRect(screen, 5, Game.HEIGHT - MINIGAME_HEIGHT*16 - 5, MINIGAME_WIDTH * 16, MINIGAME_HEIGHT * 16);
		} 

		Font.renderFrame(screen, "", 8, 6, 28, 17,
				DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR,
				DEFAULT_TITLE_COLOR);
		{ // game title
			int h = 2;
			int w = 14;
			int titleColor = Color.get(-1, 0, Color.rgb(0, 0, 0), Color.rgb(255, 0, 0));
			int xo = (screen.w - w * 8) / 2;
			int yo = 24;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					screen.render(xo + x * 8, yo + y * 8, x + (y + 6) * 32, titleColor, 0);
				}
			}
		}

		// options
		for (int i = 0; i < options.length; i++) {
			String msg = options[i];
			int col = Color.get(-1, 222, 222, 222);
			if (i == selected) {
				msg = "> " + msg + " <";
				col = Color.get(-1, 555, 555, 555);
			}
			Font.draw(msg, screen, (screen.w - msg.length() * 8) / 2, (8 + i) * 12 - 35, col);
		}
	
		
	}
}