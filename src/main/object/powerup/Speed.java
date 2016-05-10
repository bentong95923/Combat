package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
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
		if (rate == true) {
			tank.setTankSpdXY(7.5f);
		} else {
			tank.setTankSpdXY(2.5f);
		}
	}
	
	public void disablePowerUp(Tank tank) {
		tank.setTankSpdXY(5);
	}

	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		renderGeneralPowerUp(g);
		Graphics2D g2d = (Graphics2D) g;
			// Display FR increased power up
			if (rate == false) {
				//g2d.drawImage(powerUpTex.powerup[4], (int)posX, (int)posY, null);
	
			// Display FR decreased power up
			} else {
				//g2d.drawImage(powerUpTex.powerup[5], (int)posX, (int)posY, null);
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
