package com.fdoom.screen.update;

import com.fdoom.gfx.Font;
import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.screen.Menu;
import com.fdoom.screen.TitleMenu;
import com.fdoom.screen.update.UpdateMenu;
import com.fdoom.utils.ClientUtils;
import com.fdoom.utils.Connection;
import com.fdoom.InputHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.fdoom.Game;

public class DownloadMenu extends Menu
{
    protected Game game;
    protected InputHandler input;
    private int rdm;
    private int tickc;
    private int tickCount;
    private boolean finished;
    
        
    public DownloadMenu() {
        this.tickc = 0;  
    }
    
    @Override
    public void init(final Game game, final InputHandler input) {
        this.input = input;
        this.game = game;
    }
    
    @Override
    public void tick() {
    	tickCount++;
    	this.finished = false;
    	++this.tickc;
        if (this.tickc >= 100) {
        	if (ClientUtils.isClientUpToDate() == 1)
            {
        		ClientUtils.DownloadUpdates();
    			this.finished = true;
    			check();
            }else{
            	this.game.setMenu(new TitleMenu());
            }
        }
    }
    
    private void check(){
    	if (this.finished){
    		this.game.setMenu(new ForceMenu());
    	}
    }
    
    @Override
    public void render(final Screen screen) {
        int h = 5;
        int w = 46;
        ++this.rdm;
        screen.clear(0);
        for (int y = 3; y < h; ++y) {
            for (int x = 17; x < w; ++x) {
                final int titleColor = Color.get(this.rdm + x * 8, this.rdm + x * 8, this.rdm + x * 8, this.rdm + x * 8);
                screen.render(x * 4, y * 8, 352, titleColor, 0);
            }
        }
        h = game.HEIGHT;
        w = Game.WIDTH;
        ++this.rdm;
        for (int y = 0; y < h; ++y) {
            for (int x = 0; x < w; ++x) {
                final int titleColor = Color.get(0, 0, this.rdm + x * 5 + y * 2, 551);
                screen.render(x * 8, y * 8, 355, titleColor, 0);
            }
        }
        Font.renderFrame(screen, "", 9, 2, 28, 20);
        //ORIGINALS: 2, 2, 28, 2
        h = 20;
        w = 28;
        for (int y = 3; y < h; ++y) {
            for (int x = 10; x < w; ++x) {
                final int titleColor = Color.get(0, 0, 1, 551);
                screen.render(x * 8, y * 8, 355, titleColor, 0);
            }
        }
        final int h2 = 2;
        final int w2 = 16;
        final int titleColor = Color.get(-1, 107, 8, 999);
        final int xo = (screen.w - w2 * 8) / 2;
        final int yo = 24;
        for (int y2 = 0; y2 < h2; ++y2) {
            for (int x2 = 0; x2 < w2; ++x2) {
                screen.render(xo + x2 * 8, yo + y2 * 8, x2 + 14 + (y2 + 6) * 32, titleColor, 0);
            }
        }
        String dots = "";
		for (int i = 0; i < tickCount/40 % 4; i++) {
			dots += ".";
		}
        Font.draw("Downloading"+dots, screen, 100, 60, Color.get(-1, 500, 500, 500));
        //Font.draw("generating world"+dots, screen, 	115 +  0 * 8 + 4, 19 * 8, Color.get(0, 522, 522, 522));
        //Font.draw("CrazyWolf", screen, 93, 60, Color.get(-1, 500, 500, 500));
        //Font.draw("Zelosfan", screen, 125, 76, Color.get(-1, 500, 500, 500));
        //Font.draw("Minicraft: Notch", screen, 87, 140, Color.get(-1, 3, 3, 3));
    }
}
