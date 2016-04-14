package main.state;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Help extends GameState {

	private String[] options = {"Next", "Menu"};
	private String[] subOptions = {"Back", "Menu"};
	private boolean subHelpState = false, holdEnter = false, holdUp = false, holdDown = false, holdEsc = false;
	private boolean wait = true;
	public int currentOption = 0, subCurrentOption = 0;

	public List<BufferedImage> helpPg = new ArrayList<BufferedImage>();
	public List<BufferedImage> helpButton = new ArrayList<BufferedImage>();

	public Help(StateManager sm) {
		this.sm = sm;
	}
	
	public void init() {
		helpPg.add(imgLoader.loadingImage("/img/help/help.jpg"));
		helpPg.add(imgLoader.loadingImage("/img/help/help2.jpg"));
		for (int i = 0; i < 6; i++ ) {
			helpButton.add(imgLoader.loadingImage("/font/help/" + (i+1) +".png")); 
		}		
	}

	public void tick() {
		// wait for the background image to load
		if (wait) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			wait = false;
		}
		
	}

	public void render(Graphics g) {
		// TODO Auto-generated method stub
		// Display Help page

		Graphics2D g2d = (Graphics2D) g;
		int i = 0;
		if (subHelpState == true) {i = 1;} else {i = 0;}
			g2d.drawImage(helpPg.get(i), (int)0, (int)0, null);
    	
		if (subHelpState == true) {
			
			// Back button
			if (currentOption == 0 ) {
				g2d.drawImage(helpButton.get(1), 630, 675, null);

			} else {
				g2d.drawImage(helpButton.get(0), 630, 675, null);
				
			}
			
			// Menu button
			if (currentOption == 1 ) {
				g2d.drawImage(helpButton.get(3), 825, 675, null);

			} else {

				g2d.drawImage(helpButton.get(2), 825, 675, null);
			}			
			
		} else {
			
			// Next button
			if (currentOption == 0 ) {
				g2d.drawImage(helpButton.get(5),630, 675, null);

			} else {
				g2d.drawImage(helpButton.get(4),630, 675, null);
				
			}
			
			// Menu button
			if (currentOption == 1 ) {
				g2d.drawImage(helpButton.get(3), 825, 675, null);

			} else {

				g2d.drawImage(helpButton.get(2), 825, 675, null);
			}			
			
		}
	}
	

	public void keyPressed(KeyEvent k) {
		// TODO Auto-generated method stub
		
		if (!holdEnter && k.getKeyCode() == KeyEvent.VK_ENTER) {
			holdEnter = true;
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
		
		if(!holdUp && k.getKeyCode() == KeyEvent.VK_LEFT){
			holdUp = true;
			currentOption--;
			if(currentOption < 0) {
				if (subHelpState == true) {
					currentOption = subOptions.length - 1;
				} else {
					currentOption = options.length - 1;
				}
			}
		}
		
		if(!holdDown && k.getKeyCode() == KeyEvent.VK_RIGHT){
			holdDown = true;
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
		
		if(!holdEsc && k.getKeyCode() == KeyEvent.VK_ESCAPE){
			holdEsc = true;
			sm.setState(sm.MENU);
		}
		
	}

	public void keyReleased(KeyEvent k) {
		// TODO Auto-generated method stub
		switch(k.getKeyCode()) {
		case (KeyEvent.VK_ENTER): holdEnter = false; break;
		case (KeyEvent.VK_LEFT)  : holdUp = false; break;
		case (KeyEvent.VK_RIGHT): holdDown = false; break;
		}
		
	}

}
