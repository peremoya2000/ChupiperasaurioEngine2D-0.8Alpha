package Engine;

import java.io.Serializable;

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
		public Transform add(float x, float y) {
			this.x+=x;
			this.y+=y;
			return this;
		}				
		public Transform add(Transform t) {
			this.x+=t.getX();
			this.y+=t.getY();
			return this;
		}
		public Transform subtract(Transform t) {
			this.x-=t.getX();
			this.y-=t.getY();
			return this;
		}
		public Transform scale(float scale) {
			this.x*=scale;
			this.y*=scale;
			return this;
		}		
		public float getDistance(Transform other) {
			float xdif=other.getX()-x;
			float ydif=other.getY()-y;
			return (float) (Math.sqrt(xdif*xdif+ydif*ydif));			
		}		
		public float getLength() {
			return (float) (Math.sqrt(x*x+y*y));
		}		
		public Transform normalize() {
			float l=this.getLength();
			if (l!=1) {
				this.x/=l;
				this.y/=l;
			}
			return this;
		}
		
		
}
