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
			
			Object focusObj = handler.o.get(i);
			
			if (focusObj.getID() == ID.Tank) {
				
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   focusObj.setSpdX(3); focusObj.setSpdY(-3); break;
					case (KeyEvent.VK_DOWN): focusObj.setSpdX(-3); focusObj.setSpdY(3); break;
					case (KeyEvent.VK_LEFT): focusObj.setAngularSpd((float)Math.toRadians(-22.5)); break;
					case (KeyEvent.VK_RIGHT):focusObj.setAngularSpd((float)Math.toRadians(22.5)); break;
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
		
			Object focusObj = handler.o.get(i);
		
			if (focusObj.getID() == ID.Tank) {
			
				switch ((int)k.getKeyCode()) {
					case (KeyEvent.VK_UP):   focusObj.setSpdX(0); focusObj.setSpdY(0); break;
					case (KeyEvent.VK_DOWN): focusObj.setSpdX(0); focusObj.setSpdY(0); break;
					case (KeyEvent.VK_LEFT): focusObj.setAngularSpd(0); break;
					case (KeyEvent.VK_RIGHT):focusObj.setAngularSpd(0); break;
				}				
			}
		}
	}
}
