package main.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.object.Tank;
import main.object.Wall;

public class Handler {
	
	private Object testObj;
	public LinkedList<Object> o = new LinkedList<Object>();
	// Setting up control limit for better game play
	boolean holdL = false, holdR = false, holdA = false, holdD = false;
	
	public void tick() {
		
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);			
			testObj.tick(o);
		}
	}
	
	public void render(Graphics g) {
		
		for (int i = 0; i < o.size(); i++) {
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
	
	public void makeLevel() {
		
		// create an image loader
		BufferedImageLoader imageLoader = new BufferedImageLoader();
		// load level image
		Game.levelImg = imageLoader.loadingImage("/level/2.png");
		
			// Generate level according to the map picture
			for (int i = 0; i < Game.levelImg.getHeight(); i++) {
				for (int j = 0; j < Game.levelImg.getWidth(); j++) {
					
					Color px = new Color(Game.levelImg.getRGB(j, i));
					
					int red =  px.getRed();
					int green = px.getGreen();
					int blue = px.getBlue();
					
					ID tank;
					// true = left tank, false = right tank
					boolean leftOrRight;
					
					/* Identify left tank or right tank
					 * - the width of the picture of the map is 64px so 
					 *  half of it is 32px
					 */
					if (j <= 32) {
						leftOrRight = true; tank = ID.TankLeft;
					} else {
						leftOrRight = false; tank = ID.TankRight;
					}
					
					// generate black tank
					if (red == 51 && green == 51 && blue == 51) {
						addObj(new Tank(j*12, i*12, "black", leftOrRight, tank));	
					}
					
					// generate blue tank
					if (red == 00 && green == 00 && blue == 127) {
						addObj(new Tank(j*12, i*12, "blue", leftOrRight, tank));
					}
					
					// generate brown tank
					if (red == 127 && green == 00 && blue == 00) {
						addObj(new Tank(j*12, i*12, "brown", leftOrRight, tank));
					}
					
					// generate green tank
					if (red == 00 && green == 127 && blue == 00) {
						addObj(new Tank(j*12, i*12, "green", leftOrRight, tank));
					}
					
					// generate black wall
					if (red == 00 && green == 00 && blue == 00) {
						addObj(new Wall(j*12, i*12, "black", ID.Wall));
					}
					
					// generate blue wall
					if (red == 00 && green == 00 && blue == 255) {
						addObj(new Wall(j*12, i*12, "blue", ID.Wall));
					}
					
					// generate green wall
					if (red == 00 && green == 255 && blue == 00) {
						addObj(new Wall(j*12, i*12, "green", ID.Wall));
					}
					
					// generate red wall
					if (red == 255 && green == 00 && blue == 00) {
						addObj(new Wall(j*12, i*12, "red", ID.Wall));
					}
				}
			}
			
	}
	
	/* method will be called if a key is pressed,
	 * and response according to what key was pressed.
	 */
	
	public void keyPressed (KeyEvent k) {
		
		for (int i = 0 ; i < o.size(); i++) {
		
			Object tank = o.get(i);
				
			if (tank.getID() == ID.TankLeft) {					
				// Assigning control for left tank movement
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_W): tank.setSpdX(3); tank.setSpdY(-3); break;
					case (KeyEvent.VK_S): tank.setSpdX(-3); tank.setSpdY(3); break;
					case (KeyEvent.VK_A): if (!holdA) {tank.setAngle(tank.getAngle()-22.5f); holdA = true;} break;
					case (KeyEvent.VK_D): if (!holdD) {tank.setAngle(tank.getAngle()+22.5f); holdD = true;} break;
				}					
			}
				
			if (tank.getID() == ID.TankRight) {
									
				/* Assigning control for right tank movement
				 * Player on the right side will move opposite direction on screen for horizontal movement
				 */
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   tank.setSpdX(-3); tank.setSpdY(3); break;
					case (KeyEvent.VK_DOWN): tank.setSpdX(3); tank.setSpdY(-3); break;
					case (KeyEvent.VK_LEFT): if (!holdL) {tank.setAngle(tank.getAngle()-22.5f); holdL = true;} break;
					case (KeyEvent.VK_RIGHT):if (!holdR) {tank.setAngle(tank.getAngle()+22.5f); holdR = true;} break;
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
	public void keyReleased (KeyEvent k) {
	
		for (int i = 0 ; i < o.size(); i++) {
		
			Object tank = o.get(i);
			
				if (tank.getID() == ID.TankLeft) {
			
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_W): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_S): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_A): tank.setAngle(tank.getAngle()); holdA = false; break;
					case (KeyEvent.VK_D): tank.setAngle(tank.getAngle()); holdD = false; break;
				}
				
			}
			
			if (tank.getID() == ID.TankRight) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_DOWN): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_LEFT): tank.setAngle(tank.getAngle()); holdL = false; break;
					case (KeyEvent.VK_RIGHT):tank.setAngle(tank.getAngle()); holdR = false; break;
				}
				
			}
			
		}
	}
	
}

