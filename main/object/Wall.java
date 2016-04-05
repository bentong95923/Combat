package main.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;

public class Wall extends Object  {

	public Wall(float x, float y, ID id) {
		super(x, y, id);
		
	}

	public void tick(LinkedList<Object> object) {
		
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.white);
		g.drawRect((int)posX, (int)posY, 32, 32);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 32, 32);
	}
	
}
