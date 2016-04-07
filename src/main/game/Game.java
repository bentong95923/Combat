package main.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.body.KeyboardInput;
import main.body.Texture;


public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	
	static BufferedImage levelImg = null;
	
	private static boolean GameRunning = false;
	private Thread thread;
		
	private static final long serialVersionUID = 5551732181250630703L;
	 
	Random random = new Random();
	
	Handler handler;
	
	static Texture texture;
	
	public synchronized void ThreadCreation(){
		//new Thread(new Game()).start();
		thread = new Thread(this);
		thread.start();
		
		GameRunning = true;
		System.out.println("Thread created and running");
	}
	
	public void run(){
		//A common game loop used which is the 'heart' of the game.
		
		initialise();
		this.requestFocus();
		
		int FPS_SET = 30, countFrame = 0, numFrame = 0;
		
		long timeDiff, timeToWait, timeTotal = 0, avgFPS;
		
		long timeAim = 1000/FPS_SET;
		
		while(GameRunning) {
			long timeStart = System.nanoTime();
			
			tick();
			render();
			
			timeDiff = (System.nanoTime() - timeStart)/1000000;
			
			timeToWait = timeAim - timeDiff;
			
			try {
				Thread.sleep(timeToWait);
			} catch (Exception e) {}
			timeTotal += System.nanoTime() - timeStart;
			// Counting total the number of frames repainted
			numFrame++;
			
			// Find the FPS
			if (numFrame == FPS_SET) {
				// FPS = total number of frames / timetaken in nanosecond.
				avgFPS = 1000 / ((timeTotal/numFrame)/1000000);
				// Print out the FPS value for testing
				System.out.println("FPS: " + avgFPS);
				// Reset values for recalculating the FPS in the next second
				numFrame = 0;
				timeTotal = 0;
			}
		}
	}
	
	private void initialise() {
		
		texture = new Texture();
		
		handler = new Handler();
		
		// load the image of the level design
		loadLevelImage(levelImg);
		// generate map according to the loaded level design
		handler.makeLevel();
		
		// add key listener to detect any key input
		this.addKeyListener(new KeyboardInput(handler));
		
	}

	// This is the method which will update the game
	private void tick(){
		handler.tick();
	}
	
	// This is the method which will update the graphic
	private void render(){
		BufferStrategy strats = this.getBufferStrategy();
		if (strats == null) {
			this.createBufferStrategy(3);
			return;
		}		
		Graphics g = strats.getDrawGraphics();
		
		// These are going to have a colored background and fill the game WindowFrame with it
		g.setColor(Color.green);
		
		// Put a solid black square on top of the screen to prevent flicking
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		handler.render(g);;
		
		g.dispose();
		strats.show();
	}
	
	private void loadLevelImage(BufferedImage imageToLoad) {
		
		//int width = imageToLoad.getWidth();
		//int height = imageToLoad.getHeight();
		
		//System.out.println("width, height: " + width + " " + height);
		//handler.addObj(new Tank(50, 384, "blue", true, ID.TankLeft));
		//handler.addObj(new Tank(974, 384, "brown", false, ID.TankRight));
		
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public static void main(String args[]){
		new WindowFrame(WIDTH, HEIGHT, "Combat", new Game());
	}
}