package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.LinkedList;
import java.util.Random;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;
import main.game.Handler;

public class Tank extends Object {
	
	Texture tankTex = Game.getTexture();
	
	private Handler handler;
	String colorType;
	
	PowerUp activePowerUpOnThisTank = null;
	
	// true = left, false = right
	private boolean leftOrRight, shieldEnabled = false;

	private boolean collisionTW = false, collisionTB = false, collisionTT = false;
	private float shootingRate = 5f, tankSpdXY = 3;
	Random randNumGen = new Random();
	
	public Tank(float x, float y, Handler handler, String colorType, boolean leftOrRight, ID id) {
		super(x, y, id);
		typeNum = getTankTypeNum(colorType);
		this.leftOrRight = leftOrRight;
		this.handler = handler;
		this.colorType = colorType;
		setSize(48, 48);
	}

	public void tick(LinkedList<Object> object) {
		
		// get previous position
		float prevPosX = posX, prevPosY = posY;
		
		tankMovement();
		
		checkCollision(handler.o);
		wallTankCollision(prevPosX, prevPosY);
		
	}

	public void render(Graphics g) {
		AffineTransform objRotate = null;
		// create a 2D graphic for the tank being able to turn
		Graphics2D g2d = (Graphics2D)g;
		if (!collisionTW) {
			objRotate = g2d.getTransform();
			// Make the tank can be rotated visually
			g2d.rotate(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		}
		// Display tank images
		displayTankImage(g2d);
		if (!collisionTW) {
			g2d.setTransform(objRotate);
		}
		g2d.draw(getHitbox());		
	}
	
	public void displayTankImage(Graphics2D g2d) {
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
	}
	
	public void tankMovement() {
		// Logics of the tanks position
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
	}
	
	public void wallTankCollision(float prevPosX, float prevPosY) {
		if (collisionTW){
			posX = prevPosX;
			posY = prevPosY;
			collisionTW = false;
		}
	}
	public boolean checkCollision(LinkedList<Object> object) {
		
		for (int i = 0; i < object.size(); i++) {
			Object tempObject = object.get(i);
			Area tankBounds = getHitbox();
			if (tempObject.getID() == ID.Wall) {
			Area wallBounds = ((Wall)tempObject).getHitbox();
			tankBounds.intersect(wallBounds);
				if (!tankBounds.isEmpty()) {
					collisionTW = true;								
				}
				
			} else if (tempObject.getID() == ID.Bullet){
				Bullet bullet = (Bullet)tempObject;
				Area bulletBounds = bullet.getHitbox();
				tankBounds.intersect(bulletBounds);
				if (!tankBounds.isEmpty()) {
					int tickCountRef;
					if (tankSpdXY == 4.5) {
						tickCountRef = 9;
					} else if (tankSpdXY == 1.5) {
						tickCountRef = 3;
					} else {
						tickCountRef = 6;
					}
					if ( (bullet.getTickCount() > tickCountRef) || (bullet.isCollidedWithWall()) ) {
						if (!shieldEnabled) {
							collisionTB = true;
							spdX = 0;
							spdY = 0;
						} else {
							shieldEnabled = false;
						}
						handler.removeObj(bullet);
					}
				}	
			} else if (tempObject.getID() == ID.PowerUp) {
				PowerUp powerup = (PowerUp)tempObject;
				Area powerupBounds = powerup.getHitbox();
				tankBounds.intersect(powerupBounds);
				if (!tankBounds.isEmpty()) {
					if (!powerup.isFindingPosition() && !powerup.isTaken()) {
						System.out.println("powerup got: " +powerup.getClass());
						if (activePowerUpOnThisTank == null) {
							powerup.enablePowerUp(this);
							handler.removeObj(tempObject);
						} else {
							activePowerUpOnThisTank = powerup;
						}
					}
				}
			} else if (tempObject.getID() == ID.Tank) {
				Tank otherTank = (Tank)tempObject;
				Area otherTankBounds = otherTank.getHitbox();
				tankBounds.intersect(otherTankBounds);
				if (!tankBounds.isEmpty())
					if (otherTank.getLeftOrRight() != getLeftOrRight()) {
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
	
	public void firingBullets(Handler handler) {
		float bulletSpdX, bulletSpdY;
		if (leftOrRight == true) {
			bulletSpdX = 9;
			bulletSpdY = -9;
		} else {
			bulletSpdX = -9;
			bulletSpdY = 9;
		}
		handler.addObj(new Bullet(posX + 20, posY + 20,	bulletSpdX, bulletSpdY,
				angle, handler, leftOrRight, ID.Bullet));
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
