package combat;

import java.awt.Graphics;
import java.util.LinkedList;

/* Refer to "Java Programming: Let's Build a Game #2"
 * URL: https://www.youtube.com/watch?v=0T1U0kbu1Sk
 */

/* Handler class updates all of objects in the game and
 * individually renders and displays on screen individually.
 */
public class Handler {
	
	LinkedList<Tank> tank_o = new LinkedList<Tank>();
	
	public void tick() {
		for (int i = 0; i < tank_o.size(); i++) {
			Tank tank1 = tank_o.get(i);
			tank1.tick();
		}
	}
	
	public void render(Graphics g) {
		
		for (int i = 0; i < tank_o.size(); i++) {
			Tank tank1 = tank_o.get(i);
			tank1.render(g);
		}
		
	}
	
	public void addTankObject(Tank tankO) {
		this.tank_o.add(tankO);
	}
	
	public void removeTankObject(Tank tankO) {
		this.tank_o.remove(tankO);
	}
}
