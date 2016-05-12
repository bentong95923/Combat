package main.object;

import java.awt.Color;
import java.awt.Font;
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
	private String direction = "";
	private boolean collisionTW = false, collisionTB = false, collisionTT = false;
	private float shootingRate = 5f, tankSpdXY = 3, prevPosX = 0, prevPosY = 0;
	private int gms;
	Random randNumGen = new Random();
	
	public Tank(float x, float y, Handler handler, String colorType, boolean leftOrRight, ID id) {
		super(x, y, id);
		typeNum = getTankTypeNum(colorType);
		this.leftOrRight = leftOrRight;
		this.handler = handler;
		this.colorType = colorType;
		setSize(48, 48);
		this.gms = handler.getGMS();
	}

	public void tick() {
		
		// get previous position
		prevPosX = posX;
		prevPosY = posY;
		
		tankMovement();
		resetDirection();
		checkCollision(handler.o);
		wallTankCollision();
		tankAngle();
		
		powerUpTick();
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
		drawPlayerTextOnTank(g2d);
		//g2d.draw(getHitbox());
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
	
	public void drawPlayerTextOnTank(Graphics2D g2d) {
		String p1Text = "", p2Text = "";
		if (leftOrRight == true) {
			p1Text = "P1";
		} else {
			switch(gms) {
			case 0: p2Text = "COM"; break;
			case 1: p2Text = "P2"; break;
			case 2: p2Text = ""; break;
			}
		}
		Font HUD_text = new Font("Courier", Font.BOLD, 10);
		g2d.setFont(HUD_text);
		g2d.setColor(new Color(255, 0, 0));
		g2d.drawString(p1Text, posX-12, posY-0);
		g2d.drawString(p2Text, posX+w, posY-0);
		// the following code can be removed in the final
		g2d.setColor(Color.black);
	}
	
	public void powerUpTick() {
		if (activePowerUpOnThisTank != null) {
			activePowerUpOnThisTank.tick();
		}
	}
	
	public void tankMovement() {
		// Logics of the tanks position
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
	}
	
	public void tankAngle() {
		if (Math.abs(angle) == 360) {
			angle = 0;
		}
	}
	
	public void wallTankCollision() {
		if (collisionTW){
			posX = prevPosX;
			posY = prevPosY;
			collisionTW = false;
		}
	}
	
	public void resetDirection() {
		if (spdX == 0 && spdY == 0) {
			direction = "";
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
						if (activePowerUpOnThisTank != null) {
							activePowerUpOnThisTank.disablePowerUp(this);							
						}
						powerup.setTaken(true);
						activePowerUpOnThisTank = powerup;
						powerup.enablePowerUp(this);	
						handler.removeObj(powerup);
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
	
	public float getTankSpdXY() {
		return this.tankSpdXY;
	}
	
	public void setTankSpdXY(float speed, String upOrDown) {
		this.tankSpdXY = speed;
		this.direction = upOrDown;
		if (getLeftOrRight() == true) {
			if (upOrDown.toLowerCase() == "up") {
				spdX = speed;
				spdY = -speed;
			} else if (upOrDown.toLowerCase() == "down") {
				spdX = -speed;
				spdY = speed;
			}
		} else {
			if (upOrDown.toLowerCase() == "up") {
				spdX = -speed;
				spdY = speed;
			} else if (upOrDown.toLowerCase() == "down") {
				spdX = speed;
				spdY = -speed;
			}
		}
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
	
	public void removePowerUpFromThisTank() {
		activePowerUpOnThisTank = null;
	}
	
	public void resetTank() {
		this.angle = 0;
		this.spdX = 0;
		this.spdY = 0;
		if (activePowerUpOnThisTank != null) {
			this.activePowerUpOnThisTank.disablePowerUp(this);
		}
	}
	
	public float getPrevPosX() {
		return this.prevPosX;
	}
	
	public float getPrevPosY() {
		return this.prevPosY;
	}
	public String getDirection() {
		return this.direction;
	}
	
	public PowerUp getPowerUpOnThisTank() {
		return this.activePowerUpOnThisTank;
	}
	
}
