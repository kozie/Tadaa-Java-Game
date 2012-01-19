package org.kozie.tadaa.entity;

import org.kozie.tadaa.Game;
import org.kozie.tadaa.gfx.Color;

public class Player extends Entity {

	public boolean walking = false;
	public boolean jumping = false;
	public boolean falling = false;
	public int facing = 0;
	
	public int step = 100;
	public long updateTimer;

	public Player(Game game) {
		super(8, 8, game);
		this.x = 35;
		this.y = (Game.HEIGHT - 13);
	}

	public void render() {

		int tile = 0;
		int bit = 0;
		int colors = Color.get(-1, 20, 50, 555);
		
		if (facing < 0) bit = 1;

		if (step == 100 && !walking && !jumping && !falling) { 
			
			if ((game.tickCount / 50) % 2 != 0) tile = 1;
		} else if (walking || step < 100) {
			
			tile = 2;
			if ((game.tickCount / 5) % 2 == 0) tile = 3;
			if ((game.tickCount / 10) % 2 == 0) tile = 4;
		}

		game.screen.render(x, y, tile, colors, bit);
	}

	public void tick() {
		
		if (game.input.left.pressed) {
			walking = true;
			facing = -1;
		} else if (game.input.right.pressed) {
			walking = true;
			facing = 1;
		} else {
			walking = false;
		}
		
		if (step < 100 || walking) {
			move(facing, walking);
		} else {
			updateTimer = 0;
		}
		
		if (x > game.screen.width) {
			x = 0;
		} else if(x < 0) {
			x = game.screen.width;
		}
	}
	
	public void move(int facing, boolean walking) {
		
		this.walking = walking;
		
		if (updateTimer < 1) updateTimer = System.currentTimeMillis();
		
		if (System.currentTimeMillis() - updateTimer >= step) {
			
			if (walking && step > 0) {
				step -= 10;
			} else if (!walking && step < 100){
				step += 5;
			}
			
			updateTimer += step;
			
			if (facing > 0) {
				x += 1;
			} else {
				x -= 1;
			}
		}
	}
}