package main.game;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;
import main.body.PowerUp;
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
		
	// Setting up control limit for better game play
	boolean holdL = false, holdR = false, holdA = false, holdD = false, firstPress1 = true, firstPress2 = true;
	boolean startCount1 = false, startCount2 = false, collisionRegL = false, collisionRegR = false;;
	int tickCount1 = 0, tickCount2 = 0;;
	public int p1score = 0, p2score = 0;
	int genTickCount1 = 0, genTickCount2 = 0;
	float tankLeftSpdX = 0, tankLeftSpdY = 0, tankRightSpdX = 0, tankRightSpdY = 0;
	Object tankNewR, tankNewL;
	
	public void tick() {
		
		tickCountTimer();
		checkTankSpdXY();
		
		// Players can only fire a bullet after each 0.5 sec
		limitFireRate();
				
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);			
			testObj.tick(o);
			
			if (testObj.getID() == ID.Tank) {
			
				if (((Tank)testObj).getCollisionTB() == true) {
					// left tank got hit by bullet
					if (((Tank)testObj).getLeftOrRight() == true) {
						collisionRegL = true;
						tankNewL = testObj;
						this.removeObj(testObj);
						startCount1 = true;
						p2score++;
					// right tank got hit by bullet
					} else {
						collisionRegR = true;
						tankNewR = testObj;
						this.removeObj(testObj);
						startCount2 = true;	
						p1score++;
					}
										
				}
				if (((Tank)testObj).getCollisionTT() == true) {
					
					collisionRegL = true;
					collisionRegR = true;
					if (((Tank)testObj).getLeftOrRight() == true) {
						tankNewL = testObj;
						((Tank)tankNewL).setSpdX(0);
						((Tank)tankNewL).setSpdY(0);
						this.removeObj(testObj);
						tankNewR = findAndRemoveOtherTank(o, false);
	
					}
					
					if (((Tank)testObj).getLeftOrRight() == false) {
						tankNewR = testObj;
						((Tank)tankNewR).setSpdX(0);
						((Tank)tankNewR).setSpdY(0);
						this.removeObj(testObj);
						tankNewL = findAndRemoveOtherTank(o, true);
	
					}
					startCount1 = true;
					startCount2 = true;
					
				}
			}
		}
		tankRespawn();
		
		/* Bullet only lasts within a time limit and it 
		 * will eventually disappear 
		 */
		removeLongTimeBullet();			
		
		
	}
	
	public void setPowerUpTimeOut() {
		for (int i = 0; i < o.size(); i++) {
			if ((o.get(i)).getID() == ID.PowerUp) {
				PowerUp powerup = (PowerUp) (o.get(i));
				if (!powerup.isTaken()) {
					powerup.setToBeRemoved();
				}
			}
		}
	}
	
	public void removePowerUps() {
		for (int i = 0; i < o.size(); i++) {
			if ((o.get(i)).getID() == ID.PowerUp) {
				PowerUp powerup = (PowerUp)(o.get(i));
				if (powerup.isToBeRemoved()) {
					removeObj(powerup);
				}
			}
		}
	}
	
	public void tankRespawn() {

		if (collisionRegL && tickCount1 > 30) {
			
			((Tank)tankNewL).findNewPosition(o, true);
			
			this.addObj(tankNewL);
			((Tank)tankNewL).resetCollisionTB();
			((Tank)tankNewL).resetCollisionTT();
			startCount1 = false;
			tickCount1 = 0;
			collisionRegL = false;				
		}
		
		if (collisionRegR && tickCount2 > 30) {

			((Tank)tankNewR).findNewPosition(o, false);
			
			this.addObj(tankNewR);
			((Tank)tankNewR).resetCollisionTB();
			((Tank)tankNewR).resetCollisionTT();
			startCount2 = false;
			tickCount2 = 0;
			collisionRegR = false;
		}
	}
		
	public Object findAndRemoveOtherTank(LinkedList<Object> o, boolean LeftOrRight) {
		Object tank = null;
		for (int i = 0; i < o.size(); i++) {
			if ((o.get(i)).getID() == ID.Tank) {
				tank = o.get(i);
				if (((Tank)tank).getLeftOrRight() == LeftOrRight) {
					this.removeObj(o.get(i));
					break;
				}
			}				
		}
		((Tank)tank).setSpdX(0);
		((Tank)tank).setSpdY(0);
		return tank;
	}
	
	public void checkTankSpdXY() {
		for (int i = 0; i < o.size(); i++) {
			if ((o.get(i)).getID() == ID.Tank) {
				Tank tank = (Tank)(o.get(i));
				if (tank.getLeftOrRight() == true) {
					tankLeftSpdX = tank.getTankSpdX();
					tankLeftSpdY = tank.getTankSpdY();
				} else {
					tankRightSpdX = tank.getTankSpdX();
					tankRightSpdY = tank.getTankSpdY();
				}
			}
		}
	}
	
	public boolean isTwoTanksTooClose() {
		Tank tankL, tankR;
		if (collisionRegL && collisionRegR) {
			tankL = (Tank)tankNewL;
			tankR = (Tank)tankNewR;
		} else if (collisionRegR) {
			tankL = getTank(true);
			tankR = (Tank)tankNewR;
		} else {
			tankL = (Tank)tankNewL;
			tankR = getTank(false);
		}
		if ( ( Math.abs(tankL.getPosX() - tankR.getPosX()) < 300 )
			&& ( Math.abs(tankL.getPosY() - tankR.getPosY()) < 200 ) ) {
			return true;
		} else {		
			return false;
		}
		
	}
	
	public Tank getTank(boolean leftOrRight) {
		for (int i = 0; i < o.size(); i++) {
			if (o.get(i).getID() == ID.Tank) {
				Tank tank = (Tank)(o.get(i));
				if (leftOrRight == tank.getLeftOrRight()) {
					return tank;
				}
			}
		}
		return null;
	}
	
	public void render(Graphics g) {
		
		for (int i = o.size() - 1; i > 0; i--) {
			testObj = o.get(i);
			testObj.render(g);			
		}
	}
	
	public void tickCountTimer() {
		if (startCount1) {
			tickCount1++;
		}		
		if (startCount2) {
			tickCount2++;
		}		
	}
	
	public void limitFireRate() {
		if (!firstPress1) {
			genTickCount1++;
		}
		if (!firstPress2) {
			genTickCount2++;
		}
		
		if (getTank(true) != null) {
			if (genTickCount1 > (30/getTank(true).getShootingRate())){
				genTickCount1 = 0;
				firstPress1 = true;
			}
		}
		if (getTank(false) != null) {
			
			if (genTickCount2 > (30/getTank(false).getShootingRate())) {
				genTickCount2 = 0;
				firstPress2 = true;
			}
		}
			
	}
		
	public void addObj(Object obj) {
		this.o.add(obj);
	}
	
	public void removeObj(Object obj) {
		this.o.remove(obj);
	}
	
	public void removeLongTimeBullet() {
		if (testObj.getID() == ID.Bullet) {
			if (((Bullet)testObj).getTickCount() > 103) {
				removeObj(testObj);
			}
		}
	}
		
	/* method will be called if a key is pressed,
	 * and response according to what key was pressed.
	 */
	
	public void keyPressed (KeyEvent k, int gms) {

		for (int i = 0 ; i < o.size(); i++) {
		
			Object tempObject = o.get(i);
			// Multi-player mode	
			if (gms == 1) {	
				
				if (tempObject.getID() == ID.Tank) {
					Tank tank = (Tank)tempObject;
					if (tank.getCollisionTB() == false && tank.getCollisionTT() == false) {
						// left tank control
						if (tank.getLeftOrRight() == true ) {
							// Assigning control for left tank movement
							switch ((int)k.getKeyCode()) {
								case (KeyEvent.VK_W): tank.setSpdX(tankLeftSpdX); tank.setSpdY(tankLeftSpdY); break;
								case (KeyEvent.VK_S): tank.setSpdX(-tankLeftSpdX); tank.setSpdY(-tankLeftSpdY); break;
								case (KeyEvent.VK_A): if (!holdA) {tank.setAngle(tank.getAngle()-22.5f); holdA = true;} break;
								case (KeyEvent.VK_D): if (!holdD) {tank.setAngle(tank.getAngle()+22.5f); holdD = true;} break;
							}
							if (k.getKeyCode() == KeyEvent.VK_CONTROL) {
								if (firstPress1) {									
									tank.firingBullets(this);
									firstPress1 = false;
								}
								
							}
						// right tank control
						} else {
												
							/* Assigning control for right tank movement
							 * Player on the right side will move opposite direction on screen for horizontal movement
							 */
							switch ((int)k.getKeyCode()) {
								case (KeyEvent.VK_UP):   tank.setSpdX(-tankLeftSpdX); tank.setSpdY(-tankLeftSpdY); break;
								case (KeyEvent.VK_DOWN): tank.setSpdX(tankLeftSpdX); tank.setSpdY(tankLeftSpdY); break;
								case (KeyEvent.VK_LEFT):
									if (!holdL) {tank.setAngle(tank.getAngle()-22.5f); holdL = true;}
									break;
								case (KeyEvent.VK_RIGHT):if (!holdR) {tank.setAngle(tank.getAngle()+22.5f); holdR = true;} break;
							}
							if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
								// Control Fire rate to 0.5s per bullet
								if (firstPress2) {
									tank.firingBullets(this);
									firstPress2 = false;
								}					
							}
						}
					}
				}
				
			// Single-player and training mode
			} else if (gms == 0 || gms == 2){
				
				// Only left tank can be controlled by the player
				if (tempObject.getID() == ID.Tank) {

					Tank tank = (Tank)tempObject;
					if (tank.getCollisionTB() == false && tank.getCollisionTT() == false) {
						// Assigning control for left tank movement
						if (tank.getLeftOrRight() == true) {
							switch ((int)k.getKeyCode()) {
								case (KeyEvent.VK_UP):   tank.setSpdX(tankLeftSpdX); tank.setSpdY(tankLeftSpdY); break;
								case (KeyEvent.VK_DOWN): tank.setSpdX(-tankLeftSpdX); tank.setSpdY(-tankLeftSpdY); break;
								case (KeyEvent.VK_LEFT):
									if (!holdL) {tank.setAngle(tank.getAngle()-22.5f); holdL = true;}
									break;
								case (KeyEvent.VK_RIGHT): if (!holdR) {tank.setAngle(tank.getAngle()+22.5f); holdR = true;} break;
							}
							// Control Fire rate to 0.5s per bullet
							if (firstPress1) {
								if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
									tank.firingBullets(this);
									firstPress1 = false;
								}
							}
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
			boolean tankPreferred = false;
			
			// Single-player and training mode user will use mapping set 2 and play with left tank
			if (gms == 0 || gms == 2) {
				tankPreferred = true;
			}
			
			if (tank.getID() == ID.Tank) {
				if (((Tank)tank).getLeftOrRight() == tankPreferred) {
					
					switch ((int)k.getKeyCode()) {
						case (KeyEvent.VK_UP):   tank.setSpdX(0); tank.setSpdY(0); break;
						case (KeyEvent.VK_DOWN): tank.setSpdX(0); tank.setSpdY(0); break;
						case (KeyEvent.VK_LEFT): tank.setAngle(tank.getAngle()); holdL = false; break;
						case (KeyEvent.VK_RIGHT):tank.setAngle(tank.getAngle()); holdR = false; break;
					}
					
				}
				
				if (((Tank)tank).getLeftOrRight() == true) {					
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
	
}

