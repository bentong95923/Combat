package main.body;

import java.awt.Graphics;
import java.util.LinkedList;

public abstract class Object {
	
	protected float posX, posY, spdX = 0, spdY = 0;
	
	protected ID id;
	
	public Object(float x, float y, ID id) {		
		this.posX = x;
		this.posY = y;
		this.id = id;
	}
	
	public abstract void tick(LinkedList<Object> object);
	public abstract void render(Graphics g);

	public abstract void setPosX(float x);
	public abstract void setPosY(float y);
	
	public abstract float getPosX();
	public abstract float getPosY();
	
	public abstract void setSpdX(float x);
	public abstract void setSpdY(float y);
	
	public abstract float getSpdX();
	public abstract float getSpdy();
	
	public abstract ID getID();	
	
}
