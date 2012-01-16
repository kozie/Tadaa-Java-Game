// 58
package org.kozie.tadaa;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.kozie.tadaa.entity.Player;
import org.kozie.tadaa.gfx.Color;
import org.kozie.tadaa.gfx.Screen;
import org.kozie.tadaa.gfx.SpriteSheet;
import org.kozie.tadaa.gfx.Text;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 6764865459516958484L;

	public static final String NAME = "Tadaa the game";
	public static final int WIDTH = 210;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

	public Screen screen;
	public int[] pallette = new int[256];

	public InputListener input = new InputListener(this);
	public int tickCount;
	private boolean running = false;

	public Player player = new Player(this);

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void init() {

		int pIndex = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = r * 255 / 5;
					int gg = g * 255 / 5;
					int bb = b * 255 / 5;
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;

					rr = (rr + mid) / 2;
					gg = (gg + mid) / 2;
					bb = (bb + mid) / 2;

					pallette[pIndex++] = rr << 16 | gg << 8 | bb;
				}
			}
		}

		try {
			screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/sprites.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		init();

		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60.0;

		int frames = 0;
		int ticks = 0;
		long lastTimer = System.currentTimeMillis();

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

			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			}

			if (System.currentTimeMillis() - lastTimer > 1000) {
				lastTimer += 1000;
				System.out.printf("%d ticks, %d frames\n", ticks, frames);
				frames = 0;
				ticks = 0;
			}
		}
	}

	public void tick() {
		tickCount++;

		if (!hasFocus()) {
			input.releaseAll();
		} else {
			player.tick();
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}

		// for (int i = 0; i < pixels.length; i++) {
		// pixels[i] = 0xFF;
		// }

		screen.clear();
		player.render();

		if (!hasFocus()) renderFocusNote();

		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int col = screen.pixels[x + y * screen.width];
				if (col < 255) pixels[x + y * WIDTH] = pallette[col];
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		int w = WIDTH * SCALE;
		int h = HEIGHT * SCALE;
		int offsetX = (getWidth() - w) / 2;
		int offsetY = (getHeight() - h) / 2;
		g.drawImage(image, offsetX, offsetY, w, h, null);
		g.dispose();
		bs.show();
	}

	public void renderFocusNote() {

		// Blink text
		int color = -1;
		if ((tickCount / 40) % 2 == 0) color = 555;

		String text = "Click to Focus";
		int xs = (WIDTH - text.length() * 8) / 2;
		int ys = (HEIGHT - 8) / 2;

		Text.draw(text, xs, ys, screen, Color.get(-1, -1, -1, color));
	}

	public static void main(String[] args) {

		Game game = new Game();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setBackground(java.awt.Color.black);
		game.setForeground(java.awt.Color.white);

		JFrame frame = new JFrame(NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		game.start();
	}
}