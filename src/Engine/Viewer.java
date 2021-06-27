package Engine;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
/**
 * Handles the drawing loop and graphics in general
 * @author Pere
 */
public class Viewer extends JPanel implements Runnable{
	private static final long serialVersionUID = 1L;
	private Thread renderThread;
	private boolean render, active;
	private BufferedImage img;
	private Graphics2D ig2d;
	private Graphics vg2d;
	private short width,height;
	private Camera camera;
	private ArrayList<GameObject> objects;
	private ResizerJob resizer;
	
	public Viewer(ArrayList<GameObject> o, Camera camera) {
		this.objects=o;
		width=1280;
		height=720;
		this.camera=camera;
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ig2d=img.createGraphics();
		this.renderThread= new Thread(this);
	}
	
	/**
	 * Last preparations and start render thread
	 */
	public void init() {
		this.active=true;
		this.render=true;
		width=(short) this.getWidth(); height=(short) this.getHeight();
		this.vg2d = this.getGraphics();
		resizer= new ResizerJob(this);
		this.renderThread.start();
	}
	
	/**
	 * Get if the renderer is working or waiting
	 * @return true if the render loop is active
	 */
	public boolean getRender() {
		return render;
	}
	
	/**
	 * Tell the renderer to stop or to render
	 * @param render
	 */
	public void setRender(boolean render) {
		this.render = render;
	}
	
	/**
	 * Called by the JFrame/Engine when a resize event is raised, 
	 * communicates this to the Resizer who manages it to avoid problems with the way the events are raised with AWT.
	 */
	public void resizePetition() {
		resizer.managePetition();
	}
	
	/**
	 * Called by the Resizer
	 */
	public void resize() {
		render=false;
		this.width=(short) this.getWidth();
		this.height=(short) this.getHeight();
		camera.setCameraXSize(width/2);
		camera.setCameraYSize(height/2);
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		ig2d=img.createGraphics();
		this.vg2d = this.getGraphics();
		render=true;
	}
	
	/**
	 * Load a new scene's camera and object list into the Viewer
	 * @param newScene to load
	 */
	public void loadScene(Scene newScene) {
		render=false;
		this.objects=newScene.getObjects();
		this.camera=newScene.getCamera();
		render=true;
		
	}
	
	/**
	 * Core rendering loop
	 */
	@Override
	public void run() {
		while(active) {
			if(render) {
				ig2d.fillRect(0, 0, width, height);
				//Draw Shit
				int size = objects.size();
				for (int i=0; i<size; ++i) {
					if(!render)break;
					objects.get(i).draw(ig2d, camera, width, height);
				}
				vg2d.drawImage(img, 0, 0, null);
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
