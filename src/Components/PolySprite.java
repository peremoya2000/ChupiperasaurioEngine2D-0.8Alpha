package Components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


import Engine.Camera;
import Engine.GameObject;
import Engine.Transform;
/**
 * Gives the object a visible sprite that the drawing thread takes care of
 * @author Pere
 */
@SuppressWarnings("serial")
public class PolySprite extends Component {
	private File file;
	private float[] ogXCoords, ogYCoords, xCoords, yCoords;
	//xSize:half width, ySize:half height
	private short xSize,ySize,ogWidth,ogHeight, currFinalRot,vertCount;
	private Color col;
	
	public PolySprite(GameObject owner) {super(owner);}
	
	/**
	 * Constructor
	 * @param filePath path to vector image file with name and extension
	 * @param matchCollision Whether to show the sprite with the same size as the collision box (if true) or use the images original size(if false)
	 * @param owner (usually, "this")
	 */
	public PolySprite(String filePath, boolean matchCollision, Color c, GameObject owner) {
		super(owner);
	    try {
	    	file = new File(filePath);
	    	BufferedReader reader= new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			this.setCol(c);
			while (line != null) {
				if (line.contains("<polygon")) {
					//Read vertex data
					line=line.split("points=\"")[1];
					String[] points = line.split(" ");
					vertCount = (short) (points.length-1);
					ogXCoords = new float[vertCount];
					ogYCoords = new float[vertCount];
					for (byte i=0; i<vertCount; ++i) {
						String[] xy = points[i].split(",");
						ogXCoords[i]=Float.parseFloat(xy[0]);
						ogYCoords[i]=Float.parseFloat(xy[1]);
					}
					line=null;
				}else {
					// read next line
					line = reader.readLine();
				}
			}
			reader.close();
			xCoords = new float[vertCount];
			yCoords = new float[vertCount];
			Transform t = owner.getTransform();
			if(matchCollision) {
				xSize=(short) t.getxSize();
				ySize=(short) t.getySize();				
			}else {
				xSize=(short) (getValueAvg(ogXCoords));
				ySize=(short) (getValueAvg(ogYCoords));
			}
			ogWidth=(short) (xSize*2); ogHeight=(short) (ySize*2);
			for (byte i=0; i<vertCount; ++i) {
				ogXCoords[i]=ogXCoords[i]-xSize;
				ogYCoords[i]=ogYCoords[i]-ySize;
				xCoords[i]=ogXCoords[i];
				yCoords[i]=ogYCoords[i];
			}
			setRotation(t.getRotation());
		} catch (IOException | IllegalStateException e) {
			System.out.println("Could not load: "+filePath);
			e.printStackTrace();
		}	    
	}
	
	public Color getCol() {
		return col;
	}

	public void setCol(Color col) {
		this.col = col;
	}
	
	/**
	 * Calculate the average value of an array of coordinates. Internal.
	 * @param arr input float array
	 * @return average value
	 */
	private float getValueAvg(float[] arr) {
		float sum=0;
		for (byte i=0; i<vertCount; ++i) {
			sum+=arr[i];
		}
		float avg=sum/vertCount;
		return avg;
	}

	/**
	 * Reload 
	 */
	public void reloadImage() {
		xSize=(short) (ogWidth/2);ySize=(short) (ogHeight/2);
	}
	
	/**
	 * Rotate the point relative to the poly center
	 * @param rotation in degrees
	 */
	public void setRotation(short rotation) {
		if (currFinalRot==rotation)return;
		double rads = Math.toRadians(rotation);
		double sin = Math.sin(rads);
		double cos = Math.cos(rads);
		float rx,x,y;
		for (byte i=0; i<vertCount; ++i) {
			x=ogXCoords[i];
			y=ogYCoords[i];
			rx = (float) (x*cos-y*sin);
			y =  (float) (x*sin+y*cos);
			xCoords[i]=rx;
			yCoords[i]=y;
		}
		currFinalRot=rotation;
	}
	
	/**
	 * Draws the sprite to the render buffer performing the needed transformations. Called by the render thread.
	 * @param g graphics
	 * @param camera object
	 * @param sWidth Screen width
	 * @param sHeight Screen Height
	 */
	public void draw(Graphics2D g, Camera camera, short sWidth, short sHeight) {
		if (true) {
			//Get data
			Transform t= owner.getTransform();
			float x=t.getX();
			float y=t.getY();
			float camX=camera.getCameraX();
			float camY=camera.getCameraY();
			float camxSize=camera.getCameraXSize();
			float camySize=camera.getCameraYSize();
			short camRot=camera.getCameraRot();
			float left=camX-camxSize;
			float up=camY-camySize;
			Color gCol = g.getColor();
			
			//Scale to viewport
			float xScale=sWidth/(camxSize*2);
			x=(x-left)*xScale;
				
			float yScale=sHeight/(camySize*2);
			y=(y-up)*yScale;
			
			if (camRot!=0) {
				//Rotate coordinates around screen center
				double sin, cos;
				sin= Math.sin(Math.toRadians(camRot));
				cos= Math.cos(Math.toRadians(camRot));
				int halfSW=sWidth/2;
				int halfSH=sHeight/2;
				float rx;
				rx = (float) (halfSW + (x-halfSW)*cos + (y-sHeight/2)*sin);
				y =  (float) (halfSH - (x-halfSW)*sin + (y-halfSH)*cos);
				x=rx;
				setRotation((short) (t.getRotation()-camRot));
			}
			
			int[] rXCoords= new int[vertCount];
			int[] rYCoords= new int[vertCount];
			
			for (byte i=0; i<vertCount; ++i) {
				rXCoords[i]=(int) (x+xCoords[i]);
				rYCoords[i]=(int) (y+yCoords[i]);
			}
			g.setColor(this.col);
			g.fillPolygon(rXCoords, rYCoords, vertCount);
			g.setColor(gCol);						
		}
	}	
}
