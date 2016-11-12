package com.fdoom.screen;

import com.fdoom.Game;
import com.fdoom.GameContainer;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Font;
import com.fdoom.gfx.Screen;
import com.fdoom.sound.Sound;
import com.fdoom.utils.OptionFile;

public class PauseMenu extends Menu {
	private int selected = 0;

	public static int DEFAULT_TEXT_COLOR = Color.get(-1, 555, 555, 555);
	public static int DEFAULT_TITLE_COLOR = Color.get(112, 445, 445, 445);
	public static int DEFAULT_BACKGROUND_COLOR = Color.get(112, 112, 112, 112);
	public static int DEFAULT_BORDER_COLOR = Color.get(-1, 2, 112, 445);
	//TODO fix the order of this...
	private static final String[] options = { "Resume", "Save game", "Main Menu", "Setup", "Quit", "options" };

	public PauseMenu() {
	}

	public void tick() {
		if (input.up.clicked) selected--;
		if (input.down.clicked) selected++;

		int len = options.length;
		if (selected < 0) selected += len;
		if (selected >= len) selected -= len;

		if (input.attack.clicked || input.menu.clicked) {
			if (selected == 0) {
				Sound.test.play();
				game.setMenu(null);
			}
			if (selected == 1) {
				Sound.test.play();
				OptionFile.readOpt();
				String worldname = OptionFile.CurrentWorld;
				GameContainer.saveGameTest(worldname);
			}
			if (selected == 2) game.setMenu(new TitleMenu());
			if (selected == 3) game.setMenu(new SetupMenu(this));
			if (selected == 4) System.exit(0);
			if (selected == 5) game.setMenu(new OptionsMenu(true));
		}
	}

	public void render(Screen screen) {
		//Font.renderFrame(screen, "", 4, 1, 32, 20);
		Font.renderFrame(screen, "", 4, 1, 32, 20,
				DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR,
				DEFAULT_TITLE_COLOR);

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

		Font.draw("(Arrow keys,X and C, F5 and F9)", screen, 0, screen.h - 8, Color.get(0, 111, 111, 111));
	}
}