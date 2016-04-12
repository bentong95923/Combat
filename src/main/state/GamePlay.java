package main.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.body.MapLoader;
import main.game.Handler;

public class GamePlay extends GameState {
	
	// Game mode state, 0: single-player, 1: multi-player, 2: Training
	private int gms;
	boolean player1win = false, player2win = false, isPaused = false, isContinue = true;
	// Count down from 3 is necessary before the game starts
	boolean needCountDown = true, gameFinish = false, requestQuit = false;
	int pauseCount = 60;
	
	int currentOption = 0;
	
	String[] yesNo = {"Yes", "No"};
	
	Handler handler = new Handler();
	MapLoader mapLoader = new MapLoader();
	public BufferedImage background;
	
	public GamePlay(StateManager sm, int gms) {
		this.sm = sm;
		this.gms = gms;
	}

	public void init() {
		// initialize background image for different game modes	
		switch(gms) {
		case 0:	background = imgLoader.loadingImage("/img/backgrounds/singleplayer.jpg"); break;
		case 1: background = imgLoader.loadingImage("/img/backgrounds/multiplayer.jpg"); break;
		case 2: background = imgLoader.loadingImage("/img/backgrounds/training.jpg"); break;
		}
		// initialize the map
		mapLoader.generateLevel(handler);
	}

	
	public void tick() {
		// Game will run if player(s) do(es) not pause the game
		if (!isPaused) {
			handler.tick();
		}
		
	}

	public void render(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		switch(gms) {
					// read getBackground method in Background class for state number
		case 0: break;	
		case 1: break;		
		case 2: break;		
		}
				
		g2d.drawImage(background, (int)0,(int)0,null);
		handler.render(g);
		
		if (isPaused) {
			pauseCount--;
			// Ask if player(s) want(s) to go back to main menu
			if (requestQuit) {
				
				Font pauseTitle = new Font("Showcard Gothic", Font.BOLD, 90);
				g.setFont(pauseTitle);
				g.setColor(Color.black);
				g.drawString("Back to Menu?", 330, 370);
				Font pausetext = new Font("Showcard Gothic", Font.BOLD, 45);
				g.setFont(pausetext);
				g.setColor(Color.black);
				for (int i = 0; i < yesNo.length; i++) {
					
					if (i == currentOption) {
						g.setColor(Color.blue);
					} else {
						g.setColor(Color.white);
					}
					g.drawString(yesNo[i], 650, 500 + (i * 50));
				}
			} else {
				// Display flash pause text
				if (pauseCount < 30) {
					// the user presses resume
					Font pauseTitle = new Font("Showcard Gothic", Font.BOLD, 90);
					g.setFont(pauseTitle);
					g.setColor(Color.black);
					g.drawString("PAUSED", 330, 370);
					Font pausetext = new Font("Showcard Gothic", Font.BOLD, 45);
					g.setFont(pausetext);
					g.setColor(Color.black);
					g.drawString("Press P to continue", 250, 470);
				}			
				if (pauseCount < 0) {
					pauseCount = 60;
				}
			}
			
		 }
					
	}

	public void keyPressed (KeyEvent k) {
		
		if (k.getKeyCode() == KeyEvent.VK_P) {
			// Pause game when P is pressed
			if (isContinue) {
				isPaused = true;
				isContinue = false;
			} else {
			// Resume game when P is pressed after the pause
				isPaused = false;
				isContinue = true;
				pauseCount = 60;
			}
		} else if (isContinue) {
			handler.keyPressed(k, gms);
		}
		
		// The window will be popped up for confirmation of exiting the game..
		if (k.getKeyCode() ==  KeyEvent.VK_ESCAPE) {
			if (isContinue == true && isPaused == false) {
				isPaused = true;
				isContinue = false;
				requestQuit = true;
				
			} else {
				isPaused = false;
				isContinue = true;
				if (k.getKeyCode() == KeyEvent.VK_UP) {
					currentOption--;
					if(currentOption < 0) {
						currentOption = yesNo.length - 1;
					}
				}
				if(k.getKeyCode() == KeyEvent.VK_DOWN){
					currentOption++;
					if(currentOption >= yesNo.length) {
						currentOption = 0;
					}
				
				}
				
				if(k.getKeyCode() == KeyEvent.VK_ENTER) {
					if (currentOption == 0) {
						sm.setState(sm.MENU);
					} else {
						isPaused = false;
						isContinue = true;
						requestQuit = false;
					}
				}
				
			
			}
		}
		
		if (k.getKeyCode() == KeyEvent.VK_PAGE_UP) {
			
		}
		
	}
	
	/* method will be called if a key is released,
	 * and response according to what key was released.		
	 */
	public void keyReleased (KeyEvent k) {		
		handler.keyReleased(k, gms);
	}
	
}
