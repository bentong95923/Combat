package main.body;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.game.Handler;
import main.object.Tank;
import main.object.Wall;
import main.state.StateManager;

public class MapLoader {

	BufferedImage levelImg = null;
	
	// create an image loader
	BufferedImageLoader imageLoader = new BufferedImageLoader();
	

	/*	fetching a map from a scaled down level sheet (1:12), 
	 * 	this method will load the level and scale up the map.
	 */	
	public BufferedImage getLevelSheet(StateManager sm) {
		
		boolean numGenBefore = true;
		int randomNum = 0;
		
		Random randomNumGenerator = new Random();
		
		// Reset the log if all the levels has been used in the game.
		if (sm.getSizeRandNumLog() == 22) {
			sm.clearRandNumLog();
		}
		
		// Try to get a level which is different from the one used before.
		while (numGenBefore == true) {
			// Generate a random number
			randomNum = randomNumGenerator.nextInt(19) + 1;
				
			// Add the number to the log
			if (!(sm.RandNumLogContainElement(randomNum))) {
				numGenBefore = false;
				sm.RandNumLogAddElement(randomNum);
			}
			
		}
		
		// Load the level sheet
		if (randomNum != 0) {
			levelImg = imageLoader.loadingImage("/img/level/" + randomNum + ".png");
		} else {
			System.out.println("Error: Level sheet has not been loaded correctly.");
		}
		
		return levelImg;
		
	}

	public void generateLevel(Handler handler, StateManager sm) {
		levelImg = getLevelSheet(sm);
		// Generate level according to the map picture
		for (int i = 0; i < levelImg.getHeight(); i++) {
			for (int j = 0; j < levelImg.getWidth(); j++) {
				
				Color px = new Color(levelImg.getRGB(j, i));
		
				int red =  px.getRed();
				int green = px.getGreen();
				int blue = px.getBlue();
				
				// true = left tank, false = right tank
				boolean leftOrRight;
				
				/* Identify left tank or right tank
				 * - the width of the picture of the map is 86px so 
				 *  half of it is 43px
				 *  
				 *  2 has been added to "j" to recover the rest of the 
				 */
				if (j <= 43) {
					leftOrRight = true;
				} else {
					leftOrRight = false;
				}
				
				// generate black tank
				if (red == 51 && green == 51 && blue == 51) {
					handler.addObj(new Tank(j*12+2, i*12, handler, "black", leftOrRight, ID.Tank));	
				}
				
				// generate blue tank
				if (red == 00 && green == 00 && blue == 127) {
					handler.addObj(new Tank(j*12+2, i*12, handler, "blue", leftOrRight, ID.Tank));
				}
				
				// generate brown tank
				if (red == 127 && green == 00 && blue == 00) {
					handler.addObj(new Tank(j*12+2, i*12, handler, "brown", leftOrRight, ID.Tank));
				}
				
				// generate green tank
				if (red == 00 && green == 127 && blue == 00) {
					handler.addObj(new Tank(j*12+2, i*12, handler, "green", leftOrRight, ID.Tank));
				}
				
				// generate black wall
				if (red == 00 && green == 00 && blue == 00) {
					handler.addObj(new Wall((j*12)+2, i*12, "black", ID.Wall));
				}
				
				// generate blue wall
				if (red == 00 && green == 00 && blue == 255) {
					handler.addObj(new Wall((j*12)+2, i*12, "blue", ID.Wall));
				}
				
				// generate green wall
				if (red == 00 && green == 255 && blue == 00) {
					handler.addObj(new Wall((j*12)+2, i*12, "green", ID.Wall));
				}
				
				// generate red wall
				if (red == 255 && green == 00 && blue == 00) {
					handler.addObj(new Wall((j*12)+2, i*12, "red", ID.Wall));
				}
				
				// generate yellow wall
				if (red == 255 && green == 255 && blue == 00) {
					handler.addObj(new Wall((j*12)+2, i*12, "yellow", ID.Wall));
				}
			}
		}	
	}
	
}
