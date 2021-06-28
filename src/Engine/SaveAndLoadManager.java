package Engine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Class to manage saving and loading and make it accessible to all classes through static methods.
 * @author Pere
 */
public class SaveAndLoadManager {
	private static SceneManager sceneManager;
	public SaveAndLoadManager(SceneManager sm) {
		sceneManager=sm;
	}
	
	/**
	 * Saves all the scenes and a set of variables to a file.
	 * @param variables created by the user that want to be saved
	 */
	public static void save(HashMap<String,Object> variables) {
		try {
	         FileOutputStream fileOut = new FileOutputStream("src/GameAssets/save.pere");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(sceneManager.getScenes());
	         if (variables!=null) out.writeObject(variables);
	         out.close();
	         fileOut.close();
	      } catch (IOException i) {
	         i.printStackTrace();
	      }
	}
	
	/**
	 * Loads all scenes and reloads the current one so that the change is visible.
	 * @return variables saved by the user, null if there are none
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,Object> load() {
		HashMap<String,Object> variables = null;
		try {
			FileInputStream fileIn = new FileInputStream("src/GameAssets/save.pere");
	        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
	        HashMap<String, Scene> scenes = (HashMap<String, Scene>) objectIn.readObject();
	        if (scenes!=null) {
	        	sceneManager.setScenes(scenes);
	        	sceneManager.reload();
	        }
	        variables = (HashMap<String, Object>) objectIn.readObject();
	        objectIn.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return variables;
	}
	
	/**
	 * Deletes the save file
	 * @return True if the save was successfully deleted, false otherwise.
	 */
	public static boolean destroySave() {
		File save = new File("src/GameAssets/save.pere");
		return save.delete();
	}
}
