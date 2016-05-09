package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
import main.object.Bullet;
import main.object.Tank;

public class FireRate extends PowerUp{
	// true = speed up, false = slow down
	boolean rate;
	
	float posX, posY;
	ID id;
	
	public FireRate(float posX, float posY, boolean rate, ID id) {
		super(posX,posY, id);
		this.rate = rate;
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		setPowerUpType();
	}
	
	public void enablePowerUp(Tank tank) {
		if (rate == true) {
			// 1.5x is equal to 3 bullets per second
			tank.setShootingRate(3f);
		} else {
			// 0.5x is equal to 1 bullets per second
			tank.setShootingRate(1f);
		}
	}
	
	public void disablePowerUp(Tank tank) {
		// By default the shooting rate should be 2 bullets per second
		tank.setShootingRate(2f);
	}

	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		if (!(this.taken)) {
			renderGeneralPowerUp(g);
		} else {
			Graphics2D g2d = (Graphics2D) g;
			if(rate == true) {
				g2d.drawImage(powerUpTex.powerup[1], (int)posX, (int)posY, null);
			} else {
				g2d.drawImage(powerUpTex.powerup[2], (int)posX, (int)posY, null);
			}
		}	
		
	}
		
	public boolean getRate() {
		return rate;
	}
	
	public void setPowerUpType() {
		if (rate == true) {
			this.powerupType = PowerUpGenerator.FRUP;
		} else {
			this.powerupType = PowerUpGenerator.FRDOWN;
		}
	}
	
}
