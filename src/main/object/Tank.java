package main.object;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;
import main.game.Handler;

public class Tank extends Object {
	
	private float w = 48, h = 48;
	
	Texture tankTex = Game.getTexture();
	
	private Handler handler;
	
	// true = left, false = right
	boolean leftOrRight;
	
	public Tank(float x, float y, Handler handler, String colorType, boolean leftOrRight, ID id) {
		super(x, y, id);
		typeNum = getTankTypeNum(colorType);
		this.leftOrRight = leftOrRight;
		this.handler = handler;
	}

	public void tick(LinkedList<Object> object) {
		// Logics of the tanks position
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
		collision(object);
	}

	public void render(Graphics g) {
		
		// create a 2D graphic for the tank being able to turn
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform objRotate = g2d.getTransform();
		// rotate the tank about the centre according to the magnitude of the angle
		g2d.rotate(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		
		// Load tank images
		if (typeNum != 0) {
			if (leftOrRight == true) {
				// Loading image for left tank
				g2d.drawImage(tankTex.tank[typeNum-1], (int)posX, (int)posY, null);
			} else {
				// Loading image for right tank
				g2d.drawImage(tankTex.tank[typeNum+3], (int)posX, (int)posY, null);
			}
		} else {
			// display error if the tank image cannot successfully loaded.
			System.out.println("Error: Tank image cannot load correctly.");
		}
		g2d.setTransform(objRotate);
		
		g.setColor(Color.RED);
		
		g2d.draw(getBoundsLeft());
		
		g2d.draw(getBoundsRight());
		
		g2d.draw(getBoundsTop());
		
		g2d.draw(getBoundsDown());
	}
	
	private void collision(LinkedList<Object> object) {
		
		for (int i = 0; i < handler.o.size(); i++) {
			Object tempObject = handler.o.get(i);
			
			if (tempObject.getID() == ID.Wall) {
				if (getBoundsDown().intersects(tempObject.getBounds())) {
					spdY = 0;
					spdX = 0;
				}
				if (getBoundsTop().intersects(tempObject.getBounds())) {
					spdY = 0;
					spdX = 0;
				}
				if (getBoundsLeft().intersects(tempObject.getBounds())) {
					spdY = 0;
					spdX = 0;
				}
				if (getBoundsRight().intersects(tempObject.getBounds())) {
					spdY = 0;
					spdX = 0;
				}
			}
		}
		
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)posX, (int)posY, (int)5, (int)h - 10);
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle((int)(posX + w - 5), (int)posY + 5, (int)5, (int)h - 10);
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)(posX + (w/2) - (w/4)), (int)posY, (int)w/2, (int)h/2);
	}
	
	public Rectangle getBoundsDown() {
		return new Rectangle((int)(posX + (w/2) - (w/4)), (int) (posY + h/2), (int)w/2, (int)h/2);
	}
	
	public int getTankTypeNum(String colorType) {
		colorType = colorType.toUpperCase();
		switch(colorType) {
			case "BLACK": return 1; 
			case "BLUE" : return 2;
			case "BROWN": return 3;
			case "GREEN"  : return 4;
			
			case "1"	: return 1;
			case "2"	: return 2;
			case "3"	: return 3;
			case "4"	: return 4;
			default: return 0;
		}
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
