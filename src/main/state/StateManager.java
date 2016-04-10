package main.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class StateManager {
	private ArrayList<GameState> gameStates;
	private int current;
	
	public static final int MENU = 0;
	public static final int HELP = 1;
	public static final int SINGLEPLAYER = 2;
	public static final int MULTIPLAYER = 3;
	public static final int TRAINING = 4;
	public static final int PAUSE = 5;
	public static final int EXIT = 6;
	
	
	public StateManager() {
		gameStates = new ArrayList<GameState>();
		current = MENU;
		// Menu
		gameStates.add(new MenuState(this));
		// Help
		gameStates.add(new Help(this));
		// Single-player
		gameStates.add(new GamePlay(this,0));
		// Multi-player
		gameStates.add(new GamePlay(this,1));
		// Training
		gameStates.add(new GamePlay(this,2));
		// Pause
		gameStates.add(new Pause(this));
		// Exit
		gameStates.add(new Exit(this));
	}
	
	public void setState(int state) {
		current = state;	
		gameStates.get(current).init();
	}
	
	public void setState(int state, int gms) {
		current = state;
		gameStates.get(current).init();
	}
	
	public void tick() {
		gameStates.get(current).tick();
	}
	
	public void render(Graphics g) {
		gameStates.get(current).render(g);
	}
	
	public void keyPressed(KeyEvent k) {
		gameStates.get(current).keyPressed(k);
	}
	
	public void keyReleased(KeyEvent k) {
		gameStates.get(current).keyReleased(k);
	}
}
