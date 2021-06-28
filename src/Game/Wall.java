package Game;

import Components.Collision;
import Components.CollisionShape;
import Components.Sprite;
import Engine.GameObject;
import Engine.Transform;
//Demo class, can be deleted
@SuppressWarnings("serial")
public class Wall extends GameObject {
	public Wall(int x, int y) {
		super(new Transform(x,y,0,0,200,100));
		addComponent(new Sprite("src/GameAssets/square.jpg",true, this));
		addComponent(new Collision(CollisionShape.SQUARE, false, this));
	}
	
	@Override
	public void execute(float delta){

	}

}
