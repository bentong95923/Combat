package main.body;

public class Level {
	
	private int width, height;
	private int[] walls;
	
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		walls = new int[width * height];
		generateLevel();
	}
	
	private void generateLevel() {
		
	}
}
