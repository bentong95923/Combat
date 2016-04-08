package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;


public class Bullet extends Object {	

	Texture bulletTex = Game.getTexture();
	
	public Bullet(float x, float y, float spdX, float spdY, float angle, ID id) {
		super(x, y, id);
		this.spdX = spdX;
		this.spdY = spdY;
		this.angle = angle;		
	}

	public void tick(LinkedList<Object> object) {
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));		
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(bulletTex.bullet[0], (int)posX, (int)posY, null);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 8, 8);
	}

}
