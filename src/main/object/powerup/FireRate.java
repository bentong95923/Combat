package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
import main.object.Bullet;

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
	}
	
	public void setPowerUp(Bullet bullet) {
		if(rate == true) {
			bullet.setSpdX(13.5f);
			bullet.setSpdY(13.5f);
		} else {
			bullet.setSpdX(4.5f);
			bullet.setSpdY(4.5f);
		}
	}
	
	public void disable(Bullet bullet) {
		bullet.setSpdX(9f);
		bullet.setSpdY(9f);
	}

	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		if(rate == true) {
			g2d.drawImage(powerUpTex.powerup[1], (int)posX, (int)posY, null);
		} else {
			g2d.drawImage(powerUpTex.powerup[2], (int)posX, (int)posY, null);
		}
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean getRate() {
		return rate;
	}
	
}
