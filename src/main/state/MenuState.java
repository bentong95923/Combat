package main.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.body.BufferedImageLoader;

public class MenuState extends GameState {

	private String[] options = {"Play Game", "Help", "Quit"};
	private String[] subOptions = {"Singleplayer", "Multiplayer", "Training", "Back"};
	private boolean subMenuState = false;
	public int currentOption = 0, subCurrentOption = 0;

	public BufferedImage background;
	
	public MenuState(StateManager sm) {
		// TODO Auto-generated constructor stub
		this.sm = sm;
		init();
	}

	public void init() {
		BufferedImageLoader imgLoader = new BufferedImageLoader();		
		background = imgLoader.loadingImage("/img/backgrounds/menu.jpg"); 
	}

	public void tick() {
		
	}


	public void render(Graphics g){
		
		// Display background
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, (int)0, (int)0, null);
		
		Font title = new Font("Showcard Gothic", Font.BOLD, 120);
		g.setFont(title);
		g.setColor(Color.black);
		g.drawString("COMBAT", 100, 200);
		
		Font selection = new Font("Courier", Font.BOLD, 45);
		g.setFont(selection);
		
		if (subMenuState == true) {
			for (int i = 0; i < subOptions.length; i++) {
				
				if (i == currentOption) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.white);
				}
				g.drawString(subOptions[i], 650, 500 + (i * 50));
			}
		} else {
			for (int j = 0; j < options.length; j++) {
				
				if (j == currentOption) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.white);
				}
				g.drawString(options[j], 650, 500 + (j * 50));
			}
		}
	}


	public void keyPressed(KeyEvent k){
		if(k.getKeyCode() == KeyEvent.VK_ENTER){
			
			if (subMenuState == false) {
				switch(currentOption) {
				case 0: // Play game
					subMenuState = true; currentOption = 0; break;
				case 1: // Display help page
					sm.setState(StateManager.HELP); currentOption = 0; break;
				case 2: // Quit game
					System.exit(0); break;
				}
			} else {
				switch(currentOption) {
				case 0: // Single-player
					sm.setState(StateManager.SINGLEPLAYER, 0); break;
				case 1: // Multi-player
					sm.setState(StateManager.MULTIPLAYER, 1); break;
				case 2: // Training
					sm.setState(StateManager.TRAINING, 2); break;
				case 3: // back
					subMenuState = false; currentOption = 0; break;
				}
			}
			
		}
		if(k.getKeyCode() == KeyEvent.VK_UP){
			currentOption--;
			if(currentOption < 0) {
				if (subMenuState == true) {
					currentOption = subOptions.length - 1;
				} else {
					currentOption = options.length - 1;
				}
			}
		}
		if(k.getKeyCode() == KeyEvent.VK_DOWN){
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
			//sm.states.push(new Exit(sm));
			System.exit(0);
		}
	}
	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		
	}
	
}
