package Engine;

import java.util.ArrayList;
import java.util.HashMap;

import Components.Sprite;
/**
 * Manages the different scenes you create for the game on the GameStart
 * @author Pere
 */
public class SceneManager {
	private HashMap<String,Scene> scenes;
	private Scene loadedScene;
	private Engine engine;
	
	public SceneManager(Engine e) {
		this.engine=e;
		this.scenes= new HashMap<String,Scene>();
		this.loadedScene=new Scene("default");
		this.scenes.put("default", loadedScene);		
	}
	
	//Getters and Setters
	public HashMap<String,Scene> getScenes(){
		return scenes;
	}
	public void setScenes(HashMap<String,Scene> scenes){
		this.scenes=scenes;
	}
	public Scene getLoadedScene() {
		return loadedScene;
	}
	public ArrayList<GameObject> getSceneObjects(){
		return loadedScene.getObjects();
	}
	public Camera getSceneCamera(){
		return loadedScene.getCamera();
	}
	
	/**
	 * Loads the scene with the specified name if possible and save the current one
	 * @param name of the scene
	 * @return The loaded scene or null if the scene couldn't be loaded.
	 */
	public Scene loadScene(String name) {
		Scene t=scenes.get(name);
		if (t!=null) {
			scenes.put(loadedScene.getSceneName(), loadedScene);
			loadedScene=t;
			engine.loadScene(loadedScene);
			return loadedScene;
		}else {
			return null;
		}		
	}
	
	/**
	 * Reloads the currently loaded scene. Then reloads all sprites.
	 */
	public void reload() {
		loadedScene=scenes.get(loadedScene.getSceneName());
		engine.loadScene(loadedScene);
		ArrayList<GameObject>objects=loadedScene.getObjects();
		int length = objects.size();
		Sprite spr;
		for(int i=0; i<length; ++i) {
			spr=(Sprite) objects.get(i).getComponent("Sprite");
			if(spr!=null)
				spr.reloadImage();
		}
	}
	
	/**
	 * Add a scene to the scene ArrayList
	 * @param scene to be added
	 */
	public void addScene(Scene scene) {
		scenes.put(scene.getSceneName(), scene);
	}
	
	/**
	 * Delete a scene
	 * @param name of the scene
	 * @return true if the scene was successfully deleted, false otherwise
	 */
	public boolean deleteScene(String name) {
		Scene t=scenes.get(name);
		if(t==null || t==loadedScene)return false;
		t.getObjects().clear();
		t.setObjects(null);
		return scenes.remove(name)!=null;
	}
	
	/**
	 * Add an object to the loaded scene
	 * @param obj object to be added
	 */
	public void addObject(GameObject obj) {
		loadedScene.add(obj);
	}
	
	
}
