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
import main.body.PowerUp;
import main.body.Texture;
import main.game.Game;
import main.game.Handler;

public class Tank extends Object {
	
	private float w = 48, h = 48;
	
	Texture tankTex = Game.getTexture();
	
	private Handler handler;
	String colorType;
	
	PowerUp activePowerUpOnThisTank = null;
	
	// true = left, false = right
	private boolean leftOrRight, shieldEnabled = false;

	private boolean collisionTW = false, collisionTB = false, collisionTT = false;
	private float shootingRate = 0.5f, tankSpdXY = 3;
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
		
		checkCollision(handler.o);
		
		if (collisionTW){
			posX = prevPosX;
			posY = prevPosY;
			collisionTW = false;
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
	
	public boolean checkCollision(LinkedList<Object> object) {
		
		for (int i = 0; i < object.size(); i++) {
			Object tempObject = object.get(i);
			Shape tankBounds = (Shape)getBounds();
			if (tempObject.getID() == ID.Wall) {
			
				if (tankBounds.intersects(((Wall)tempObject).getBounds())) {
					collisionTW = true;								
				}
				
			} else if (tempObject.getID() == ID.Bullet){
				if (tankBounds.intersects(((Bullet)tempObject).getBounds())) {
					int tickCountRef;
					if (tankSpdXY == 4.5) {
						tickCountRef = 9;
					} else if (tankSpdXY == 1.5) {
						tickCountRef = 3;
					} else {
						tickCountRef = 6;
					}
					if ( ((Bullet)tempObject).getTickCount() > tickCountRef || ( ((Bullet)tempObject).isCollidedWithWall() ) ) {
						if (!shieldEnabled) {
							collisionTB = true;
							spdX = 0;
							spdY = 0;
						} else {
							shieldEnabled = false;
						}
					}
				}	
			} else if (tempObject.getID() == ID.PowerUp) {
				PowerUp powerup = (PowerUp)tempObject;
				if (tankBounds.intersects(powerup.getBounds()) && !powerup.isFindingPosition() && !powerup.isTaken()) {
					if (activePowerUpOnThisTank == null) {
						powerup.enablePowerUp(this);
						handler.removeObj(tempObject);
					} else {
						activePowerUpOnThisTank = powerup;
					}
				}
			} else if (tempObject.getID() == ID.Tank) {
				if (tankBounds.intersects(((Tank)tempObject).getBounds()) && ((Tank)tempObject).getLeftOrRight() != getLeftOrRight()) {
					collisionTT = true;
				}
			}
		}
		return (collisionTW || collisionTB);
	}
	
	public void resetCollisionBoolean() {
		collisionTW = false;
		collisionTB = false;
	}
	
	public void findNewPosition(LinkedList<Object> object, boolean leftRight) {
		boolean collision = true, tooClose;
		if (leftRight == false) {tooClose = true;} else {tooClose = false;}
		while (collision || tooClose) {
			
			int xrand = randNumGen.nextInt(940) + 30;
			int yrand = randNumGen.nextInt(740) + 30;
			posX = xrand;
			posY = yrand;
			collision = checkCollision(object);
			resetCollisionBoolean();
			tooClose = handler.isTwoTanksTooClose();
		}		
	}
	
	public Rectangle getBounds() {
		Rectangle rectangle = new Rectangle((int)posX, (int)posY, (int)w, (int)h);
		return rectangle;
	}
	
	public Bullet firingBullets(Handler handler) {
		float bulletSpdX, bulletSpdY;
		if (leftOrRight == true) {
			bulletSpdX = 9;
			bulletSpdY = -9;
		} else {
			bulletSpdX = -9;
			bulletSpdY = 9;
		}
		return new Bullet((float)(posX + 20), (float)(posY + 20),
				bulletSpdX, bulletSpdY, angle, handler, leftOrRight, ID.Bullet);
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
	
	public boolean getLeftOrRight() {
		return leftOrRight;
	}
	
	public boolean getCollisionTW() {
		return collisionTW;
	}
	
	public void resetCollisionTB() {
		collisionTB = false;
	}
	
	public void resetCollisionTT() {
		collisionTT = false;
	}
	
	public boolean getCollisionTB() {
		return collisionTB;
	}
	
	public boolean getCollisionTT() {
		return collisionTT;
	}
	
	public void setShootingRate(float shootingRate) {
		this.shootingRate = shootingRate;
	}

	public float getShootingRate() {
		return this.shootingRate;
	}
	
	public float getTankSpdX() {
		if (getLeftOrRight() == true) {
			return this.tankSpdXY;
		} else {
			return (-(this.tankSpdXY));
		}
	}
	
	public float getTankSpdY() {
		if (getLeftOrRight() == true) {
			return (-(this.tankSpdXY));
		} else {
			return this.tankSpdXY;
		}
	}
	
	public void setTankSpdXY(float speed) {
		this.tankSpdXY = speed;
	}
	
	public boolean isShieldEnabled() {
		return shieldEnabled;
	}

	public void setShieldEnabled() {
		this.shieldEnabled = true;
	}
	
	public void setShieldDisabled() {
		this.shieldEnabled = false;
	}
	
}
