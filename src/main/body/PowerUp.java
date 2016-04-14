package main.body;

import java.awt.Graphics;
import java.util.LinkedList;

import main.game.Game;

public abstract class PowerUp extends Object {
	
	protected Texture powerUpTex = Game.getTexture();
	
	boolean collision = false, taken;
	protected int w = 24, h = 24;
	float posX, posY;
	ID id;
	
	public PowerUp (float posX, float posY, ID id) {
		super(posX, posY, id);
		this.posX = posX;
		this.posY = posY;
		this.id = id;
	}
	
	public abstract void tick(LinkedList<Object> object);
	
	public abstract void render(Graphics g);
}