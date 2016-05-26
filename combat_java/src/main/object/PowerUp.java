package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;

public abstract class PowerUp extends Object {
	
	protected Texture powerUpTex = Game.getTexture();
	protected Tank tank = null;
	protected int powerupType, w_gen = 48, h_gen = 48;
	protected boolean notStarted = true, collideWithTank = false, collideWithWall = false, collideWithPowerUp = false, findingPosition = false;
	// If removeThis is true then this power up will be removed from handler object.
	protected boolean removeThis = false, taken = false;
	long beginTime = 0, endTime = 0;
	/* initialize the coordinates for the power up.
	* The posX_Gen and posY_Gen are for the power up generated
	* in random positions. We use a double size for the power
	* up bounds so that for the collision detection of power up
	* generation, the power up will not be generated in where the
	* tank cannot access.
	*/
	protected float posX, posY;
	protected ID id;
	
	public PowerUp (float posX, float posY, ID id) {
		super(posX, posY, id);
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		setSize(24, 24);
	}
	
	public void tick() {
		if (taken) {
			if (notStarted) {
				beginTime = System.currentTimeMillis();
				notStarted = false;
			}
			endTime = System.currentTimeMillis();
			System.out.println("time: "+(endTime - beginTime));
			if (endTime - beginTime >= 15000) {
				taken = false;
				disablePowerUp(tank);
			}
			if (tank == null) {
				disablePowerUp(tank);
			}
		}
	}
	
	public boolean checkCollision(LinkedList<Object> object, PowerUp powerup, boolean lookingForPos) {
		findingPosition = lookingForPos;
		for (int i = 0; i < object.size(); i++) {
			Object tempObject = object.get(i);
			Area powerupBounds = powerup.getHitbox();
			if (tempObject.getID() == ID.Wall) {
				Wall wall = (Wall)tempObject;
				powerupBounds.intersect(wall.getWallHitbox(wall.getBounds()));
				if (!powerupBounds.isEmpty()) {
					collideWithWall = true;								
				}
				//System.out.println("collide with wall: "+collideWithWall);
			} else if (tempObject.getID() == ID.PowerUp) {
				PowerUp otherpowerup = (PowerUp)tempObject;
				powerupBounds.intersect(otherpowerup.getHitbox());
				if (!powerupBounds.isEmpty()) {
					collideWithPowerUp = true;
				}
			} else if (tempObject.getID() == ID.Tank) {
				Tank tank = (Tank)tempObject;
				powerupBounds.intersect(tank.getHitbox());
				if (!powerupBounds.isEmpty()) {
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
	
	public void stopTimer() {
		beginTime = 0;
		endTime = 0;
	}
	public abstract void setPowerUpType();
	
	public int getPowerUpType() {
		return this.powerupType;
	}
		
	public void tick(LinkedList<Object> object) {
		if (!taken) {
			checkCollision(object, this, findingPosition);
		}
	}
	
	public abstract void renderThisPowerUp(Graphics2D g2d, int posX, int posY);
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if (!taken) {
			g2d.draw(getBounds());
			g2d.drawImage(powerUpTex.powerup[0], (int)posX, (int)posY, null);
		}
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