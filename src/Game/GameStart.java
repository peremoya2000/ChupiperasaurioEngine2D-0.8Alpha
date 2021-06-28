package Game;

import Engine.Camera;
import Engine.SceneManager;
import Engine.Transform;

/**
 * This class belongs to the engine but has to be modified by the user.
 */
//DO NOT DELETE
public class GameStart {
	/**
	 * Constructor to script the game's initialization.
	 * @param obects The list of objects received from Engine so that objects can be added.
	 * @param camera received from Engine.
	 */
	public GameStart(SceneManager sceneManager, Camera camera) {
		camera.setCameraTransform(new Transform(0,0,0,0,1280,720));
		sceneManager.addObject(new Player(camera));
		sceneManager.addObject(new Wall(100,200));
		sceneManager.addObject(new Wall(-200,100));
	}
}
