package main.body;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.game.Handler;

public class KeyboardInput extends KeyAdapter {
	
	Handler handler;
	
	public KeyboardInput(Handler handler) {
		this.handler = handler;
	}
		
	// method will be called if a key is released.
	public void keyPressed (KeyEvent k) {
		
		for (int i = 0 ; i < handler.o.size(); i++) {
			
			Object tank = handler.o.get(i);
			
			if (tank.getID() == ID.TankLeft) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_W): tank.setSpdX(3); tank.setSpdY(-3); break;
					case (KeyEvent.VK_S): tank.setSpdX(-3); tank.setSpdY(3); break;
					case (KeyEvent.VK_A): tank.setAngularSpd((float)Math.toRadians(-22.5)); break;
					case (KeyEvent.VK_D): tank.setAngularSpd((float)Math.toRadians(22.5)); break;
				}				
			}
			
			if (tank.getID() == ID.TankRight) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   tank.setSpdX(3); tank.setSpdY(-3); break;
					case (KeyEvent.VK_DOWN): tank.setSpdX(-3); tank.setSpdY(3); break;
					case (KeyEvent.VK_LEFT): tank.setAngularSpd((float)Math.toRadians(-22.5)); break;
					case (KeyEvent.VK_RIGHT):tank.setAngularSpd((float)Math.toRadians(22.5)); break;
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
	
	// method will be called if a key is pressed.
	public void keyReleased (KeyEvent k) {
	
		for (int i = 0 ; i < handler.o.size(); i++) {
		
			Object tank = handler.o.get(i);
			
			if (tank.getID() == ID.TankLeft) {
			
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_W):   tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_S): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_A): tank.setAngularSpd(0); break;
					case (KeyEvent.VK_D):tank.setAngularSpd(0); break;
				}				
			}
			
			if (tank.getID() == ID.TankRight) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_DOWN): tank.setSpdX(0); tank.setSpdY(0); break;
					case (KeyEvent.VK_LEFT): tank.setAngularSpd(0); break;
					case (KeyEvent.VK_RIGHT):tank.setAngularSpd(0); break;
				}				
			}
			
		}
	}
}
