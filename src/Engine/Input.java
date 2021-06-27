package Engine;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Input implements KeyListener, MouseListener, FocusListener{
	private static boolean[] keys;
	static boolean mouseLeftPressed,click;
	
	public Input() {
		keys = new boolean[65536];
		mouseLeftPressed=false;
		click=false;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code > 0 && code < keys.length)
			keys[code] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code > 0 && code < keys.length)
			keys[code] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static boolean GetKey(int key) {
		return keys[key];
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		click=true;	
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mouseLeftPressed=true;
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseLeftPressed=false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Returns the mouse position on the entire screen
	 * @return p mouse position on screen
	 */
	public static Point getMousePosition() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		p.setLocation(p.x, p.y-50);
		return p;
	}
	
	/**
	 * Detect if the mouse button is being pressed
	 * @return true if the mouse button is pressed
	 */
	public static boolean getMousePressed() {
		return mouseLeftPressed;
	}
	
	/**
	 * Returns true once every time the mouse has been pressed and released. The input is consumed when checked, only returns true once per click.
	 * @return click Whether there was a click
	 */
	public static boolean detectMouseClick() {
		if(click) {
			click=false;
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void focusGained(FocusEvent e) {}
	/**
	 * Invalidate inputs if window focus is lost
	 */
	@Override
	public void focusLost(FocusEvent e) {
		for (int i=0; i<65536; ++i) {
			keys[i]=false;
		}
		mouseLeftPressed=false;
		click=false;
	}

}
