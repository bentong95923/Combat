package main.body;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.game.BufferedImageLoader;
import main.game.Handler;
import main.object.Tank;
import main.object.Wall;

public class Level {
	
	BufferedImage levelImg = null;

	private List<Integer> randNumLog = new ArrayList();
	
	Random randomNumGenerator = new Random();
	
	// create an image loader
	BufferedImageLoader imageLoader = new BufferedImageLoader();
	
	Handler handler;
	
	public Level(Handler handler) {
		this.handler = handler;
	}
	
	public BufferedImage getLevelSheet() {
		
		boolean numGenBefore = true;
		int randomNum = 0;
		
		// Reset the log if all the levels has been used in the game.
		if (randNumLog.size() == 22) {
			randNumLog.clear();
		}
		
		// Try to get a level which is different from the one used before.
		while (numGenBefore == true) {
			// Generate a random number
			randomNum = randomNumGenerator.nextInt(19) + 1;
				
			// Add the number to the log
			if (!(randNumLog.contains(randomNum))) {
				numGenBefore = false;
				randNumLog.add(randomNum);
			}
			
		}
		
		if (randomNum != 0) {
			Integer temp = new Integer(randomNum);
			String randNumToString = temp.toString();
			levelImg = imageLoader.loadingImage("/level/" + randNumToString + ".png");
		} else {
			System.out.println("Error: Level sheet has not been loaded correctly.");
		}
		
		return levelImg;
		
	}
	
	public void generateLevel() {
		levelImg = getLevelSheet();
		// Generate level according to the map picture
		for (int i = 0; i < levelImg.getHeight(); i++) {
			for (int j = 0; j < levelImg.getWidth(); j++) {
				
				Color px = new Color(levelImg.getRGB(j, i));
		
				int red =  px.getRed();
				int green = px.getGreen();
				int blue = px.getBlue();
				
				ID tank;
				// true = left tank, false = right tank
				boolean leftOrRight;
				
				/* Identify left tank or right tank
				 * - the width of the picture of the map is 86px so 
				 *  half of it is 43px
				 *  
				 *  2 has been added to "j" to recover the rest of the 
				 */
				if (j <= 43) {
					leftOrRight = true; tank = ID.TankLeft;
				} else {
					leftOrRight = false; tank = ID.TankRight;
				}
				
				// generate black tank
				if (red == 51 && green == 51 && blue == 51) {
					handler.addObj(new Tank(j*12+2, i*12, "black", leftOrRight, tank));	
				}
				
				// generate blue tank
				if (red == 00 && green == 00 && blue == 127) {
					handler.addObj(new Tank(j*12+2, i*12, "blue", leftOrRight, tank));
				}
				
				// generate brown tank
				if (red == 127 && green == 00 && blue == 00) {
					handler.addObj(new Tank(j*12+2, i*12, "brown", leftOrRight, tank));
				}
				
				// generate green tank
				if (red == 00 && green == 127 && blue == 00) {
					handler.addObj(new Tank(j*12+2, i*12, "green", leftOrRight, tank));
				}
				
				// generate black wall
				if (red == 00 && green == 00 && blue == 00) {
					handler.addObj(new Wall(j*12+2, i*12, "black", ID.Wall));
				}
				
				// generate blue wall
				if (red == 00 && green == 00 && blue == 255) {
					handler.addObj(new Wall(j*12+2, i*12, "blue", ID.Wall));
				}
				
				// generate green wall
				if (red == 00 && green == 255 && blue == 00) {
					handler.addObj(new Wall(j*12+2, i*12, "green", ID.Wall));
				}
				
				// generate red wall
				if (red == 255 && green == 00 && blue == 00) {
					handler.addObj(new Wall(j*12+2, i*12, "red", ID.Wall));
				}
				
				// generate yellow wall
				if (red == 255 && green == 255 && blue == 00) {
					handler.addObj(new Wall(j*12+2, i*12, "yellow", ID.Wall));
				}
			}
		}	
	}
}



