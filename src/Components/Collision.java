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
			if(xSize<ySize) {
				xSize=ySize;
			}else {
				ySize=xSize;
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
		if(Math.abs(oX-x)>oXs*2)return;
		switch(other.getShape()) {
			case CIRCLE:
				//circle to square
				if(shape==CollisionShape.SQUARE) {
					  float testX = oX;
					  float testY = oY;
					  
					  // select edge
					  if (x+xSize < oX) {
						  testX = x+xSize;      // right edge
					  }else if (x-xSize > oX) {
						  testX = x-xSize;		// left edge
					  }  		
					  if (y+ySize < oY) {
						  testY = y+ySize;     // down edge
					  }else if (y-ySize > oY) {
						  testY = y-ySize;		// up edge
					  }

					  // get distance from closest edges
					  float distX = oX-testX;
					  float distY = oY-testY;
					  float distance = (float) Math.sqrt((distX*distX) + (distY*distY));
					  //System.out.println(distance);
					  if (distance < oXs) {
						  collision=true;
						  if(!moves)return;
						  transform.setX(((x-oX)/distance)+x);
						  transform.setY(((y-oY)/distance)+y);
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
					float oYs= other.getySize();
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
					  float testX = x;
					  float testY = y;
					  float oYs= other.getySize();

					  // select closest edge
					  if (x < oX-oXs) {
						  testX = oX-oXs;      	// left edge
					  }else if (x > oX+oXs) {
						  testX = oX+oXs;		// right edge
					  }  		
					  if (y < oY-oYs) {
						  testY = oY-oYs;		// top edge
					  }else if (y > oY+oYs) {	
						  testY = oY+oYs;	 	// bottom edge
					  }

					  // get distance from closest edges
					  float distX = x-testX;
					  float distY = y-testY;
					  float distance = (float) Math.sqrt((distX*distX) + (distY*distY));

					  if (distance < xSize) {
						  collision=true;
						  if(!moves)return;
						  if(Math.abs(distX)>Math.abs(distY)) {
							  if(x>testX) {
								  transform.setX(testX+xSize);
							  }else if(x<testX) {
								  transform.setX(testX-xSize);
							  }
						  }else {
							  if(y>testY) {
								  transform.setY(testY+ySize);
							  }else if(y<testY) {
								  transform.setY(testY-ySize);
							  }
						  }
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
