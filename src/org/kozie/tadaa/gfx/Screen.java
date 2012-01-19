package org.kozie.tadaa.gfx;

public class Screen {

	public int width, height;
	public int[] pixels;

	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;

	private SpriteSheet sheet;

	public Screen(int width, int height, SpriteSheet sheet) {

		this.sheet = sheet;
		this.width = width;
		this.height = height;

		pixels = new int[width * height];
	}

	public void render(int x, int y, int tile, int colors, int bit) {

		boolean mirrorX = (bit & BIT_MIRROR_X) > 0;
		boolean mirrorY = (bit & BIT_MIRROR_Y) > 0;
		
		int offsetX = tile % 32;
		int offsetY = tile / 32;
		int offset = offsetX * 8 + offsetY * 8 * sheet.width;

		for (int yy = 0; yy < 8; yy++) {
			int ys = yy;
			if (mirrorY) ys = 7 - yy;
			if (y + ys < 0 || y + ys > height) continue;

			for (int xx = 0; xx < 8; xx++) {
				int xs = xx;
				if (mirrorX) xs = 7 - xx;
				
				if (x + xs < 0 || x + xs > width) continue;

				int col = (colors >> (sheet.pixels[offset + xs + ys * sheet.width] * 8)) & 0xFF;
				if (col < 255) pixels[(x + xx) + (y + yy) * width] = col;
			}
		}
	}
	
	public void clear() {
		pixels = new int[width * height];
	}
}