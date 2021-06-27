package Engine;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Contains a scene's objects and camera
 * @author Pere
 */
@SuppressWarnings("serial")
public class Scene implements Serializable{
	private String sceneName;
	private ArrayList<GameObject> objects;	
	private Camera camera;
	
	public Scene(String name) {
		objects = new ArrayList<GameObject>();
		sceneName=name;
		camera=new Camera();
	}
	
	public Scene(String name,  Camera cam) {
		objects = new ArrayList<GameObject>();
		sceneName=name;
		camera=cam;
		if(camera==null)camera=new Camera();
	}
	
	public Scene(String name, ArrayList<GameObject> objs, Camera cam) {
		objects = objs;
		sceneName=name;
		camera=cam;
		if(camera==null)camera=new Camera();
	}
	
	public ArrayList<GameObject> getObjects(){
		return objects;
	}	
	public void setObjects(ArrayList<GameObject> o) {
		this.objects=o;
	}
	public String getSceneName() {
		return sceneName;
	}
	public void setSceneName(String newName) {
		this.sceneName=newName;
	}
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	/**
	 * Add an object to the scene
	 * @param obj object to add
	 */
	public void add(GameObject obj) {
		short depth = obj.getTransform().getDepth();
		int max = objects.size();
		if (depth==0 || max<1) {
			objects.add(obj);
		}else {
			//Binary search			
			int min = 0;
			int guess = max/2;
			while(min<max-1) {
				if (objects.get(guess).getTransform().getDepth() < depth) {
					min = guess;
					guess = (max + guess) / 2;
				} else if (objects.get(guess).getTransform().getDepth() > depth) {
					max = guess;
					guess = (min + guess) / 2;
				}else {
					break;
				}
			}
			objects.add(guess,obj);
			System.out.println(guess);
		}
	}
	
	/**
	 * Remove an object from the scene
	 * @param obj object to remove
	 */
	public void remove(GameObject obj) {
		objects.remove(obj);
	}
	
	/**
	 * Called when a scene is loaded
	 */
	public void onLoad() {
		
	}

}

