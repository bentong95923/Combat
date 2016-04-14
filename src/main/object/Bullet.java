package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;


public class Bullet extends Object {	

	Texture bulletTex = Game.getTexture();
	
	private float w = 8, h = 8;
	private boolean leftOrRight = false;
	int tickCount = 0;
	
	public Bullet(float x, float y, float spdX, float spdY, float angle, boolean leftOrRight, ID id) {
		super(x, y, id);
		this.spdX = spdX;
		this.spdY = spdY;
		this.angle = angle;		
		this.leftOrRight = leftOrRight;
	}

	public void tick(LinkedList<Object> object) {
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
		tickCount++;
	}

	public void render(Graphics g) {
		// create a 2D graphic for the tank being able to turn
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform objRotate = g2d.getTransform();
		if (leftOrRight == true) {
			// rotate the tank about the centre according to the magnitude of the angle
			g2d.rotate(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		} else {
			g2d.rotate(Math.toRadians(angle+180), posX+w*0.5, posY+h*0.5);
		}
		g2d.drawImage(bulletTex.bullet[0], (int)posX, (int)posY, null);
		g2d.setTransform(objRotate);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 8, 8);
	}
	public int getTickCount() {
		return tickCount;
	}

}
