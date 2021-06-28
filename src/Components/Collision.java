package Components;

import Engine.GameObject;
import Engine.Transform;

@SuppressWarnings("serial")
public class Collision extends Component {
	//This represents half the width and the height, taken from the transform
	private float xSize,ySize;
	private CollisionShape shape;
	private Transform transform;
	private boolean collision, moves;
	
	public Collision(GameObject owner) {
		super(owner);		
	}
	
	/**
	 * Full constructor
	 * @param shape of the collision (enum)
	 * @param moves determines if the object can move or not
	 * @param owner
	 */
	public Collision(CollisionShape shape, boolean moves, GameObject owner) {
		super(owner);
		this.transform=owner.getTransform();
		this.xSize=transform.getxSize();
		this.ySize=transform.getySize();
		this.shape=shape;
		this.moves=moves;
		if(shape==CollisionShape.CIRCLE) {
			if(xSize>ySize) {
				ySize=xSize;
			}else {
				xSize=ySize;
			}
		}
	}
	
		
	public boolean getCollision() {
		return collision;
	}
	public void setCollision(boolean collision) {
		this.collision = collision;
	}
	public boolean isMoves() {
		return moves;
	}
	public void setMoves(boolean moves) {
		this.moves = moves;
	}
	public float getxSize() {
		return xSize;
	}
	public void setxSize(float xSize) {
		this.xSize = xSize;
	}
	public float getySize() {
		return ySize;
	}
	public void setySize(float ySize) {
		this.ySize = ySize;
	}
	public CollisionShape getShape() {
		return shape;
	}
	public void setShape(CollisionShape shape) {
		this.shape = shape;
	}

	public Transform getTransform() {
		return transform;
	}
	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	
	/**
	 * Handle all the different collision cases
	 * @param other collision component
	 */
	public void handleCollision(Collision other) {
		Transform otherTrans =other.getTransform();
		float oX= otherTrans.getX();
		float oY= otherTrans.getY();
		float x= transform.getX();
		float y= transform.getY();
		float oXs= other.getxSize();
		float oYs= other.getySize();
		if(Math.abs(oX-x)>oXs+xSize+16)return;
		switch(other.getShape()) {
			case CIRCLE:
				//circle to square
				if(shape==CollisionShape.SQUARE) {
					Transform nearestPoint = new Transform();					
					nearestPoint.setX(Math.max(x-xSize, Math.min(oX, x+xSize)));
					nearestPoint.setY(Math.max(y-ySize, Math.min(oY, y+ySize)));
					
					nearestPoint.subtract(otherTrans);
					
					float overlap =oXs-nearestPoint.getLength();
					if (Float.isNaN(overlap))overlap = 0;

					if (overlap>0){
						collision=true;
						if(moves) {
							Transform correction = new Transform(x-oX,y-oY).normalize();
							transform.add(correction);
						}
					}else {
						collision=false;
					}
					
				//circle to circle:
				}else if(shape==CollisionShape.CIRCLE) {
					float dist = transform.getDistance(otherTrans);
					if(dist<xSize+oXs) {
						collision=true;
						if(moves) {
							transform.setX(((x-oX)/dist)+x);
							transform.setY(((y-oY)/dist)+y);
						}
					}else {
						collision=false;
					}
				}
				break;
				
			case SQUARE:
				//square to square
				if(shape==CollisionShape.SQUARE) {
					if(!(x+xSize<oX-oXs || oX+oXs<x-xSize || y+ySize<oY-oYs || oY+oYs<y-ySize)) {
						collision=true;
						if(!moves)return;
						 if(Math.abs(x-oX)>Math.abs(y-oY)) {
							 if(x<oX-oXs) {
								x=oX-oXs-xSize-0.1f;
							 }else if(x>oX-oXs){
								x=oX+oXs+xSize+0.1f;
							 }
						 }else {
							 if(y<oY-oYs) {
								y=oY-oYs-ySize-0.1f;
							 }else if(y>oY+oYs){
								y=oY+oYs+ySize+0.1f;
							 }
						 }
						transform.setX(x);
						transform.setY(y);
					}else {
						collision=false;
					}
				//square to circle
				}else if(shape==CollisionShape.CIRCLE) {
					Transform nearestPoint = new Transform();					
					nearestPoint.setX(Math.max(oX-oXs, Math.min(x, oX+oXs)));
					nearestPoint.setY(Math.max(oY-oYs, Math.min(y, oY+oYs)));
					
					nearestPoint.subtract(transform);
					
					float overlap =xSize-nearestPoint.getLength();
					if (Float.isNaN(overlap))overlap = 0;

					if (overlap>0){
						collision=true;
						if (moves) 
							transform.subtract(nearestPoint.normalize().scale(overlap));
					}else {
						collision=false;
					}
				}
				break;
				
			default:
				return;
		}
	}
	

}
