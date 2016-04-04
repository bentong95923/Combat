package main.object;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import main.body.ID;
import main.body.Object;

public class Test extends Object  {

	public Test(float x, float y, ID id) {
		super(x, y, id);
		
	}

	public void tick(LinkedList<Object> object) {
		
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect((int)posX, (int)posY, 32, 32);
	}

		public void setPosX(float x) {	
		this.posX = x;
	}

		public void setPosY(float y) {
		this.posY = y;
	}

		public float getPosX() {		
		return posX;
	}
	
	public float getPosY() {		
		return posY;
	}
	
	public void setSpdX(float x) {
		this.spdX = x;
	}
	
	public void setSpdY(float y) {
		this.spdY = y;
	}
	
	public float getSpdX() {		
		return 0;
	}
	
	public float getSpdy() {		
		return spdY;
	}
	
	public ID getID() {
		return id;
	}

}
