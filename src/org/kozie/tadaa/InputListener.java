package org.kozie.tadaa;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputListener implements KeyListener {

	public class Key {

		public boolean pressed = false;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean flag) {
			if (flag != pressed) {
				pressed = flag;
			}
		}
	}

	public List<Key> keys = new ArrayList<Key>();
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();

	public InputListener(Game game) {
		game.addKeyListener(this);
	}

	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).toggle(false);
		}
	}

	public void keyPressed(KeyEvent e) {
		toggle(e, true);
	}

	public void keyReleased(KeyEvent e) {
		toggle(e, false);
	}

	private void toggle(KeyEvent e, boolean flag) {
		if (e.getKeyCode() == KeyEvent.VK_UP) up.toggle(flag);
		if (e.getKeyCode() == KeyEvent.VK_DOWN) down.toggle(flag);
		if (e.getKeyCode() == KeyEvent.VK_LEFT) left.toggle(flag);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) right.toggle(flag);
	}

	public void keyTyped(KeyEvent e) {
	}

}
