package main.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import main.body.BufferedImageLoader;

public class Help extends GameState {

	private String[] options = {"Next", "Menu"};
	private String[] subOptions = {"Back", "Menu"};
	private boolean subHelpState = false;
	public int currentOption = 0, subCurrentOption = 0;

	public BufferedImage background;

	public Help(StateManager sm) {
		this.sm = sm;
	}
	
	public void init() {
		background = imgLoader.loadingImage("/img/help/help.jpg");		
	}

	public void tick() {
		// TODO Auto-generated method stub
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		// Display Help page

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(background, (int)0, (int)0, null);
    	
		Font selection = new Font("Courier", Font.BOLD, 45);
		g.setFont(selection);
		

		if (subHelpState == true) {
			for (int i = 0; i < subOptions.length; i++) {
				
				if (i == currentOption) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.white);
				}
				g.drawString(subOptions[i], 750, 600 + (i * 50));
			}
		} else {
			for (int j = 0; j < options.length; j++) {
				
				if (j == currentOption) {
					g.setColor(Color.blue);
				} else {
					g.setColor(Color.white);
				}
				g.drawString(options[j], 750, 600 + (j * 50));
			}
		}
	}
	

	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub
		
		if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			
			if (subHelpState == false) {
				switch(currentOption) {
				case 0: // Next button
					subHelpState = true; currentOption = 0; break;
				case 1: // To meun button
					currentOption = 1; sm.setState(StateManager.MENU); break;				
				}
			} else {
				switch(currentOption) {
				case 0: // Back button
					subHelpState = false; currentOption = 0; break;
				case 1: // To main meun button
					subHelpState = false; currentOption = 1;
					sm.setState(StateManager.MENU); break;
				
				}
			}
			
		}
		
		if(k.getKeyCode() == KeyEvent.VK_UP){
			currentOption--;
			if(currentOption < 0) {
				if (subHelpState == true) {
					currentOption = subOptions.length - 1;
				} else {
					currentOption = options.length - 1;
				}
			}
		}
		
		if(k.getKeyCode() == KeyEvent.VK_DOWN){
			currentOption++;
			if (subHelpState == true) {
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
