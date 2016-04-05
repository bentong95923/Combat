package main.game;

import java.awt.Graphics;
import java.util.LinkedList;

<<<<<<< HEAD
import main.body.Object;
=======
import main.body.ID;
import main.body.Object;
import main.object.Wall;
>>>>>>> testing_ben

public class Handler {
	
	private Object testObj;
	public LinkedList<Object> o = new LinkedList<Object>();
	
	public void tick() {
		
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);			
			testObj.tick(o);
		}
	}
	
	public void render(Graphics g) {
		
		for (int i = 0; i < o.size(); i++) {
			testObj = o.get(i);
			testObj.render(g);			
		}
	}
	
	public void addObj(Object obj) {
		this.o.add(obj);
	}
	
	public void removeObj(Object obj) {
		this.o.remove(obj);
	}
	
<<<<<<< HEAD
=======
	public void makeLevel() {
		// square at the bottom of the screen
		for (int a = 0; a <= Game.WIDTH+31 ; a += 32 ) {
			addObj(new Wall(a, Game.HEIGHT-64, ID.Wall));
		}
	}
>>>>>>> testing_ben
	
}

