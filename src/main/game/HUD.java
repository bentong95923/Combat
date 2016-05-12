package main.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.body.BufferedImageLoader;
import main.object.PowerUp;
import main.object.Tank;

public class HUD {
	BufferedImageLoader imgLoader = new BufferedImageLoader();
	List<BufferedImage>HUDStr = new ArrayList<BufferedImage>(), powerUpTimerStr = new ArrayList<BufferedImage>();
	PowerUp powerupTankL = null, powerupTankR = null;
	Tank tankL = null, tankR = null;
	private boolean displayHUD = false;
	private int gms;
	private int p1score = 0, p2score = 0;
	
	public HUD(int gms) {
		this.gms = gms;
		init();
	}

	public void init() {
		loadHUDStr();
		loadPowerUpTimerStr();
	}
	
	public void tick(Tank tankLeft, Tank tankRight, int p1score, int p2score) {
		updateTank(tankLeft, tankRight);
		checkContentLoaded();
		updateScore(p1score, p2score);
		updateTankPowerUpConsumed(tankLeft, tankRight);
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		if (displayHUD) {
			displayP1HUD(g2d);
			displayP2HUD(g2d);			
			displayScore(g2d);
			// Displaying PowerUp consumed for all players
			displayPowerUpConsumed(g2d);
		}
	}
	
	public void loadHUDStr() {
		HUDStr.clear();
		for (int i = 0; i < 5; i++) {
			HUDStr.add(imgLoader.loadingImage("/font/gameplay/HUD/"+(i+1)+".png"));
		}
	}
	
	public void loadPowerUpTimerStr() {
		powerUpTimerStr.clear();
		for (int i = 0; i < 15; i++) {
			powerUpTimerStr.add(imgLoader.loadingImage("/font/gameplay/HUD/PowerUpTimer/"+(i+1)+".png"));
		}
	}
	
	public void updateTank(Tank tankL, Tank tankR) {
		this.tankL = tankL;
		this.tankR = tankR;
	}
	
	public void displayP1HUD(Graphics2D g2d) {
		// Displaying Score
		g2d.drawImage(HUDStr.get(4), 25, 585, null);
		
		// Displaying "P1"
		g2d.drawImage(HUDStr.get(0), 25, 675, null);
	}
	
	public void displayP2HUD(Graphics2D g2d) {		
		/* Displaying Player 2 's HUD
		 * Again Training mode does not have player 2 HUD
		 */
		if (gms != 2) {
			// Displaying Score
			g2d.drawImage(HUDStr.get(4), 1024-HUDStr.get(4).getWidth() - 90, 585, null);
		}		
		BufferedImage p2Str = null;
		switch(gms) {
		// Displaying "COM" in SinglePlayer
		case 0: p2Str = HUDStr.get(2); break;
		// Displaying "P2" in Multi-Player
		case 1: p2Str = HUDStr.get(1); break; 
		// Training mode does not have player 2 HUD
		}
		g2d.drawImage(p2Str, 1024-p2Str.getWidth() - 25, 675, null);
	}
	
	public void displayScore(Graphics2D g2d) {
		String p1scoreStr = Integer.toString(p1score);
		String p2scoreStr = Integer.toString(p2score);
		Font score = new Font("Courier", Font.BOLD, 30);
		g2d.setFont(score);
		g2d.setColor(new Color(255, 128, 0));
		g2d.drawString(p1scoreStr, 115, 610);
		
		g2d.setFont(score);
		g2d.setColor(new Color(255, 128, 0));
		g2d.drawString(p2scoreStr, 940, 610);
	}
	
	public void displayPowerUpConsumed(Graphics2D g2d) {
		if (powerupTankL != null) {
			powerupTankL.renderThisPowerUp(g2d, 25, 625);
		} else {			
			g2d.drawImage(HUDStr.get(3), 25, 625, null);
		}
		if (powerupTankR != null) {
			powerupTankR.renderThisPowerUp(g2d, 975, 625);
		} else {
			g2d.drawImage(HUDStr.get(3), 1024-HUDStr.get(3).getWidth() - 25, 625, null);
		}
	}
	
	public void checkContentLoaded() {
		if (HUDStr.size() == 5 && powerUpTimerStr.size() == 15) {
			displayHUD = true;
		}
	}
	
	public void updateScore(int p1score, int p2score) {
		this.p1score = p1score;
		this.p2score = p2score;
	}
	
	public void updateTankPowerUpConsumed(Tank tankLeft, Tank tankRight) {
		if (tankLeft != null) {
			powerupTankL = tankLeft.getPowerUpOnThisTank();
		} else {
			powerupTankL = null;
		}
		
		if (tankRight != null) {
			powerupTankR = tankRight.getPowerUpOnThisTank();			
		} else {
			powerupTankR = null;
		}
	}
	
}
