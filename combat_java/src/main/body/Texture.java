package main.body;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

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
			
		try {
			tankBlackL = loadingImage("/img/tank/left/tank_black.png");
			tankBlueL = loadingImage("/img/tank/left/tank_blue.png");
			tankBrownL = loadingImage("/img/tank/left/tank_brown.png");
			tankGreenL = loadingImage("/img/tank/left/tank_green.png");
			
			tankBlackR = loadingImage("/img/tank/right/tank_black.png");
			tankBlueR = loadingImage("/img/tank/right/tank_blue.png");
			tankBrownR = loadingImage("/img/tank/right/tank_brown.png");
			tankGreenR = loadingImage("/img/tank/right/tank_green.png");
			
			wallBlack = loadingImage("/img/wall/wall_black.png");
			wallBlue = loadingImage("/img/wall/wall_blue.png");
			wallGreen = loadingImage("/img/wall/wall_green.png");
			wallRed = loadingImage("/img/wall/wall_red.png");
			wallYellow = loadingImage("/img/wall/wall_yellow.png");
			
			pow = loadingImage("/img/powerup/pow.png");
			frUp = loadingImage("/img/powerup/increasefirerate.png");
			frDown = loadingImage("/img/powerup/decreasefirerate.png");
			shield = loadingImage("/img/powerup/shield.png");
			slow = loadingImage("/img/powerup/slowdown.png");
			fast = loadingImage("/img/powerup/speedup.png");
			
			bullet_load = loadingImage("/img/bullet/bullet.png");
			
		} catch(Exception e) {
			e.printStackTrace();
		}

		getTexture();
	}
	
	
	public BufferedImage loadingImage(String path) {
		BufferedImage imgToLoad = null;
		try {
			imgToLoad = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imgToLoad;
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
		
		powerup[0] = pow;
		powerup[1] = frUp;
		powerup[2] = frDown;
		powerup[3] = shield;
		powerup[4] = slow;
		powerup[5] = fast;
		
		
		bullet[0] = bullet_load;
	}
	
}


