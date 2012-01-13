package org.kozie.tadaa.gfx;

public final class Text {

	public final static String charMap = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + "0123456789.,!?'\"-+=/\\%()<>:;    ";

	public static void draw(String text, int x, int y, Screen screen, int colors) {

		text = text.toUpperCase();
		for (int i = 0; i < text.length(); i++) {
			
			int index = charMap.indexOf(text.charAt(i));
			if (index >= 0) {
				int xx = x + i * 8;
				
				screen.render(xx, y, index + 30 * 32, colors, 0);
			}
		}
	}
}