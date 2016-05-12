package main.object.powerup;

import java.awt.Graphics2D;

import main.body.ID;
import main.object.PowerUp;
import main.object.Tank;

public class Shield extends PowerUp{
		
	float posX, posY;
	boolean taken;
	ID id;
	
	public Shield(float posX, float posY, ID id) {
		// TODO Auto-generated constructor stub
		super(posX, posY, id);
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		setPowerUpType();
	}

	public void enablePowerUp(Tank tank) {
		tank.setShieldEnabled();
		this.tank = tank;
	}
	
	public void disablePowerUp(Tank tank) {
		tank.setShieldDisabled();
		tank.removePowerUpFromThisTank();
		this.stopTimer();
	}

	public void renderThisPowerUp(Graphics2D g2d, int posX, int posY) {
		g2d.drawImage(powerUpTex.powerup[4], posX, posY, null);
	}
	
	public void setPowerUpType() {
		this.powerupType = PowerUpGenerator.SHIELD;
	}
}
