package main.game;

import java.awt.image.BufferStrategy;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;


public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	
	private static boolean GameRunning = false;
	private Thread thread;
	
	
	private static final long serialVersionUID = 5551732181250630703L;
	
	public synchronized void ThreadCreation(){
		//new Thread(new Game()).start();
		thread = new Thread(this);
		thread.start();
		
		GameRunning = true;
		System.out.println("Thread created and running");
	}
	
	public void run(){
		//A common game loop used which is the 'heart' of the game.
		
		int frames = 0;
		int updates = 0;
		double ticknumber = 60.0;
		double divider = 1000000000 / ticknumber;
		double difference = 0;
		
		long prevTime = System.nanoTime();
		long timeKeeper = System.currentTimeMillis();
		
		while(GameRunning = true){
			long currentTime = System.nanoTime();
			difference += (currentTime - prevTime) / divider;
			prevTime = currentTime;
			
			while(difference >= 1){
				difference = difference - 1;
				tick();
				updates++;
			}
			
			render();
			frames++;
			
			if(System.currentTimeMillis() - timeKeeper >= 1001){
				timeKeeper = timeKeeper + 1000;
				System.out.println(frames);
				System.out.println(updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	//Count frames (refreshes)
	private void tick(){
		
	}
	
	private void render(){
		BufferStrategy strats = this.getBufferStrategy();
		if (strats == null) {
			this.createBufferStrategy(3);
			return;
		}		
		Graphics g = strats.getDrawGraphics();
		
		//These are going to have a colored background and fill the game WindowFrame with it
		g.setColor(Color.green);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.dispose();
		strats.show();
	}
	
	public static void main(String args[]){
		new WindowFrame(WIDTH, HEIGHT, "Combat", new Game());
	}
}