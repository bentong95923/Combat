package main;

import javax.swing.*;

public class Game {

	public static void main(String[] args) {

		JFrame Window = new JFrame("Combat");
	
		Window.setLocationRelativeTo(null);  //Brings the window to the centre of the screen instead of left
		Window.setVisible(true);			//Visibility of windowFrame
		Window.setResizable(false);			//Disabling resizing abilities
		Window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
