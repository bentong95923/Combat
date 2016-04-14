package main.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import main.body.ID;
import main.body.Object;
import main.object.Bullet;
import main.object.Tank;

public class Handler {
	
	/* This class will handle all the objects in the game
	 * (including object movement control) by storing all
	 *  objects to be used in a link list. It can add or
	 *  remove objects as required.
	 */
	
	private Object testObj;
	public LinkedList<Object> o = new LinkedList<Object>();
	
	Random randNumGen = new Random();
		
	// Setting up control limit for better game play
	boolean holdL = false, holdR = false, holdA = false, holdD = false, firstPress1 = true, firstPress2 = true;
	boolean startCount = false, collisionRegL = false, collisionRegR = false;;
	int tickCount = 0;
	public int p1score = 0, p2score = 0;
	int genTickCount1 = 0, genTickCount2 = 0;
	Object tankNewR, tankNewL;
	
	public void tick() {
		if (startCount) {
			tickCount++;
		}
		
		if (!firstPress1) {
			genTickCount1++;
		}
		if (!firstPress2) {
			genTickCount2++;
		}
		
		if (genTickCount1 > 15) {
			genTickCount1 = 0;
			firstPress1 = true;
		}
		
		if (genTickCount2 > 15) {
			genTickCount2 = 0;
			firstPress2 = true;
		}
		
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);			
			testObj.tick(o);
			
			if (testObj.getID() == ID.TankLeft || testObj.getID() == ID.TankRight) {
				if (((Tank) testObj).getCollisionTB() == true) {
					if (testObj.getID() == ID.TankRight) {
						collisionRegR = true;
						tankNewR  = testObj;
					} else {
						collisionRegL = true;
						tankNewL  = testObj;
					}
						this.removeObj(testObj);
						startCount = true;					
				}
			}
			
			if (collisionRegR && tickCount > 30) {
				this.addObj(tankNewR);
				((Tank)tankNewR).resetCollisionTB();
				startCount = false;
				tickCount = 0;
				collisionRegR = false;
			}
			if (collisionRegL && tickCount > 30) {
				this.addObj(tankNewL);
				((Tank)tankNewL).resetCollisionTB();
				startCount = false;
				tickCount = 0;
				collisionRegL = false;
				
			}
			
			if (testObj.getID() == ID.Bullet) {
				if (((Bullet)testObj).getTickCount() > 100) {
					removeObj(testObj);
				}
			}
			
			if (testObj.getID() == ID.TankLeft ) {
				if (((Tank)testObj).getCollisionTB()) {
					p2score++;
				}
			} else if (testObj.getID() == ID.TankRight) {
				if (((Tank)testObj).getCollisionTB()) {
					p1score++;
				}
			}
			
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
						if (firstPress1) {
							Bullet bullet = new Bullet((float)(tank.getPosX() + 20), (float)(tank.getPosY() + 20), 9, -9, tank.getAngle(), this, true, ID.Bullet);
							addObj(bullet);
							firstPress1 = false;
						}
						
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
						// Control Fire rate to 0.5s per bullet
						if (firstPress2) {
							Bullet bullet = new Bullet((float)(tank.getPosX() +20), (float)(tank.getPosY() +20) , -9, 9, tank.getAngle(), this, false, ID.Bullet);
							addObj(bullet);
							firstPress2 = false;
						}					
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
					// Control Fire rate to 0.5s per bullet
					if (firstPress1) {
						if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
							Bullet bullet = new Bullet((float)(tank.getPosX() +20), (float)(tank.getPosY() +20) , 9, -9, tank.getAngle(), this, true, ID.Bullet);
							addObj(bullet);
							firstPress1 = false;
						}
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

