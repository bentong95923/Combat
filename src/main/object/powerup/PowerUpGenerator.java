package main.object.powerup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
import main.game.Handler;

public class PowerUpGenerator {

	public static final int SPEEDUP = 0;
	public static final int SPEEDDOWN = 1;
	public static final int FRUP = 2;
	public static final int FRDOWN = 3;
	public static final int SHIELD = 4;

	protected List<PowerUp> powerUpGen = new ArrayList<PowerUp>();
	boolean finishLoading = false, findingPosition = false;

	Random randomNumGen = new Random();
		
	int randposX = 0;
	int randposY = 0;
	
	public PowerUpGenerator() {
	}
	
	public void generatePowerUps(Handler handler) {
		finishLoading = false;
				
		// Find random choice of power ups
		int randchoice[] = {0, 0, 0};
		for (int i = 0; i < 3; i++) {
			randchoice[i] = randomNumGen.nextInt(5);
		}
		System.out.println("randchoice: " + randchoice[0]+ " " +randchoice[1] + " " + randchoice[2]);
		// Store temporary 3 power ups 
		for (int i = 0; i < 3; i++) {
		findPosition(handler.o);		
			switch(randchoice[i]) {
				
				case (SPEEDUP):powerUpGen.add(new Speed(randposX,randposY, true, ID.PowerUp)); break;
				case (SPEEDDOWN):powerUpGen.add(new Speed(randposX,randposY, false, ID.PowerUp)); break;
				case (FRUP):powerUpGen.add(new FireRate(randposX,randposY, true, ID.PowerUp)); break;
				case (FRDOWN):powerUpGen.add(new FireRate(randposX,randposY, false, ID.PowerUp)); break;
				case (SHIELD):powerUpGen.add(new Shield(randposX,randposY, ID.PowerUp)); break;
				
			}
			handler.addObj(powerUpGen.get(i));
		}
		finishLoading = true;
	}
	
	public void findPosition(LinkedList<Object> object) {
		boolean collision = true;
		findingPosition = true;
		while (collision) {
			randposX = randomNumGen.nextInt(940) + 30;		
			randposY = randomNumGen.nextInt(740) + 30;
			
			Shield testPowerUp = new Shield((float)randposX, (float)randposY, ID.PowerUp);
			// Check collision before generating the power ups
			collision = testPowerUp.checkCollision(object, (PowerUp)testPowerUp, findingPosition);
			if (collision) {
				break;
			}
				testPowerUp.resetCollisionBoolean();
			}
		findingPosition = false;
	}
	
	public List<PowerUp> getPowerUpChosen() {
		return powerUpGen;
	}
	
}
