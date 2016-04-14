package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
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
	}

	public void setPowerup(Tank tank) {
		tank.setShieldAvailable();
	}
	
	public void disable(Tank tank) {
		tank.setShieldDisable();
	}

	public void tick(LinkedList<Object> object) {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(powerUpTex.powerup[4], (int)posX, (int)posY, null);
	}

	public Rectangle getBounds() {
		Rectangle rectangle = new Rectangle((int)posX, (int)posY, (int)w, (int)h);
		return rectangle;
	}
		
}
