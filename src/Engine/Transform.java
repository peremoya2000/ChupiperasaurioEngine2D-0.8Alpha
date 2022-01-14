package Engine;

import java.io.Serializable;
/**
 * Encapsulates the coordinates, rotation and size of an object. It can also be used to  perform 2d vector calculations.
 * @author Pere
 */
@SuppressWarnings("serial")
public class Transform implements Serializable{
		private float x,y;
		private short rotation, depth;
		private GameObject owner;
		
		//These are essentially half the width and height
		private float xSize, ySize;
		
		public Transform() {
			this.x=0;
			this.y=0;
			this.rotation=0;
			this.xSize=1;
			this.ySize=1;
		}
		
		public Transform(GameObject o) {
			this.x=0;
			this.y=0;
			this.rotation=0;
			this.xSize=1;
			this.ySize=1;
			this.depth=0;
			this.owner=o;
		}
		
		public Transform(float x, float y) {
			this.x=x;
			this.y=y;
			this.rotation=0;
			this.xSize=1;
			this.ySize=1;
		}
		public Transform(float x, float y, int depthIndex, int rot, float width, float height) {
			this.x=x;
			this.y=y;
			this.depth=(short) depthIndex;
			this.rotation=(short) (rot%360);
			this.xSize=width/2;
			this.ySize=height/2;
		}
		
		public float getX() {
			return x;
		}
		public void setX(float f) {
			this.x = f;
		}
		public float getY() {
			return y;
		}
		public void setY(float y) {
			this.y = y;
		}
		
		/**
		 * Gets the rotation in degrees
		 * @return rotation in degrees
		 */
		public short getRotation() {
			return (short) (rotation%360);
		}
		/**
		 * Set rotation.
		 * @param rotation in degrees
		 */
		public void setRotation(int rotation) {
			this.rotation = (short) (rotation%360);
			if(owner != null)
				owner.setSpriteRotation(this.rotation);
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
		public void setOwner(GameObject o) {
			this.owner=o;
		}
		public short getDepth() {
			return depth;
		}
		public void setDepth(int depth) {
			this.depth = (short) depth;
		}
		
		//Vectorial operations
		/**
		 * Adds the two values to its x and y values
		 * @param x value to add
		 * @param y value to add
		 * @return itself
		 */
		public Transform add(float x, float y) {
			this.x+=x;
			this.y+=y;
			return this;
		}
		/**
		 * Adds the input transform to itself
		 * @param t transform that is added
		 * @return itself
		 */
		public Transform add(Transform t) {
			this.x+=t.getX();
			this.y+=t.getY();
			return this;
		}
		/**
		 * Subtracts the input transform from itself
		 * @param t transform to subtract
		 * @return itself
		 */
		public Transform subtract(Transform t) {
			this.x-=t.getX();
			this.y-=t.getY();
			return this;
		}
		/**
		 * Scales the vector according to the input
		 * @param scale value to scale the transforms position vector to
		 * @return itself
		 */
		public Transform scale(float scale) {
			this.x*=scale;
			this.y*=scale;
			return this;
		}
		/**
		 * Calculates the distance between two points
		 * @param other transform to calculate the distance to
		 * @return Distance between itself and the input transform
		 */
		public float getDistance(Transform other) {
			float xdif=other.getX()-x;
			float ydif=other.getY()-y;
			return (float) (Math.sqrt(xdif*xdif+ydif*ydif));			
		}
		
		/**
		 * Calculates the Manhattan distance between two points. Less accurate but faster.
		 * @param other transform to calculate the distance to
		 * @return Distance between itself and the input transform
		 */
		public float getFastDistance(Transform other) {
			float xdif=other.getX()-x;
			float ydif=other.getY()-y;
			return (float) (Math.abs(xdif)+Math.abs(ydif));			
		}
		
		/**
		 * Calculates the length of the transforms 2d vector
		 * @return Length of the 2d vector
		 */
		public float getLength() {
			return (float) (Math.sqrt(x*x+y*y));
		}
		/**
		 * Normalizes the 2d vector to have length 1
		 * @return itself
		 */
		public Transform normalize() {
			float l=this.getLength();
			if (l!=1) {
				this.x/=l;
				this.y/=l;
			}
			return this;
		}
		
		
}
