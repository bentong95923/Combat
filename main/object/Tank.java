package main.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;

public class Tank extends Object {
	
	private float w = 48, h = 48;
	public Tank(float x, float y, ID id) {
		super(x, y, id);		
	}

	public void tick(LinkedList<Object> object) {			
		angle += angularSpd;
		
		// Logics of the tanks position
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
	}

	public void render(Graphics g) {
		
		g.setColor(Color.blue);
		
		// create a 2D graphic for the tank being able to turn
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform objRotate = g2d.getTransform();
		g2d.rotate(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		g.fillRect((int)posX, (int)posY, (int)w, (int)h);
		g2d.setTransform(objRotate);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, (int)w, (int)h);
	}
}
