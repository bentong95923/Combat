package main.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import main.body.ID;
import main.body.KeyboardInput;
import main.body.Texture;
import main.object.Tank;


public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 1024;
	public static final int HEIGHT = 768;
	
	private BufferedImage tank = null;
	
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
//		long prevTime = System.nanoTime();
//		long timePoint = System.currentTimeMillis();
//		int TARGET_FPS = 30;
//		final double targetTime = 100000000/TARGET_FPS;
//		
//		double timeDiff = 0;
//		
//		int numFrame = 0, numUpdate = 0;
//		
//		while (GameRunning) {
//			
//			long currentTime = System.nanoTime();
//			timeDiff += (currentTime - prevTime)/targetTime;
//			prevTime = currentTime;
//			
//			while (timeDiff >= 1) {
//				// Update game
//				tick();
//				numUpdate++;
//				timeDiff--;
//			}
//			
//			render();
//			numFrame++;
//			
//			// Find the FPS
//			if (System.currentTimeMillis() - timePoint > 1000) {
//				timePoint += 1000;
//				System.out.println("FPS : " + numFrame + "TPS : " + numUpdate);
//				// reset values for recalculating the FPS in the next second
//				numFrame = 0;
//				numUpdate = 0;
//			}
//		}
		
	}
	
	private void initialise() {
		
		texture = new Texture();
		
		// create an image loader
		BufferedImageLoader imageLoader = new BufferedImageLoader();
		// load level image
		tank = imageLoader.loadingImage("/tank/left/tank_brown.png");
		
		handler = new Handler();
		
		// load tank image
		loadTankImage(tank);
		
		//LoadImage(tank);
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
		g.setColor(Color.black);
		
		// Put a solid black square on top of the screen to prevent flicking
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		handler.render(g);;
		
		g.dispose();
		strats.show();
	}
	
	private void loadTankImage(BufferedImage imageToLoad) {
		
		int width = imageToLoad.getWidth();
		int height = imageToLoad.getHeight();
		
		System.out.println("width, height: " + width + " " + height);
		handler.addObj(new Tank(50, 384, "blue", true, ID.TankLeft));
		handler.addObj(new Tank(974, 384, "brown", false, ID.TankRight));
		
		//generate level
		/*  
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				
				int px = imageToLoad.getRGB(i, j);
				int red = (px >> 16) & 0xff;
				int green = (px >> 8) & 0xff;
				int blue = (px) & 0xff;
				
				// generate level walls according to level design sheet
				if (red = xxxxx &&  green == xxxxx && blue = xxxxx) {
					handler.addObj(new Tank(0, 0, ID.TankLeft));
				}
				// generate tanks according to the level design sheet
				if (red = xxxxx &&  green == xxxxx && blue = xxxxx) {
					handler.addObj(Wall(posX, posY, String colorType, ID.Wall));
				}
				
			}
		}*/
		
		
	}
	
	public static Texture getTexture() {
		return texture;
	}
	
	public static void main(String args[]){
		new WindowFrame(WIDTH, HEIGHT, "Combat", new Game());
	}
}