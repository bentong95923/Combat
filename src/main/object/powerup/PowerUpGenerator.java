package main.object.powerup;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.body.ID;
import main.body.PowerUp;
import main.body.Texture;
import main.game.Game;

public class PowerUpGenerator {

	public static final int SPEEDUP = 0;
	public static final int SLOWDOWN= 1;
	public static final int FRUP = 2;
	public static final int FRDOWN = 3;
	public static final int SHIELD = 4;

	Random randNumGen = new Random();

	public List<PowerUp> powerUpGen = new ArrayList<PowerUp>();

	protected Texture powerUpTex = Game.getTexture();
	

	int randposX[] = {0, 0, 0};
	int randposY[] = {0, 0, 0};
	
	public PowerUpGenerator() {

		getRandomPowerUps();
	}
	
	public void getRandomPowerUps() {
		
		// Generate random number, and make sure that these power ups generated won't be collide
		for (int i = 0; i < 3; i++) {
			randposX[i] = randNumGen.nextInt(940) + 30;
			if (i != 0) {
				if (randposX[i] == randposX[i-1]) {
					i = 0;
				}
			}
		}
		
		for(int j = 0; j < 3; j++) {
			randposY[j] = randNumGen.nextInt(740) + 30;
			if (j != 0) {
				if (randposX[j] == randposX[j-1]) {
					j = 0;
				}
			}
		}
		
		// Find random choice of power ups
		int randchoice[] = {0, 0, 0};
		for (int i = 0; i < 3; i++) {
			randchoice[i] = randNumGen.nextInt(5);
		}
		
		// Store temporary power ups
		for (int i = 0; i < 3; i++) {
			switch(randchoice[i]) {
				
				case (SPEEDUP):powerUpGen.add(new Speed(randposX[i], randposY[i], true, ID.Speed)); break;
				case (SLOWDOWN):powerUpGen.add(new Speed(randposX[i], randposY[i], false, ID.Speed)); break;
				case (FRUP):powerUpGen.add(new FireRate(randposX[i], randposY[i], true, ID.FireRate)); break;
				case (FRDOWN):powerUpGen.add(new FireRate(randposX[i], randposY[i], false, ID.FireRate)); break;
				case (SHIELD):powerUpGen.add(new Shield(randposX[i], randposY[i], ID.Shield)); break;
				
			}
		}
		
	}
	
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// Draw general power up icon
		for (int i = 0; i < 3; i++) {
			g2d.drawImage(powerUpTex.powerup[0], (int)randposX[i], (int)randposY[i], null);
		}
	}
	
	public List<PowerUp> getPowerUps() {
		return powerUpGen;
	}

	
}
