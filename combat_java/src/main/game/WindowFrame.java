package main.game;

import javax.swing.*;
import java.awt.*;

// This is the main WindowFrame class where the JFrame window's dimensions and features will be set.
public class WindowFrame extends Canvas {
	
	private static final long serialVersionUID = -152448970909146807L;

	WindowFrame(int width, int height, String name, Game combat){
		
		JFrame window = new JFrame(name);
		
		combat.setPreferredSize(new Dimension(width, height)); //Setting our preferred 
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Actually closes the JFrame when we press exit
		window.setResizable(false);			//Disabling resizing abilities
		window.add(combat);					//Adding the game to the new JFrame we made
		window.pack();
		window.setLocationRelativeTo(null); //Brings the window to the center of the screen instead of top left
		window.setVisible(true);			//Visibility of windowFrame
		combat.ThreadCreation();			//Starting the Thread creation and running it to execute game
	}
}