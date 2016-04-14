package main.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import main.body.BufferedImageLoader;
import main.body.Texture;
import main.state.StateManager;


public class Game extends Canvas implements Runnable, KeyListener {
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	public static final int FPS_SET = 30;
	
	
	private static boolean GameRunning = false;
	private Thread thread;
		
	private static final long serialVersionUID = 5551732181250630703L;
	
	BufferedImage loadingBg;
	
	BufferedImageLoader imgLoader = new BufferedImageLoader();
	
	// Game State Manager
	private StateManager sm;
	
	static Texture texture;
	
	Handler handler;
	
	public synchronized void ThreadCreation(){
		//new Thread(new Game()).start();
		thread = new Thread(this);
		thread.start();
		
		GameRunning = true;
		System.out.println("Thread created and running");
	}
	
	public void run(){
		//A common game loop used which is the 'heart' of the game.
		
		initialize();
		this.requestFocus();
		
		int numFrame = 0;
		
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
			} catch (Exception e) {
			}
			timeTotal += System.nanoTime() - timeStart;
			// Counting total the number of frames repainted
			numFrame++;
			
			// Find the FPS
			if (numFrame == FPS_SET) {
				// FPS = total number of frames / time taken in nanosecond.
				avgFPS = 1000 / ((timeTotal/numFrame)/1000000);
				// Print out the FPS value for testing
				System.out.println("FPS: " + avgFPS);
				// Reset values for recalculating the FPS in the next second
				numFrame = 0;
				timeTotal = 0;
			}
		}
	}
	
	private void initialize() {
		
		loadingBg = imgLoader.loadingImage("/img/backgrounds/loading.jpg");
		
		try {
			wait(2000);
		} catch(Exception e) {}
		// initialize texture
		texture = new Texture();
		// initialize handler
		handler = new Handler();
		
		sm = new StateManager();
		
		
		// add key listener to detect any key input
		addKeyListener(this);
		
	}

	// This is the method which will update the game
	private void tick(){
		
		sm.tick();
	}
	
	// This is the method which will paint the graphic
	private void render(){
		BufferStrategy strats = this.getBufferStrategy();
		if (strats == null) {
			this.createBufferStrategy(3);
			return;
		}		
		Graphics g = strats.getDrawGraphics();
		
		// Set loading background
		g.drawImage(loadingBg, 0, 0, WIDTH, HEIGHT, null);
		
		sm.render(g);
		
		g.dispose();
		strats.show();
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public static void main(String args[]){
		new WindowFrame(WIDTH, HEIGHT, "Combat", new Game());
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* Methods for the actions after a key is pressed/released.
	 * They are referred to the methods on handler class for
	 * the action logic for each key event.
	 */
	public void keyPressed(KeyEvent e) {
		sm.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		sm.keyReleased(e);
	}
}