package main.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.object.Wall;

public class Handler {
	
	private Object testObj;
	public LinkedList<Object> o = new LinkedList<Object>();
	
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
		// square at the bottom of the screen
		for (int a = 0; a <= Game.WIDTH+31 ; a += 32 ) {
			addObj(new Wall(a, Game.HEIGHT-64, ID.Wall));
		}
	}
	
	/* method will be called if a key is pressed,
	 * and response according to what key was pressed.
	 */
	
		public void keyPressed (KeyEvent k) {
			
			for (int i = 0 ; i < o.size(); i++) {
				
				Object tank = o.get(i);
				
				if (tank.getID() == ID.TankLeft) {
					
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_W): tank.setSpdX(3); tank.setSpdY(-3); break;
						case (KeyEvent.VK_S): tank.setSpdX(-3); tank.setSpdY(3); break;
						case (KeyEvent.VK_A): tank.setAngle(tank.getAngle()-22.5f); break;
						case (KeyEvent.VK_D): tank.setAngle(tank.getAngle()+22.5f); break;
					}				
				}
				
				if (tank.getID() == ID.TankRight) {
					// Player on the right side will move opposite direction on screen for horizontal movement
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_UP):   tank.setSpdX(-3); tank.setSpdY(3); break;
						case (KeyEvent.VK_DOWN): tank.setSpdX(3); tank.setSpdY(-3); break;
						case (KeyEvent.VK_LEFT): tank.setAngle(tank.getAngle()-22.5f); break;
						case (KeyEvent.VK_RIGHT):tank.setAngle(tank.getAngle()+22.5f); break;
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
						case (KeyEvent.VK_A): tank.setAngle(tank.getAngle()); break;
						case (KeyEvent.VK_D): tank.setAngle(tank.getAngle()); break;
					}				
				}
				
				if (tank.getID() == ID.TankRight) {
					
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_UP):   tank.setSpdX(0); tank.setSpdY(0); break;
						case (KeyEvent.VK_DOWN): tank.setSpdX(0); tank.setSpdY(0); break;
						case (KeyEvent.VK_LEFT): tank.setAngle(tank.getAngle()); break;
						case (KeyEvent.VK_RIGHT):tank.setAngle(tank.getAngle()); break;
					}				
				}
				
			}
		}
	
}

