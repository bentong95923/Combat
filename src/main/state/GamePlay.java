package main.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.body.Level;
import main.game.Handler;

public class GamePlay extends GameState {
	
	// Game mode state, 0: single-player, 1: multi-player, 2: Training
	private int gms;
	StateManager sm;	
	
	Handler handler;
	// Setting up control limit for better game play
	boolean holdL = false, holdR = false, holdA = false, holdD = false;
	
	public GamePlay(StateManager sm, int gms) {
		this.sm = sm;
		this.gms = gms;
	}

	public void init() {
		// TODO Auto-generated method stub
		handler = new Handler();
		handler.generateLevel();
	}

	
	public void tick() {
		// TODO Auto-generated method stub
		handler.tick();
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		handler.render(g);		
	}


	public void keyPressed (KeyEvent k) {
		handler.keyPressed(k, gms);
	}
	
	/* method will be called if a key is released,
	 * and response according to what key was released.		
	 */
	public void keyReleased (KeyEvent k) {		
		handler.keyReleased(k, gms);
	}
	
}
