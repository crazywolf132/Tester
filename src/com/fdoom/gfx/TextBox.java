package com.fdoom.gfx;

import com.fdoom.gfx.Color;
import com.fdoom.gfx.Screen;
import com.fdoom.gfx.Font;
import java.awt.event.KeyEvent;

public class TextBox
{
    private String text;
    private boolean enabled;
    private boolean onlyRead;
    public final int x0;
    public final int y0;
    public final int x1;
    public final int y1;
    
    public TextBox(final String s, final int x, final int y, final int w) {
        this.x0 = x;
        this.y0 = y;
        this.x1 = w;
        this.y1 = 20;
        this.enabled = false;
        this.onlyRead = false;
        this.setText(s);
    }
    
    public TextBox(final String s, final int x, final int y, final int w, final boolean flag) {
        this.x0 = x;
        this.y0 = y;
        this.x1 = w;
        this.y1 = 20;
        this.enabled = false;
        this.onlyRead = flag;
        this.setText(s);
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setEnabled(final boolean flag) {
        if (!this.onlyRead) {
            this.enabled = flag;
        }
        else {
            this.enabled = false;
        }
    }
    
    public boolean getEnabled() {
        return this.enabled && !this.onlyRead;
    }
    
    public void Write(final int key) {
        if (KeyEvent.getKeyText(key).length() == 1 && Font.chars.indexOf(KeyEvent.getKeyText(key)) >= 0 && this.text.length() * 4 + 8 < this.x1 / 2) {
            final String s = KeyEvent.getKeyText(key);
            this.text = String.valueOf(this.text) + s;
        }
        else if (key == 32 && this.text.length() * 4 + 8 < this.x1 / 2) {
            this.text = String.valueOf(this.text) + " ";
        }
        else if (key == 8) {
            String s = "";
            for (int i = 0; i < this.text.length() - 1; ++i) {
                s = String.valueOf(s) + String.valueOf(this.text.charAt(i));
            }
            this.text = s;
        }
    }
    
    public void render(final Screen screen) {
        final int col1 = 111;
        final int col2 = 0;
        final int col3 = 222;
        for (int j = this.y0 / 8; j <= this.y0 / 8 + this.y1 / 8; ++j) {
            for (int i = this.x0 / 8; i <= this.x0 / 8 + this.x1 / 8; ++i) {
                if (i == this.x0 / 8 && j == this.y0 / 8) {
                    screen.render(i * 8, j * 8, 416, Color.get(-1, col1, col2, col3), 0);
                }
                else if (i == this.x0 / 8 + this.x1 / 8 && j == this.y0 / 8) {
                    screen.render(i * 8, j * 8, 416, Color.get(-1, col1, col2, col3), 1);
                }
                else if (i == this.x0 / 8 && j == this.y0 / 8 + this.y1 / 8) {
                    screen.render(i * 8, j * 8, 416, Color.get(-1, col1, col2, col3), 2);
                }
                else if (i == this.x0 / 8 + this.x1 / 8 && j == this.y0 / 8 + this.y1 / 8) {
                    screen.render(i * 8, j * 8, 416, Color.get(-1, col1, col2, col3), 3);
                }
                else if (j == this.y0 / 8) {
                    screen.render(i * 8, j * 8, 417, Color.get(-1, col1, col2, col3), 0);
                }
                else if (j == this.y0 / 8 + this.y1 / 8) {
                    screen.render(i * 8, j * 8, 417, Color.get(-1, col1, col2, col3), 2);
                }
                else if (i == this.x0 / 8) {
                    screen.render(i * 8, j * 8, 418, Color.get(-1, col1, col2, col3), 0);
                }
                else if (i == this.x0 / 8 + this.x1 / 8) {
                    screen.render(i * 8, j * 8, 418, Color.get(-1, col1, col2, col3), 1);
                }
                else {
                    screen.render(i * 8, j * 8, 418, Color.get(col2, col2, col2, col2), 1);
                }
            }
        }
        Font.draw(this.text, screen, this.x0 + this.x1 / 2 - this.text.length() * 4, this.y0 / 8 * 8 + this.y1 / 8 * 8 / 2, Color.get(col2, col2, col2, 555));
    }
    
    public void setText(final String s) {
        this.text = "";
        for (int j = 0; j < s.length(); ++j) {
            if (Font.chars.indexOf(s.charAt(j)) >= 0 && this.text.length() * 4 + 8 < this.x1 / 2) {
                this.text = String.valueOf(this.text) + String.valueOf(s.charAt(j));
            }
        }
    }
}