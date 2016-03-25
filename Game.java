package combat;

import java.awt.Canvas;

public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 1024, HEIGHT = 768;
	private static final long serialVersionUID = 3818541701527953868L;
	
	public Game(){
		new Window(WIDTH, HEIGHT, "Combat", this);
	}
	
	public synchronized void start(){
		
	}
	
	public void run(){
		
	}
	
	public static void main(String args[]) {
		new Game();
	}

}
