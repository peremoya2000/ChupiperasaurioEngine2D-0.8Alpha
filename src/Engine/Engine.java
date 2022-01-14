package Engine;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JFrame;

import Game.GameStart;
/**
 * Main class
 * @author Pere
 */
public class Engine extends JFrame{
	private static final long serialVersionUID = 1L;
	private ArrayList<GameObject> objects;
	private SceneManager sceneManager;
	private Viewer viewer;
	private GameObjectHandler gHandler;
	private GameStart start;
	//Engine instance
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Engine e = new Engine();
	}
	
	@SuppressWarnings("unused")
	/**
	 * Initialize the engine
	 */
	public Engine() {
		Input input = new Input();
		this.sceneManager= new SceneManager(this);
		SaveAndLoadManager saveManager = new SaveAndLoadManager(sceneManager);
		this.objects= sceneManager.getSceneObjects();	
		Camera cam = sceneManager.getSceneCamera();
		gHandler = new GameObjectHandler(objects);
		viewer = new Viewer(objects, cam);
		this.add(viewer);
		this.addKeyListener(input);
		this.addMouseListener(input);
		this.addFocusListener(input);
        this.setSize(1280, 720);        
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocus();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        start = new GameStart(sceneManager,cam);
        viewer.init();
        //Screen resize event
        this.addComponentListener(new ComponentAdapter() {  
            @Override
			public void componentResized(ComponentEvent evt) {
            	viewer.resizePetition();         	
            }
        });
        gHandler.init();
	}
	
	/**
	 * Load the scene into the viewer and the game object handler
	 * @param s The new scene to load
	 */
	public void loadScene(Scene s) {
		viewer.loadScene(s);
		gHandler.loadScene(s);
		start.onSceneLoad();
	}	

}
