package Components;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Engine.GameObject;
import Engine.Transform;
/**
 * Contains and manages multiple sounds you can add or delete
 * @author Usuario
 */
@SuppressWarnings("serial")
public class Sound extends Component {
	//SOUNDS MUST BE .WAV
	//HasMap on sounds added one by one
	private HashMap<String, Clip> sounds;
	private boolean attenuation;
	private float maxSoundDist;
	private Transform camPos;
	
	public Sound(GameObject owner) {
		super(owner);
		attenuation=false;
		maxSoundDist=1;
		sounds= new HashMap<String, Clip>();		
	}
	
	public Sound(String path, GameObject owner) {
		super(owner);
		attenuation=false;
		maxSoundDist=1;
		sounds= new HashMap<String, Clip>();
		try {
			File f = new File(path);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			audioInputStream.close();
			sounds.put(f.getName(), clip);
		} catch (UnsupportedAudioFileException | IOException  | LineUnavailableException e) {
			System.out.println(e.getMessage());
			System.out.println("Could not initialize sound");
		}
	}
	
	/**
	 * Add a sound to be played from a file
	 * @param path of the file with name and extension. The path to the GameAssets folder is src/GameAssets/.
	 */
	public void addSound(String path) {
		try {
			File f = new File(path);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(f);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			audioInputStream.close();
			sounds.putIfAbsent(f.getName(), clip);
		} catch (UnsupportedAudioFileException | IOException  | LineUnavailableException e) {
			System.out.println(e.getMessage());
			System.out.println("Could not initialize sound");
		}
	}
	
	/**
	 * Play a sound.
	 * @param name File name for the sound with extension.
	 * @param fromStart Whether to play from start or resume playing.
	 * @param loops Amount of times the sound is going to loop.
	 */
	public void playSound(String name, boolean fromStart, int loops) {
		Clip c = sounds.get(name);
		if(c==null) {
			System.out.println("Unable to play sound");
			return;
		}
		if (fromStart && !c.isActive()) {
			c.setFramePosition(0);
			c.loop(loops);
		}
		if(attenuation) {
			float vol = (this.getOwner().getTransform().getFastDistance(camPos));
			vol= 1-(Math.min(maxSoundDist, vol)/maxSoundDist);
			System.out.println(vol);
			setVolume(name, vol);
		}
		c.start();
	}
	
	/**
	 * Stop a sound.
	 * @param name File name for the sound with extension
	 */
	public void stopSound(String name) {
		Clip c = sounds.get(name);
		if(c!=null) {
			c.stop();
		}else {
			System.out.println("Sound not found");
		}
	}
	
	/**
	 * Stop a sound and delete it from memory.
	 * @param name File name for the sound with extension
	 */
	public void killSound(String name) {
		try {
		sounds.get(name).close();
		sounds.remove(name);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Set the gain of the particular sound clip to adapt to the input volume
	 * @param name of the sound clip to change the loudness of
	 * @param level of volume from 0 to 1
	 */
	public void setVolume(String name, float level) {
		try {
			Clip c = sounds.get(name);
			c.open();
		    FloatControl control = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
		    if (control != null) {
		    	float val = lerp(control.getMinimum(),1,level);
		        control.setValue(val);
		    }
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Activate or deactivate attenuation by distance to camera. Applied to all sound clips played by the component.
	 * @param active If attenuation must be active or not
	 * @param t Camera transform
	 * @param maxDist Maximum attenuation radius (square shape)
	 */
	public void setAttenuation(boolean active, Transform t, float maxDist) {
		this.attenuation=active;
		if (active) {
			this.camPos=t;
			this.maxSoundDist=maxDist;
		}
	}
	
	/**
	 * Linearly interpolate between two values. Only for internal class use.
	 * @param a first value
	 * @param b second value
	 * @param f interpolation amount
	 * @return Interpolated result
	 */
	private static float lerp(float a, float b, float f){
	    return a + f * (b - a);
	}
	
	
	
}
