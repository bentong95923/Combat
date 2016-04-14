package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.Random;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;
import main.game.Handler;
import main.object.powerup.Speed;

public class Tank extends Object {
	
	private float w = 48, h = 48;
	
	Texture tankTex = Game.getTexture();
	
	private Handler handler;
	String colorType;
	
	// true = left, false = right
	boolean leftOrRight, shieldAvailable = false;
	
	private boolean CollisionTW = false;
	private boolean CollisionTB = false;
	

	Random randNumGen = new Random();
	
	public Tank(float x, float y, Handler handler, String colorType, boolean leftOrRight, ID id) {
		super(x, y, id);
		typeNum = getTankTypeNum(colorType);
		this.leftOrRight = leftOrRight;
		this.handler = handler;
		this.colorType = colorType;
	}

	public void tick(LinkedList<Object> object) {
		
		// get previous position
		float prevPosX = posX;
		float prevPosY = posY;
		
		// Logics of the tanks position
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
		
		collision(handler.o);
		
		if(CollisionTW){
			posX = prevPosX;
			posY = prevPosY;
			CollisionTW = false;
		}
		
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
		
	}
	

	public void collision(LinkedList<Object> object) {
		
		for (int i = 0; i < object.size(); i++) {
			Object tempObject = object.get(i);
			Shape rectangle = (Shape)getBounds();
			if (tempObject.getID() == ID.Wall) {
			
				if (rectangle.intersects(tempObject.getBounds())) {
					CollisionTW = true;								
				}
				
			}else if (tempObject.getID() == ID.Bullet){
				if (rectangle.intersects(tempObject.getBounds())) {
					if (((Bullet)tempObject).getTickCount() > 14) {
						CollisionTB = true;
						handler.removeObj(tempObject);
					}
				}	
			} else if(tempObject.getID() == ID.Speed) {
				((Speed)tempObject).setPowerUp(this);
			} else if(tempObject.getID() == ID.Bullet) {
				if (((Bullet)tempObject).getTickCount() > 14) {
					handler.removeObj(tempObject);
					handler.removeObj(this);
				}
			}
		}
	}
	
	public Rectangle getBounds() {
		Rectangle rectangle = new Rectangle((int)posX, (int)posY, (int)w, (int)h);
		return rectangle;
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

	public void setShieldAvailable() {
		shieldAvailable = true;
	}
	
	public void setShieldDisable() {
		shieldAvailable = false;
	}
	
	public boolean getCollisionTW() {
		return CollisionTW;
	}
	
	public void resetCollisionTB() {
		CollisionTB = false;
	}
	
	public boolean getCollisionTB() {
		return CollisionTB;
	}
	
}
