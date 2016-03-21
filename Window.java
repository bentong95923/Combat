package combat;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends Canvas{
	
	/*Got from "Java Programming: Let's build a game" youtube clip by 'RealTutsGML'
	https://www.youtube.com/watch?v=1gir2R7G9ws
	which is used for creating a set window frame for the entire game. This is going to be set as a static boundary*/
	private static final long serialVersionUID = 137118081456217119L;
		
	public Window(int height, int width, String windowTitle, Game combat){
		JFrame mainWindow = new JFrame(windowTitle);
		
		mainWindow.setPreferredSize(new Dimension(height,width));
		mainWindow.setPreferredSize(new Dimension(height,width));
		mainWindow.setPreferredSize(new Dimension(height,width));
	
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setResizable(false);
		mainWindow.add(combat);
		mainWindow.setVisible(true);
		combat.start();
	}
}
