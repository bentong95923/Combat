package main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.body.ID;
import main.body.Object;
import main.object.Bullet;
import main.object.Tank;
import main.object.Wall;

public class Handler {
	
	private Object testObj;
	public LinkedList<Object> o = new LinkedList<Object>();
		
	// Setting up control limit for better game play
	boolean holdL = false, holdR = false, holdA = false, holdD = false;

	BufferedImage levelImg = null;

	private List<Integer> randNumLog = new ArrayList();
	
	Random randomNumGenerator = new Random();
	
	// create an image loader
	BufferedImageLoader imageLoader = new BufferedImageLoader();
	
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
					addObj(new Tank(j*12+2, i*12, this, "black", leftOrRight, tank));	
				}
				
				// generate blue tank
				if (red == 00 && green == 00 && blue == 127) {
					addObj(new Tank(j*12+2, i*12, this, "blue", leftOrRight, tank));
				}
				
				// generate brown tank
				if (red == 127 && green == 00 && blue == 00) {
					addObj(new Tank(j*12+2, i*12, this, "brown", leftOrRight, tank));
				}
				
				// generate green tank
				if (red == 00 && green == 127 && blue == 00) {
					addObj(new Tank(j*12+2, i*12, this, "green", leftOrRight, tank));
				}
				
				// generate black wall
				if (red == 00 && green == 00 && blue == 00) {
					addObj(new Wall((j*12)+2, i*12, "black", ID.Wall));
				}
				
				// generate blue wall
				if (red == 00 && green == 00 && blue == 255) {
					addObj(new Wall((j*12)+2, i*12, "blue", ID.Wall));
				}
				
				// generate green wall
				if (red == 00 && green == 255 && blue == 00) {
					addObj(new Wall((j*12)+2, i*12, "green", ID.Wall));
				}
				
				// generate red wall
				if (red == 255 && green == 00 && blue == 00) {
					addObj(new Wall((j*12)+2, i*12, "red", ID.Wall));
				}
				
				// generate yellow wall
				if (red == 255 && green == 255 && blue == 00) {
					addObj(new Wall((j*12)+2, i*12, "yellow", ID.Wall));
				}
			}
		}	
	}
	
	public void tick() {
		
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);			
			testObj.tick(o);
		}
	}
	
	public void render(Graphics g) {
		
		for (int i = o.size() - 1; i > 0; i--) {
			testObj = o.get(i);
			testObj.render(g);			
		}
	}
	
	public void addObj(Object obj) {
		this.o.add(obj);
	}
	
	public void removeObj(Object obj) {
		this.o.remove(obj);
	}
	
	/*	fetching a map from a scaled down level sheet (1:12) and 
	* 	scale up and load the level according to it.
	*/
	
	
	/* method will be called if a key is pressed,
	 * and response according to what key was pressed.
	 */
	

	public void keyPressed (KeyEvent k, int gms) {

		for (int i = 0 ; i < o.size(); i++) {
		
			Object tank = o.get(i);
			// Multi-player mode	
			if (gms == 1) {	
				if (tank.getID() == ID.TankLeft) {					
					// Assigning control for left tank movement
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_W): tank.setSpdX(3); tank.setSpdY(-3); break;
						case (KeyEvent.VK_S): tank.setSpdX(-3); tank.setSpdY(3); break;
						case (KeyEvent.VK_A): if (!holdA) {tank.setAngle(tank.getAngle()-22.5f); holdA = true;} break;
						case (KeyEvent.VK_D): if (!holdD) {tank.setAngle(tank.getAngle()+22.5f); holdD = true;} break;
					}
					if (k.getKeyCode() == KeyEvent.VK_CONTROL) {
						Bullet bullet = new Bullet((float)(tank.getPosX() + 20), (float)(tank.getPosY() + 20), 9, -9, tank.getAngle(), true, ID.Bullet);
						addObj(bullet);
						
					}
				}
					
				if (tank.getID() == ID.TankRight) {
										
					/* Assigning control for right tank movement
					 * Player on the right side will move opposite direction on screen for horizontal movement
					 */
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_UP):   tank.setSpdX(-3); tank.setSpdY(3); break;
						case (KeyEvent.VK_DOWN): tank.setSpdX(3); tank.setSpdY(-3); break;
						case (KeyEvent.VK_LEFT):
							System.out.println(holdL);
							if (!holdL) {tank.setAngle(tank.getAngle()-22.5f); holdL = true;}
							System.out.println(holdL);
							break;
						case (KeyEvent.VK_RIGHT):if (!holdR) {tank.setAngle(tank.getAngle()+22.5f); holdR = true;} break;
					}
					if (k.getKeyCode() == KeyEvent.VK_ALT) {
						Bullet bullet = new Bullet((float)(tank.getPosX() +20), (float)(tank.getPosY() +20) , -9, 9, tank.getAngle(), false, ID.Bullet);
						addObj(bullet);
					}
				}
			// Single-player and training mode
			} else if (gms == 0 || gms == 2){
				if (tank.getID() == ID.TankLeft) {					
					// Assigning control for left tank movement
					
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_UP):   tank.setSpdX(3); tank.setSpdY(-3); break;
						case (KeyEvent.VK_DOWN): tank.setSpdX(-3); tank.setSpdY(3); break;
						case (KeyEvent.VK_LEFT):
							System.out.println(holdL);
							if (!holdL) {tank.setAngle(tank.getAngle()-22.5f); holdL = true;}
							System.out.println(holdL);
							break;
						case (KeyEvent.VK_RIGHT):if (!holdR) {tank.setAngle(tank.getAngle()+22.5f); holdR = true;} break;
					}
					if (k.getKeyCode() == KeyEvent.VK_ALT) {
						Bullet bullet = new Bullet((float)(tank.getPosX() +20), (float)(tank.getPosY() +20) , 9, -9, tank.getAngle(), false, ID.Bullet);
						addObj(bullet);
					}
				}
			}
					
		}
			
		/* The window will be closed immediately after
		 * pressing "Esc" key. We will add a pop up window
		 * before exiting the game later on.
		 */
		if (k.getKeyCode() ==  KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}	
	}
	
	/* method will be called if a key is released,
	 * and response according to what key was released.		
	 */
	public void keyReleased (KeyEvent k, int gms) {
		
		System.out.println("released");
	
		for (int i = 0 ; i < o.size(); i++) {
		
			Object tank = o.get(i);
			ID tankPreferred = ID.TankRight; ;
			// Single-player and training mode user will use mapping set 2 and play with left tank
			if (gms == 0 || gms == 2) {
				tankPreferred = ID.TankLeft;
			}
			
			if (tank.getID() == tankPreferred) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_DOWN): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_LEFT): tank.setAngle(tank.getAngle()); holdL = false; break;
					case (KeyEvent.VK_RIGHT):tank.setAngle(tank.getAngle()); holdR = false; break;
				}
				
			}
			
			if (tank.getID() == ID.TankLeft) {					
				// Assigning control for left tank movement
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_W): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_S): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_A): tank.setAngle(tank.getAngle()); holdA = false; break;
					case (KeyEvent.VK_D): tank.setAngle(tank.getAngle()); holdD = false; break;
				}
				
			}
			
			
		}
	}
	
}

