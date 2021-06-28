package Components;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Engine.GameObject;
/**
 * Contains and manages multiple sounds you can add or delete
 * @author Usuario
 */
@SuppressWarnings("serial")
public class Sound extends Component {
	//SOUNDS MUST BE .WAV
	//HasMap on sounds added one by one
	private HashMap<String, Clip> sounds;
	public Sound(GameObject owner) {
		super(owner);
		sounds= new HashMap<String, Clip>();		
	}
	
	public Sound(String path, GameObject owner) {
		super(owner);
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
	 * @param loops Whether the sound sound will loop.
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
	
	
	
}
