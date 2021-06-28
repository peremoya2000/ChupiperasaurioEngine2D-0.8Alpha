package Engine;

import java.util.ArrayList;
/**
 * Loops through the objects each tick, executes their code and calls the PhysicsSolver
 * @author Pere
 */
public class GameObjectHandler implements Runnable{
	private Thread gameThread;
	private boolean play;
	private long lastTime;
	private float delta;
	private ArrayList<GameObject> objects;
	private PhysicsSolver physics;
	
	public GameObjectHandler(ArrayList<GameObject> o) {
		this.objects= o;
		this.physics= new PhysicsSolver(o);
		this.gameThread= new Thread(this);
	}
	
	/**
	 * Start the game Thread
	 */
	public void init() {
		this.play=true;
		this.lastTime=System.currentTimeMillis();
		this.gameThread.start();
	}
	/**
	 * Check if the game's logic loop is running
	 * @return game loop started
	 */
	public boolean isPlay() {
		return play;
	}
	/**
	 * Start or stop the game loop
	 * @param play whether to tick or finish ticking
	 */
	public void setPlay(boolean play) {
		this.play = play;
	}
	
	/**
	 * Get the delta time between game ticks
	 * @return delta time
	 */
	public float getDelta() {
		return delta;
	}
	
	/**
	 * Load a new object list
	 * @param newScene to load
	 */
	public void loadScene(Scene newScene) {
		this.objects=newScene.getObjects();
	}
	
	//Tick loop
	@Override
	public void run() {
		long now;
		int size;
		while(play) {
			now=System.currentTimeMillis();
			//get a delta to automatically apply to most actions
			delta=(now-lastTime)/1000.f;
			lastTime=now;

			//loop through objects
			size = objects.size();
			for (int i=0; i<size; ++i) {
				GameObject o=objects.get(i);
				o.execute(delta);
				physics.physicsTick(o);
			}
			//small wait, improves stability
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
