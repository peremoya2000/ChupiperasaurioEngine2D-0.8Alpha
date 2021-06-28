package Game;

import java.awt.event.KeyEvent;
import Components.Collision;
import Components.CollisionShape;
import Components.Sprite;
import Engine.Camera;
import Engine.GameObject;
import Engine.Input;
import Engine.Transform;
/**
 * Demo player class
 * @author Pere
 */
@SuppressWarnings("serial")
public class Player extends GameObject {
	float xSpd,ySpd;
	private Camera camera;
	public Player(Camera camera) {
		super(new Transform(0,0,0,0,50,50));
		addComponent(new Sprite("src/GameAssets/circle.png",true, this));
		addComponent(new Collision(CollisionShape.CIRCLE, true, this));
		this.camera=camera;
	}
	@Override
	public void execute(float delta){
		xSpd=Input.GetKey(KeyEvent.VK_D) 	? 100 : 0;
		xSpd-=Input.GetKey(KeyEvent.VK_A) 	? 100 : 0;
		ySpd=Input.GetKey(KeyEvent.VK_S)	? 100 : 0;
		ySpd-=Input.GetKey(KeyEvent.VK_W)	? 100 : 0;
		transform.setX(transform.getX()+xSpd*delta);
		transform.setY(transform.getY()+ySpd*delta);
		//System.out.println(transform.getY());
		camera.setCameraX(transform.getX());
		camera.setCameraY(transform.getY());
	}
}
