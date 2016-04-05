package main.body;

import java.awt.Graphics;
<<<<<<< HEAD
=======
import java.awt.Rectangle;
>>>>>>> testing_ben
import java.util.LinkedList;

public abstract class Object {
	
<<<<<<< HEAD
	protected float posX, posY, spdX = 0, spdY = 0;
=======
	protected float posX, posY, spdX = 0, spdY = 0, angle = 0, angularSpd = 0;
>>>>>>> testing_ben
	
	protected ID id;
	
	public Object(float x, float y, ID id) {		
		this.posX = x;
		this.posY = y;
		this.id = id;
	}
	
	public abstract void tick(LinkedList<Object> object);
	public abstract void render(Graphics g);
<<<<<<< HEAD

	public abstract void setPosX(float x);
	public abstract void setPosY(float y);
	
	public abstract float getPosX();
	public abstract float getPosY();
	
	public abstract void setSpdX(float x);
	public abstract void setSpdY(float y);
	
	public abstract float getSpdX();
	public abstract float getSpdy();
	
	public abstract ID getID();	
=======
	
	public abstract Rectangle getBounds();
	
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
		return spdX;
	}
	public float getSpdy() {
		return spdY;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void setAngularSpd(float angularSpd) {
		this.angularSpd = angularSpd;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public float getAngularSpd() {
		return angularSpd;
	}
		
	public ID getID() {
		return id;
	}
>>>>>>> testing_ben
	
}
