package com.fdoom;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {
	public class Key {
		public int presses, absorbs;
		public boolean down, clicked;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean pressed) {
			if (pressed != down) {
				down = pressed;
			}
			if (pressed) {
				presses++;
			}
		}

		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				clicked = true;
			} else {
				clicked = false;
			}
		}
	}

	public List<Key> keys = new ArrayList<Key>();

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key menu = new Key();
	public Key save = new Key();
	public Key load = new Key();
	public Key escape = new Key();
	public Key quick = new Key();
	public Key map = new Key();
	public Key a = new Key();
	public Key b = new Key();
	public Key c = new Key();
	public Key d = new Key();
	public Key e = new Key();
	public Key f = new Key();
	public Key g = new Key();
	public Key h = new Key();
	public Key i = new Key();
	public Key j = new Key();
	public Key k = new Key();
	public Key l = new Key();
	public Key m = new Key();
	public Key n = new Key();
	public Key o = new Key();
	public Key p = new Key();
	public Key q = new Key();
	public Key r = new Key();
	public Key s = new Key();
	public Key t = new Key();
	public Key u = new Key();
	public Key v = new Key();
	public Key w = new Key();
	public Key x = new Key();
	public Key y = new Key();
	public Key z = new Key();
    public Key a1 = new Key();
    public Key a2 = new Key();
    public Key a3 = new Key();
    public Key a4 = new Key();
    public Key a5 = new Key();
    public Key a6 = new Key();
    public Key a7 = new Key();
    public Key a8 = new Key();
    public Key a9 = new Key();
    public Key a0 = new Key();
    public Key f2 = new Key();
    public Key f3 = new Key();
    public Key backspace = new Key();
    public Key space = new Key();
    public Key enter = new Key();
    public Key pause = new Key();
	
	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).down = false;
		}
	}

	public void tick() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}

	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	public void keyPressed(KeyEvent ke) {
		toggle(ke, true);
	}

	public void keyReleased(KeyEvent ke) {
		toggle(ke, false);
	}

	private void toggle(KeyEvent ke, boolean pressed) {
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) right.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_A) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) right.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_UP) up.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(pressed);

		if (ke.getKeyCode() == KeyEvent.VK_TAB) menu.toggle(pressed);
		//if (ke.getKeyCode() == KeyEvent.VK_ALT) menu.toggle(pressed); // THIS KEY SUCKS!
		if (ke.getKeyCode() == KeyEvent.VK_ALT_GRAPH) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_CONTROL) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_INSERT) attack.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) menu.toggle(pressed);

		if (ke.getKeyCode() == KeyEvent.VK_X) menu.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_C) attack.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_S) save.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F9) load.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) escape.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_M) map.toggle(pressed);
		
		if (ke.getKeyCode() == KeyEvent.VK_A) a.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_B) b.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_C) c.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_D) d.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_E) e.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_F) f.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_G) g.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_H) h.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_I) i.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_J) j.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_K) k.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_L) l.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_M) m.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_N) n.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_O) o.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_P) p.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Q) q.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_R) r.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_S) s.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_T) t.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_U) u.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_V) v.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_W) w.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_X) x.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Y) y.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_Z) z.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_0) a0.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_1) a1.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_2) a2.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_3) a3.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_4) a4.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_5) a5.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_6) a6.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_7) a7.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_8) a8.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_9) a9.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) backspace.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_SPACE) space.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) pause.toggle(pressed);
		if (ke.getKeyCode() == KeyEvent.VK_ENTER) enter.toggle(pressed);
	}

	public void keyTyped(KeyEvent ke) {
	}
}
