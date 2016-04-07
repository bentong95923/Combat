package main.object;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.Texture;
import main.game.Game;

public class Wall extends Object  {

	Texture wallTex = Game.getTexture();
	
	public Wall(float x, float y, String colorType, ID id) {
		super(x, y, id);
		typeNum = getWallTypeNum(colorType);
	}

	public void tick(LinkedList<Object> object) {}
	
	public void render(Graphics g) {
		
		if (typeNum != 0) {
			// generate graphic according to the type of walls
			g.drawImage(wallTex.wall[typeNum-1], (int)posX, (int)posY, null);
		} else {
			// display error if the wall does not load correctly.
			System.out.println("Error: Wall does not load correctly.");
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int)posX, (int)posY, 32, 32);
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
			
			case "1"	: return 1;
			case "2"	: return 2;
			case "3"	: return 3;
			case "4"	: return 4;
			default: return 0;
		}
	}
	
}
