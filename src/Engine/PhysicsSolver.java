package Engine;

import java.util.ArrayList;

import Components.Collision;
/**
 * Handles the collision checking and position correction
 * @author Usuario
 */
public class PhysicsSolver{
	private ArrayList<GameObject> objects;
	Collision o1;
	PhysicsSolver(){
		this.objects= new ArrayList<GameObject>();
	}
	PhysicsSolver(ArrayList<GameObject> o){
		this.objects= o;
	}
	
	/**
	 * Called inside the gameplay loop
	 * @param o input GameObject
	 */
	public void physicsTick(GameObject o) {
		o1=(Collision) o.getComponent("Collision");
		Collision o2;
		if(o1!=null) {
			int size = objects.size();
			for (int i=0; i<size; ++i) {
				o2 =(Collision) objects.get(i).getComponent("Collision");
				if(o2!=null && o1!=o2) {
					o1.handleCollision(o2);
				}
			}
		}
	}
}
