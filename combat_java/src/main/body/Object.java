package main.body;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public abstract class Object {
	
	protected float posX, posY, spdX = 0, spdY = 0, angle = 0, w = 0, h = 0;
	protected int typeNum = 0;
	
	protected ID id;
	
	public Object(float x, float y, ID id) {		
		this.posX = x;
		this.posY = y;
		this.id = id;
	}
	
	public abstract void tick();
	public abstract void render(Graphics g);
	
	public void setSize(int width, int height) {
		this.w = width;
		this.h = height;
	}
	
	// Getting the hitbox of the object
	public abstract Rectangle getBounds();
	
	// make the hitbox can rotate with the tank
	public Area getHitbox() {
		AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(angle), posX+w*0.5, posY+h*0.5);
		return new Area(at.createTransformedShape(getBounds()));
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
		return spdX;
	}
	public float getSpdY() {
		return spdY;
	}
	
	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public int getTypeNum() {
		return typeNum;
	}
	
	public ID getID() {
		return id;
	}
	
}
