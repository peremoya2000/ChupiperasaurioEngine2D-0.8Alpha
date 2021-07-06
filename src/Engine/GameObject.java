package Engine;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.HashMap;

import Components.AnimatedSprite;
import Components.Component;
import Components.Sprite;

/**
 * Class to override to create any game object class. Which means any object on the game.
 * @author Usuario
 */
@SuppressWarnings("serial")
public class GameObject implements Serializable{
	//Transform of the object.
	protected Transform transform;
	//HashMap of the components added to this object
	protected HashMap<String, Component> components;
	
	/**
	 * Empty constructor. Uses default transform
	 */
	protected GameObject() {
		this.components= new HashMap<String, Component>();
		this.transform=new Transform(this);
	}
	
	/**
	 * GameObject constructor
	 * @param t transform
	 */
	protected GameObject(Transform t){
		this.components= new HashMap<String, Component>();
		this.transform=t;
		t.setOwner(this);		
	}
	
	/**
	 * Called by GameObjectHandler once per tick. Has to be overridden.
	 * @param delta Time between ticks, used to multiply by speed values to make them consistent.
	 */
	protected void execute(float delta) {
		execute();
	}
	
	/**
	 * Called by GameObjectHandler once per tick. Has to be overridden.
	 * Takes no parameters, better to use the one with delta parameter
	 */
	protected void execute() {}
	
	/**
	 * Draw the object, called by the Viewer
	 * @param g Graphics
	 * @param camera Camera
	 * @param sWidth Screen Width
	 * @param sHeight Screen Height
	 */
	public void draw(Graphics2D g, Camera camera, short sWidth, short sHeight) {
		Sprite sprite= (Sprite) components.get("Sprite");
		if(sprite!=null) {
			sprite.draw(g, camera, sWidth, sHeight);
		}
		AnimatedSprite animsprite= (AnimatedSprite) components.get("AnimatedSprite");
		if(animsprite!=null) {
			animsprite.draw(g, camera, sWidth, sHeight);
		}
	}
	
	/**
	 * Add component to the object
	 * @param c Component to add
	 * @return Returns the added component
	 */
	public Component addComponent(Component c) {
		if(components.putIfAbsent(c.getClass().getSimpleName(),c)!=null) {
			System.out.println("Component already present");
			return null;
		}
		return c;
	}
	
	/**
	 * Get the component determined by the parameter.
	 * @param type The type of component you want. Only one of each per GameObject
	 * @return
	 */
	public Component getComponent(String type) {
		return components.get(type);	
	}
	
	/**
	 * Get the transform
	 * @return The object's Transform
	 */
	public Transform getTransform() {
		return transform;
	}
	
	/**
	 * Rotates the sprite the specified amount of degrees
	 * @param rot is the rotation amount in degrees
	 */
	public void setSpriteRotation(short rot) {
		Sprite sprite= (Sprite) components.get("Sprite");
		if(sprite!=null) {
			sprite.setRotation(rot);
		}
	}
	
}
