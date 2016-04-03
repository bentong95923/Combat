package main.game;

import javax.swing.*;
import java.awt.*;

//This is the main WindowFrame class where the JFrame window's dimensions and features will be setted.
public class WindowFrame extends Canvas {
	
	private static final long serialVersionUID = -152448970909146807L;

	WindowFrame(int width, int height, String name, Game combat){
		
		combat.setPreferredSize(new Dimension(width, height));
		
		JFrame window = new JFrame(name);
		
		window.add(combat);					//Adding the game to the new JFrame we made
		window.pack();
		window.setLocationRelativeTo(null); //Brings the window to the center of the screen instead of top left
		window.setResizable(false);			//Disabling resizing abilities
		window.setVisible(true);			//Visibility of windowFrame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Actually closes the JFrame when we press exit
		combat.ThreadCreation();			//Starting the Thread creation and running it to execute game
	}
}