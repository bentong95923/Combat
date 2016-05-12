package main.object;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;

public class Wall extends Object  {

	Texture wallTex = Game.getTexture();
	
	public Wall(float x, float y, String colorType, ID id) {
		super(x, y, id);
		typeNum = getWallTypeNum(colorType);
		setSize(12, 12);
	}

	public void tick() {}
	
	public void render(Graphics g) {
		if (typeNum != 0) {
			// generate graphic according to the type of walls
			g.drawImage(wallTex.wall[typeNum-1], (int)posX, (int)posY, null);
		} else {
			// display error if the wall does not load correctly.
			System.out.println("Error: Wall does not load correctly.");
		}
		/* Fixing to load the wall at the upper left corner.
		 * The frequency of repainting the game in the render method is 
		 * probably not as fast as that of updating the game. Thus the wall
		 * at the upper left corner was missing. The line below tries to
		 * complement it.
		 */
		g.drawImage(wallTex.wall[typeNum-1], (int)2, (int)0, null);
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(getWallHitbox(getBoundsTankRespawn()));
	}

	/* generate color code for the wall. If number code is accidentally
	*  inserted as an argument wall constructor, it will return the same
	*  number.
	*/
	public int getWallTypeNum(String colorType) {
		colorType = colorType.toUpperCase();
		switch(colorType) {
			case "BLACK": return 1; 
			case "BLUE" : return 2;
			case "GREEN": return 3;
			case "RED"  : return 4;
			case "YELLOW":return 5;
			
			case "1"	: return 1;
			case "2"	: return 2;
			case "3"	: return 3;
			case "4"	: return 4;
			case "5" 	: return 5;
			default: return 0;
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, (int)w, (int)h);
	}
	
	public Rectangle getBoundsTankRespawn() {
		return new Rectangle((int)posX-12, (int)posY-12, (int)36, (int)36);
	}
	
	public Rectangle getBoundsTop() {
		return new Rectangle((int)posX + 1, (int)posY, 10, 1);
	}
	
	public Rectangle getBoundsBottom() {
		return new Rectangle((int)posX + 1, (int)posY + 11, 10, 1);
	}
	
	public Rectangle getBoundsLeft() {
		return new Rectangle((int)posX, (int)posY + 1, 1, 10);
	}
	
	public Rectangle getBoundsRight() {
		return new Rectangle((int)posX + 11, (int)posY + 1, 1, 10);
	}
		
	public Rectangle getBoundsTopLeftCorner() {
		return new Rectangle((int)posX, (int)posY, 1, 1);
	}
	
	public Rectangle getBoundsBottomLeftCorner() {
		return new Rectangle((int)posX, (int)posY + 11, 1, 1);
	}
	
	public Rectangle getBoundsTopRightCorner() {
		return new Rectangle((int)posX + 11, (int)posY, 1, 1);
	}
	
	public Rectangle getBoundsBottomRightCorner() {
		return new Rectangle((int)posX + 11, (int)posY + 11, 1, 1);
	}
	

	public Area getWallHitbox(Rectangle bounds) {
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		return new Area(at.createTransformedShape(bounds));
	}
	
}
