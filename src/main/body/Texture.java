package main.body;

import java.awt.image.BufferedImage;

import main.game.BufferedImageLoader;

public class Texture {
	
	// Load images for all tanks
	private BufferedImage tankBlackL = null, tankBlueL = null, tankBrownL = null, tankGreenL = null;
	
	private BufferedImage tankBlackR = null, tankBlueR = null, tankBrownR = null, tankGreenR = null;
	
	// load images for all walls
	private BufferedImage wallBlack = null, wallBlue = null, wallGreen = null, wallRed = null, wallYellow = null;	
	
	// load images for all bullet
	private BufferedImage bullet_load = null, pow = null, frUp = null, frDown = null, shield = null, slow = null, fast = null;
	
	// store the load images into an array for use later
	public BufferedImage[] wall = new BufferedImage[5], tank = new BufferedImage[8], powerup = new BufferedImage[6], bullet = new BufferedImage[1];
	
	
	public Texture() { 
		BufferedImageLoader imgLoader = new BufferedImageLoader();
			
		try {
			tankBlackL = imgLoader.loadingImage("/tank/left/tank_black.png");
			tankBlueL = imgLoader.loadingImage("/tank/left/tank_blue.png");
			tankBrownL = imgLoader.loadingImage("/tank/left/tank_brown.png");
			tankGreenL = imgLoader.loadingImage("/tank/left/tank_green.png");
			
			tankBlackR = imgLoader.loadingImage("/tank/right/tank_black.png");
			tankBlueR = imgLoader.loadingImage("/tank/right/tank_blue.png");
			tankBrownR = imgLoader.loadingImage("/tank/right/tank_brown.png");
			tankGreenR = imgLoader.loadingImage("/tank/right/tank_green.png");
			
			wallBlack = imgLoader.loadingImage("/wall/wall_black.png");
			wallBlue = imgLoader.loadingImage("/wall/wall_blue.png");
			wallGreen = imgLoader.loadingImage("/wall/wall_green.png");
			wallRed = imgLoader.loadingImage("/wall/wall_red.png");
			wallYellow = imgLoader.loadingImage("/wall/wall_yellow.png");
			
			bullet_load = imgLoader.loadingImage("/bullet/bullet.png");
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		getTexture();
	}
	
	private void getTexture() {
		
		tank[0] = tankBlackL;
		tank[1] = tankBlueL;
		tank[2] = tankBrownL;
		tank[3] = tankGreenL;
		
		tank[4] = tankBlackR;
		tank[5] = tankBlueR;
		tank[6] = tankBrownR;
		tank[7] = tankGreenR;
		
		wall[0] = wallBlack;
		wall[1] = wallBlue;
		wall[2] = wallGreen;
		wall[3] = wallRed;
		wall[4] = wallYellow;
		
		bullet[0] = bullet_load;
	}
	
}


