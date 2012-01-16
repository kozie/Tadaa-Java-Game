package org.kozie.tadaa.entity;

import org.kozie.tadaa.Game;

public abstract class Entity {

	public int x, y;
	public int width, height;
	public Game game;
	
	public Entity(int width, int height, Game game) {
		this.width = width;
		this.height = height;
		this.game = game;
	}
	
	public void render() {
	}
	
	public void tick() {
	}
}