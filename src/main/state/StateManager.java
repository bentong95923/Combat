package main.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class StateManager {
	private ArrayList<GameState> gameStates;

	private List<Integer> randNumLog = new ArrayList<Integer>();
	private int current;
	
	public static final int MENU = 0;
	public static final int HELP = 1;
	public static final int SINGLEPLAYER = 2;
	public static final int MULTIPLAYER = 3;
	public static final int TRAINING = 4;
	public static final int EXIT = 5;
	
	public StateManager() {
		gameStates = new ArrayList<GameState>();
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
		// Exit (by default the exit screen is for single player)
		gameStates.add(new Exit(this,0, 0, 0));
		// Default is menu state
		current = MENU;
		

	}
	
	public void setState(int state) {
		// Refresh previous state
		refreshState(current, gameStates);
		current = state;	
		gameStates.get(current).init();
	}
	
	// This method will only be used to set state as "Exit"
	public void setState(int state, int gms, int score1, int score2) {
		// Refresh previous state
		refreshState(current, gameStates);
		current = state;
		gameStates.set(current, new Exit(this,gms, score1, score2));
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
	
	public void refreshState(int state, ArrayList<GameState> gameStates) {
		/* Set the new state which is to be refreshed to Help by default
		 * Menu state will not be refreshed.
		 */
		GameState freshState = new Help(this);
		switch(state) {
		case 0: freshState = new MenuState(this); break;
		case 2: freshState = new GamePlay(this,0); break;
		case 3: freshState = new GamePlay(this,1); break;
		case 4: freshState = new GamePlay(this,2); break;
		case 5: freshState = new Exit(this,0, 0, 0); break;
		}
		gameStates.set(current, freshState);		
	}

	public List<Integer> getRandNumLog() {
		return this.randNumLog;
	}

	public void clearRandNumLog() {
		this.randNumLog.clear();
	}
	
	public boolean RandNumLogContainElement(int element) {
		return this.randNumLog.contains(element);
	}

	public void RandNumLogAddElement(int element) {
		this.randNumLog.add(element);
	}
	
	public int getSizeRandNumLog() {
		return this.randNumLog.size();
	}
}
