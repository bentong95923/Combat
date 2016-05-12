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

	Texture bulletTex = Game.getTexture();
	
	private boolean leftOrRight = false, collidedWithWall = false;
	int tickCount = 0;
	Handler handler;
	
	public Bullet(float x, float y, float spdX, float spdY, float angle, Handler handler, boolean leftOrRight, ID id) {
		super(x, y, id);
		this.spdX = spdX;
		this.spdY = spdY;
		this.angle = angle;
		this.handler = handler;
		this.leftOrRight = leftOrRight;
		setSize(8, 8);
	}

	public void tick() {
		posX += (float) (spdX*Math.cos(Math.toRadians(angle)));
		posY += (float) (-spdY*Math.sin(Math.toRadians(angle)));
		tickCount++;
		collision(handler.o);
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
		g2d.draw(getHitbox());
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 8, 8);
	}
	
	public int getTickCount() {
		return tickCount;
	}
	
	private void collision(LinkedList<Object> object) {
		
		for (int i = 0; i < handler.o.size(); i++) {
			Object tempObject = handler.o.get(i);
			Rectangle bulletBounds = getBounds();
			if (tempObject.getID() == ID.Wall) {
				
				/* Bullet bounds off the wall edge at the complementary angle.
				 * If the bullet hits exactly at the corner of the wall, the bullet
				 * is reflected in the opposite direction to what it was traveled.
				 */
				if (bulletBounds.intersects( ((Wall)tempObject).getBoundsTopLeftCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsTop()) ) {
					spdY *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsTopLeftCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsLeft()) ) {
					spdX *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsTopRightCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsTop()) ) {
					spdY *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsTopRightCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsRight()) ) {
					spdX *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsBottomLeftCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsLeft()) ) {
					spdX *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsBottomLeftCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsBottom()) ) {
					spdY *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsBottomRightCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsBottom()) ) {
					spdY *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsBottomRightCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsRight()) ) {
					spdX *= -1;
				
				
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsBottomRightCorner() )
					&& bulletBounds.intersects( ((Wall)tempObject).getBoundsRight()) ) {
					spdX *= -1;
				} 
						
				else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsTop() )
					|| bulletBounds.intersects( ((Wall)tempObject).getBoundsBottom()) ) {
					spdY *= -1;
				} else if (bulletBounds.intersects( ((Wall)tempObject).getBoundsRight() )
					|| bulletBounds.intersects( ((Wall)tempObject).getBoundsLeft()) ) {
					spdX *= -1;
				}
				
				if (bulletBounds.intersects( ((Wall)tempObject).getBounds()) ) {
					collidedWithWall = true;
					System.out.println("collided With Wall!");
				}
				
			} 
						
			if (tempObject.getID() == ID.Bullet) {
				Bullet otherBullet = (Bullet)tempObject;
				// Both bullets destroy if they collide together
				if ( bulletBounds.intersects(((Bullet)tempObject).getBounds() ) ) {
					if ( otherBullet.getPosX() != getPosX()  || tempObject.getPosY() != getPosY()  ||
						otherBullet.getSpdX() != getSpdX() || otherBullet.getSpdY() != getSpdY()) {
						handler.removeObj(tempObject);
						handler.removeObj(this);
					}
				}
			}
			
		}
	}

	public boolean isCollidedWithWall() {
		return collidedWithWall;
	}

}
