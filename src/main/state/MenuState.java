package main.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MenuState extends GameState {

	private String[] options = {"Play Game", "Help", "Quit"};
	private String[] subOptions = {"Singleplayer", "Multiplayer", "Training", "Back"};
	private boolean subMenuState = false, holdEnter = false, holdUp = false, holdDown = false;
	public int currentOption = 0, subCurrentOption = 0;

	public BufferedImage background, title;
	
	public List<BufferedImage> menuButton = new ArrayList<BufferedImage>();
	
	public MenuState(StateManager sm) {
		// TODO Auto-generated constructor stub
		this.sm = sm;
		init();
	}

	public void init() {
		background = imgLoader.loadingImage("/img./backgrounds/menu.jpg"); 
		
		// Load font for main menu
		title = imgLoader.loadingImage("/font/menu/title.png");
		
		for (int i = 0; i < 16; i++) {
			menuButton.add(imgLoader.loadingImage("/font/menu/" + (i+1) +".png")); 
		}
		
	}

	public void tick() {
		
	}


	public void render(Graphics g){
		
		// Display background
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, (int)0, (int)0, null);
		
		g2d.drawImage(title, 100, 100, null);
		
		int respCurrentPos = 0;
		
		if (subMenuState == true) {
			for (int i = 10; i < 16; i+=2) {
				
				if (currentOption == respCurrentPos) {
					g2d.drawImage(menuButton.get(i+1), 550, 350 + respCurrentPos * 80, null);
				} else {
					g2d.drawImage(menuButton.get(i), 550, 350 + respCurrentPos * 80, null);
				}
				respCurrentPos++;
			}
			
			if (currentOption == 3) {
				g2d.drawImage(menuButton.get(7), 550, 350 + respCurrentPos * 90, null);
			} else {
				g2d.drawImage(menuButton.get(6), 550, 350 + respCurrentPos * 90, null);
			}
			
		} else {
			for (int j = 0; j < 6; j+=2) {
				
				if (currentOption == respCurrentPos) {
					g2d.drawImage(menuButton.get(j+1), 550, 350 + respCurrentPos * 107, null);
				} else {
					g2d.drawImage(menuButton.get(j), 550, 350 + respCurrentPos * 107, null);
				}
				respCurrentPos++;
			}
		}
	}


	public void keyPressed(KeyEvent k){
		if(!holdEnter && k.getKeyCode() == KeyEvent.VK_ENTER){
			holdEnter = true;
			if (subMenuState == false) {
				switch(currentOption) {
				case 0: // Play game
					subMenuState = true; currentOption = 0; break;
				case 1: // Display help page
					sm.setState(StateManager.HELP); currentOption = 1; break;
				case 2: // Quit game
					System.exit(0); break;
				}
			} else {
				switch(currentOption) {
				case 0: // Single-player
					
					sm.setState(StateManager.SINGLEPLAYER);
					subMenuState = false;
					currentOption = 0;
					break;
				case 1: // Multi-player
					sm.setState(StateManager.MULTIPLAYER);
					subMenuState = false;
					currentOption = 0;
					break;
				case 2: // Training
					sm.setState(StateManager.TRAINING);
					subMenuState = false;
					currentOption = 0;
					break;
				case 3: // back
					subMenuState = false; 
					subMenuState = false;
					currentOption = 0;
					break;
				}
			}
			
		}
		if(!holdUp && k.getKeyCode() == KeyEvent.VK_UP){
			holdUp = true;
			currentOption--;
			if(currentOption < 0) {
				if (subMenuState == true) {
					currentOption = subOptions.length - 1;
				} else {
					currentOption = options.length - 1;
				}
			}
		}
		if(!holdDown && k.getKeyCode() == KeyEvent.VK_DOWN){
			holdDown = true;
			currentOption++;
			if (subMenuState == true) {
				if(currentOption >= subOptions.length) {
					currentOption = 0;
				}
			} else {
				if(currentOption >= options.length) {
					currentOption = 0;
				}
			}
		}
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE){
			if (subMenuState == true) {
				subMenuState = false; currentOption = 0;
			} else {
				System.exit(0);
			}
		}
	}
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		switch(k.getKeyCode()) {
		case (KeyEvent.VK_ENTER): holdEnter = false; break;
		case (KeyEvent.VK_UP)  : holdUp = false; break;
		case (KeyEvent.VK_DOWN): holdDown = false; break;
		}
		
		
	}
	
}
