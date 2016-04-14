package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
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
	}
	
	public void setPowerUp(Tank tank) {
		if(rate == true) {
			tank.setSpdX(4.5f);
			tank.setSpdY(4.5f);
		} else {
			tank.setSpdX(1.5f);
			tank.setSpdY(1.5f);
		}
	}
	
	public void disable(Tank tank) {
		tank.setSpdX(3f);
		tank.setSpdY(3f);
	}

	@Override
	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		// Display FR increased powerup
		if (rate == false) {
			g2d.drawImage(powerUpTex.powerup[4], (int)posX, (int)posY, null);

		// Display FR decreased powerup
		} else {
			g2d.drawImage(powerUpTex.powerup[5], (int)posX, (int)posY, null);
		}
	}

	public Rectangle getBounds() {
		Rectangle rectangle = new Rectangle((int)posX, (int)posY, (int)w, (int)h);
		return rectangle;
	}
	
	public boolean getRate() {
		return rate;
	}
}
