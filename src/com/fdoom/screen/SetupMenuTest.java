package com.fdoom.screen;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.GameSetup;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.SpriteSheet;
import com.fdoom.level.Level;
import com.fdoom.level.tile.Tile;
import com.fdoom.sound.Sound;
import com.fdoom.utils.OptionFile;

public class SetupMenuTest extends Menu {
	private Menu parent;
	
	private int selected = 0;
	private GameSetup setup;
	
	public static int DEFAULT_TEXT_COLOR = Color.get(-1, 555, 555, 555);
	public static int DEFAULT_TITLE_COLOR = Color.get(112, 445, 445, 445);
	public static int DEFAULT_BACKGROUND_COLOR = Color.get(112, 112, 112, 112);
	public static int DEFAULT_BORDER_COLOR = Color.get(-1, 2, 112, 445);
	
	boolean onoff;
	
	public SetupMenuTest(Menu parent) {
		this.parent = parent;
		
		this.setup = GameContainer.getInstance().getSetup();
	}

	public void tick() {
		onoff = false;
		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;
		if (selected < 0) {
			selected = 0;
		}
		if (selected > 0) {
			selected = 0;
		}
		if (input.menu.clicked) {
			game.setMenu(parent);
		}
		if (input.attack.clicked) {
			// Fog of war
			if (selected == 0) {
				Sound.craft.play();
				OptionFile.readOpt();
				if (OptionFile.Fog.equals("1")){
					OptionFile.Fog = "0";
				}else{
					OptionFile.Fog = "1";
				}
				OptionFile.writeOpt();
			}
		}
	}

	public void render(Screen screen) {
		//screen.clear(0);
		Font.renderFrame(screen, "Game Options", 4, 1, 32, 20,
				DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR,
				DEFAULT_TITLE_COLOR);

		int marginX = 50;
		//Font.draw("Setup game", screen, marginX + 4 * 8 + 4, 1 * 8, Color.get(0, 555, 555, 555));
		Font.draw("C change, X back", screen, marginX - 10, screen.h - 45, DEFAULT_TITLE_COLOR);

		int yo = 30;
		int checkboxColorTrue = Color.get(0, -1, 353, 454);
		int checkboxColorFalse = Color.get(0, -1, 533, 544);
		int optionColorActive = Color.get(-1, 555, 555, 555);
		int optionColorInactive = Color.get(-1, 333, 333, 333);
		
		// Fog of war
		OptionFile.readOpt();
		if (OptionFile.FinalFog.equals("1")){
			screen.render(marginX, yo, 0 + 1 * 32, checkboxColorTrue, 0);
		}else{
			screen.render(marginX, yo, 0 + 1 * 32, checkboxColorFalse, 0);
		}
		
		Font.draw("Disable fog of war", screen, marginX + 15, yo, selected == 0 ? optionColorActive : optionColorInactive);
	}
}
