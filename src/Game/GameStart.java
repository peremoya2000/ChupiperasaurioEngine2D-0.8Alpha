package Game;

import Engine.Camera;
import Engine.SceneManager;
import Engine.Transform;
//DO NOT DELETE
/**
 * This class belongs to the engine but has to be modified by the user. It executes automatically when runtime execution begins.
 */
public class GameStart {
	/**
	 * Constructor to script the game's initialization.
	 * @param sceneManager is used to modify or switch scenes
	 * @param camera received from Engine.
	 */
	public GameStart(SceneManager sceneManager, Camera camera) {
		camera.setCameraTransform(new Transform(0,0,0,0,1280,720));
		sceneManager.addObject(new Player(camera));
		sceneManager.addObject(new Wall(100,300));
		sceneManager.addObject(new Wall(-200,100));

	}
	
	/**
	 * Called when a scene is loaded.
	 */
	public void onSceneLoad() {
		
	}
}
