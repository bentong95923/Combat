package main.object.powerup;

import java.awt.Graphics2D;

import main.body.ID;
import main.object.PowerUp;
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
			tank.setShootingRate(7.5f);
		} else {
			// 0.5x is equal to 1 bullets per second
			tank.setShootingRate(2.5f);
		}
		this.tank = tank;
	}
	
	public void disablePowerUp(Tank tank) {
		// By default the shooting rate should be 5 bullets per second (0.2sec per bullet)
		tank.setShootingRate(5f);
		this.stopTimer();
		tank.removePowerUpFromThisTank();
	}

	public void renderThisPowerUp(Graphics2D g2d, int posX, int posY) {
		if(rate == true) {
			g2d.drawImage(powerUpTex.powerup[1], posX, posY, null);
		} else {
			g2d.drawImage(powerUpTex.powerup[2], posX, posY, null);
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
