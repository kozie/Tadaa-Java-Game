package org.kozie.tadaa.gfx;

public class Color {

	public static int get(int a, int b, int c, int d) {
		return (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a));
	}
	
	public static int get(int i) {
		if (i < 0) return 255;
		
		int r = i / 100 % 10;
		int g = i / 10 % 10;
		int b = i % 10;
		
		return r * 36 + g * 6 + b;
	}
	
}