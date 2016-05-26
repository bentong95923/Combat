package main.object.powerup;

import java.awt.Graphics2D;

import main.body.ID;
import main.object.PowerUp;
import main.object.Tank;

public class Speed extends PowerUp{
	// true = speed up, false = slow down
	boolean rate;
		
	float posX, posY;
	ID id;
	
	public Speed (float posX, float posY, boolean rate, ID id) {
		super(posX, posY, id);
		this.rate = rate;
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		setPowerUpType();
	}
		
	public void enablePowerUp(Tank tank) {
		float tankSpd = 3.0f;
		
		if (rate == true) {
			tankSpd = 4.5f;
		} else {
			tankSpd = 1.5f;
		}
		tank.setTankSpdXY(tankSpd, tank.getDirection());
		this.tank = tank;
	}
	
	public void disablePowerUp(Tank tank) {
		String upOrDown = "";
		if (tank.getPosY()-tank.getPrevPosY() > 0) {
			upOrDown = "down";
		} else if (tank.getPosY()-tank.getPrevPosY() < 0) {
			upOrDown = "up";
		}		
		tank.setTankSpdXY(3f, upOrDown);
		tank.removePowerUpFromThisTank();
		this.stopTimer();
	}

	public void renderThisPowerUp(Graphics2D g2d, int posX, int posY) {
		// Display FR increased power up
		if (rate == false) {
			g2d.drawImage(powerUpTex.powerup[4], posX, posY, null);

		// Display FR decreased power up
		} else {
			g2d.drawImage(powerUpTex.powerup[5], posX, posY, null);
		}
		
	}
	
	public void setPowerUpType() {
		if (rate == true) {
			this.powerupType = PowerUpGenerator.SPEEDUP;
		} else {
			this.powerupType = PowerUpGenerator.SPEEDDOWN;
		}
	}
	
	public boolean getRate() {
		return rate;
	}
}
