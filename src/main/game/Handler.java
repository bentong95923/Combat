package main.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.object.Bullet;

public class Handler {
	
	/* This class will handle all the objects in the game
	 * (including object movement control) by storing all
	 *  objects to be used in a link list. It can add or
	 *  remove objects as required.
	 */
	
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
					if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
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
					if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
						Bullet bullet = new Bullet((float)(tank.getPosX() +20), (float)(tank.getPosY() +20) , 9, -9, tank.getAngle(), true, ID.Bullet);
						addObj(bullet);
					}
				}
			}
					
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

