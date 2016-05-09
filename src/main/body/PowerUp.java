package main.body;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.game.Game;
import main.object.Tank;
import main.object.Wall;

public abstract class PowerUp extends Object {
	
	protected Texture powerUpTex = Game.getTexture();
			
	protected int powerupType;
	protected boolean collideWithTank = false, collideWithWall = false, collideWithPowerUp = false, findingPosition = false;
	// If removeThis is true then this power up will be removed from handler object.
	protected boolean removeThis = false, taken = false;
	/* initialize the coordinates for the power up.
	* The posX_Gen and posY_Gen are for the power up generated
	* in random positions. We use a double size for the power
	* up bounds so that for the collision detection of power up
	* generation, the power up will not be generated in where the
	* tank cannot access.
	*/
	protected int w = 24, h = 24, w_gen = 48, h_gen = 48;
	float posX, posY;
	ID id;
	
	public PowerUp (float posX, float posY, ID id) {
		super(posX, posY, id);
		this.posX = posX;
		this.posY = posY;
		this.id = id;
	}
	
	public boolean checkCollision(LinkedList<Object> object, PowerUp powerup, boolean lookingForPos) {
		findingPosition = lookingForPos;
		for (int i = 0; i < object.size(); i++) {
			Object tempObject = object.get(i);
			Rectangle rectangle = powerup.getBounds();
			if (tempObject.getID() == ID.Wall) {
				Wall wall = (Wall)tempObject;
				if (rectangle.intersects(wall.getBounds()) || wall.getBounds().intersects(rectangle)) {
					collideWithWall = true;								
				}
				//System.out.println("collide with wall: "+collideWithWall);
			} else if (tempObject.getID() == ID.PowerUp) {
				PowerUp otherpowerup = (PowerUp)tempObject;
				if (rectangle.intersects(otherpowerup.getBounds()) || otherpowerup.getBounds().intersects(rectangle)) {
					collideWithPowerUp = true;
				}
			} else if (tempObject.getID() == ID.Tank) {
				Tank tank = (Tank)tempObject;
				if (rectangle.intersects(tank.getBounds()) || tank.getBounds().intersects(rectangle)) {
					if (!findingPosition) {
						powerup.enablePowerUp((Tank)tempObject);
						taken = true;
					} 
					collideWithTank = true;
				}
			}
		}
		findingPosition = false;
		//System.out.println("collisionPowerUP = " + collideWithPowerUp);
		return (collideWithWall || collideWithPowerUp || collideWithTank);
	}
	
	public void resetCollisionBoolean() {
		collideWithWall = false;
		collideWithPowerUp = false;
		collideWithTank = false;
	}
	
	public Rectangle getBounds() {
		/* Return 48x48 bounds while powerup looking for a correct position to display on screen.
		 * Return 24x24 bounds if the powerup has been displayed on screen
		 */
		if (findingPosition) {
			//System.out.println("using 48*48");
			return new Rectangle((int)this.posX, (int)this.posY, (int)this.w_gen, (int)this.h_gen);
		} else {
			//System.out.println("using 24*24");
			return new Rectangle((int)this.posX, (int)this.posY, (int)this.w, (int)this.h);
		}
	}

	public void setPosX(float x) {
		this.posX = x;
	}
	
	public void setPosY(float y) {
		this.posY = y;
	}
	
	public float getPosX() {
		return posX;
	}
	public float getPosY() {
		return posY;
	}
	
	public abstract void enablePowerUp(Tank tank);
	public abstract void disablePowerUp(Tank tank);
	
	public abstract void setPowerUpType();
	
	public int getPowerUpType() {
		return this.powerupType;
	}
		
	public void tick(LinkedList<Object> object) {
		if (!taken) {
			checkCollision(object, this, findingPosition);
		}
	}
	
	public abstract void render(Graphics g);
	public void renderGeneralPowerUp(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(getBounds());
		g2d.drawImage(powerUpTex.powerup[0], (int)posX, (int)posY, null);
			
	}
	
	public boolean isToBeRemoved() {
		return this.removeThis;
	}
	
	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

	public void setToBeRemoved() {
		this.removeThis = true;
	}
	
	public boolean isFindingPosition() {
		return this.findingPosition;
	}
}