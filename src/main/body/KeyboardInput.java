package main.body;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import main.game.Handler;

public class KeyboardInput extends KeyAdapter {
	
	Handler handler;
	
	public KeyboardInput(Handler handler) {
		this.handler = handler;
	}
	
	/* Methods for the actions after a key is pressed/released.
	 * They are referred to the methods on handler class for
	 * the action logic for each key event.
	 */
	public void keyPressed(KeyEvent k) {
		handler.keyPressed(k);
	}
	
	public void keyReleased(KeyEvent k) {
		handler.keyReleased(k);
	}
}
