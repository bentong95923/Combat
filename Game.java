package combat;

import java.awt.Canvas;

/* Refer from "Java Programming: Let's Build a Game #1" for freating the
 * window display and synchronize the frame rate
 * URL: https://www.youtube.com/watch?v=1gir2R7G9ws
 */
public class Game extends Canvas implements Runnable{
	
	public static final int WIDTH = 1024, HEIGHT = 768;
	private static final long serialVersionUID = 3818541701527953868L;
	
	private Thread thread;
	private boolean running = false;
	
	public Game(){
		new Window(WIDTH, HEIGHT, "Combat", this);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try {
			thread.join();
			running = false;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 6.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while (running) {
			long now = System.nanoTime();
			delta+= (now - lastTime) /ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				delta--;
			}
			if(running) {
				render();
				frames++;
			}
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("FPS " + frames);
				frames = 0;
			}
		}
		stop();
	}
	
	
	
	
	
	private void tick() {
		// TODO Auto-generated method stub
		
	}

	private void render() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) {
		new Game();
	}

}
