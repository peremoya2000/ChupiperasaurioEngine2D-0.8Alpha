package Engine;

/**
 * You can extend this class to create jobs that execute on a new thread.
 * Only the run needs to be overridden the constructor starts the thread.
 * @author Pere
 */
public class Job implements Runnable{
	private Thread myThread;
	public Job() {
		myThread= new Thread(this);
		myThread.start();
	}

	@Override
	public void run() {

	}

}
