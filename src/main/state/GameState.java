package main.state;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import main.body.BufferedImageLoader;

public abstract class GameState {
	protected StateManager sm;
	BufferedImageLoader imgLoader = new BufferedImageLoader();
	public abstract void init();
	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void keyPressed(KeyEvent k);
	public abstract void keyReleased(KeyEvent k);
}
