package Components;

import java.io.Serializable;

import Engine.GameObject;
/**
 * Parent component class, overridden to create new components
 */
@SuppressWarnings("serial")
public class Component implements Serializable{
	protected GameObject owner;
	public Component(GameObject owner) {
		this.owner=owner;
	}
	
	public GameObject getOwner() {
		return owner;
	}

	public void setOwner(GameObject owner) {
		this.owner = owner;
	}

	protected void execute() {}
}
