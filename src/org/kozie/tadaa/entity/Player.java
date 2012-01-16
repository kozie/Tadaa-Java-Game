package org.kozie.tadaa.entity;

import org.kozie.tadaa.Game;
import org.kozie.tadaa.gfx.Color;

public class Player extends Entity {

	boolean walking = false;
	boolean jumping = false;
	boolean falling = false;
	boolean toLeft = false;

	public Player(Game game) {
		super(8, 8, game);
		this.x = 35;
		this.y = 35;
	}

	public void render() {

		int tile = 0;
		int bit = 0;
		int colors = Color.get(-1, 20, 50, 555);
		
		if (toLeft) bit = 1;

		if (!walking && !jumping && !falling) {
			
			if ((game.tickCount / 50) % 2 != 0) tile = 1;
		} else if (walking) {
			
			tile = 2;
			if ((game.tickCount / 5) % 2 == 0) tile = 3;
			if ((game.tickCount / 10) % 2 == 0) tile = 4;
		}

		game.screen.render(x, y, tile, colors, bit);
	}

	public void tick() {
		walking = false;

		if (game.input.left.pressed) {
			x--;
			toLeft = true;
			walking = true;
		}

		if (game.input.right.pressed) {
			x++;
			toLeft = false;
			walking = true;
		}
	}
}