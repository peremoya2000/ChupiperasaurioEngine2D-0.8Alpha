package Engine;

/**
 * Handles Window resizing asynchronously
 * @author Usuario
 */
public class ResizerJob extends Job {
	boolean resizeNeeded;
	Viewer v;
	public ResizerJob(Viewer v){
		super();
		this.v=v;
		resizeNeeded=false;		
	}
	
	@Override
	public void run() {
		//Check every 1.8s if a resize is needed (async) 
		while (true) {
			try {
				Thread.sleep(1800);
				if(resizeNeeded) {
					v.resize();
					resizeNeeded=false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Called by the engine when a window  resize is needed
	 */
	public void managePetition() {
		resizeNeeded=true;
	}
	/**
	 * Returns if the engine is expecting a window resize
	 * @return true if resize is necessary
	 */
	public boolean getResizeNeeded() {
		return resizeNeeded;
	}
	
}
