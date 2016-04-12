package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;
import main.game.Handler;


public class Bullet extends Object {	

	private Handler handler;
	
	Texture bulletTex = Game.getTexture();
	
	private float w = 8, h = 8;
	private boolean leftOrRight = false;
	
	public Bullet(float x, float y, float spdX, float spdY, float angle, boolean leftOrRight, ID id) {
		super(x, y, id);
		this.spdX = spdX;
		this.spdY = spdY;
		this.angle = angle;		
		this.leftOrRight = leftOrRight;
	}

	public void tick(LinkedList<Object> object) {
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
		
		if(posY <= 24 || posY >= Game.HEIGHT - 32){
			spdY *= -1;
		}
		if(posX <= 24 || posX >= Game.WIDTH - 32){
			spdX *= -1;
		}
/*		for (int i = 0; i < handler.o.size(); i++) {
			Object CollisionObject = handler.o.get(i);
			
			if (CollisionObject.getID() == ID.Wall) {
				if(getBounds().intersects(CollisionObject.getBounds())){
					System.out.println("Bitch you bounced");
					spdX *= -1;
					spdY *= -1;
				}
			}
		}*/
	}

	public void render(Graphics g) {
		// create a 2D graphic for the tank being able to turn
		Graphics2D g2d = (Graphics2D)g;
		AffineTransform objRotate = g2d.getTransform();
		if (leftOrRight == true) {
			// rotate the tank about the centre according to the magnitude of the angle
			g2d.rotate(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		} else {
			g2d.rotate(Math.toRadians(angle+180), posX+w*0.5, posY+h*0.5);
		}
		g2d.drawImage(bulletTex.bullet[0], (int)posX, (int)posY, null);
		g2d.setTransform(objRotate);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 8, 8);
	}

}
