package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
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
	}
	
	public void disablePowerUp(Tank tank) {
		tank.setShieldDisabled();
	}

	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		renderGeneralPowerUp(g);
		Graphics2D g2d = (Graphics2D) g;
		//g2d.drawImage(powerUpTex.powerup[4], (int)posX, (int)posY, null);
	}
	
	public void setPowerUpType() {
		this.powerupType = PowerUpGenerator.SHIELD;
	}
}
