package Components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Engine.Camera;
import Engine.GameObject;
import Engine.Transform;
/**
 * Gives the object a visible sprite that the drawing thread takes care of
 * @author Pere
 */
@SuppressWarnings("serial")
public class Sprite extends Component {
	//These two values are not saved but generated from the file
	private transient BufferedImage image,ogImage;
	private File file;
	//xSize:half width, ySize:half height
	private short xSize,ySize,ogWidth,ogHeight, currFinalRot;
	
	public Sprite(GameObject owner) {super(owner);}
	
	/**
	 * Constructor
	 * @param filePath path to image file with name and extension
	 * @param matchCollision Whether to show the sprite with the same size as the collision box(if true) or use the images original size(if false)
	 * @param owner (usually, "this")
	 */
	public Sprite(String filePath, boolean matchCollision, GameObject owner) {
		super(owner);
	    try {
	    	file = new File(filePath);
			image = ImageIO.read(file);
			Transform t = owner.getTransform();
			if(matchCollision) {
				xSize=(short) t.getxSize();
				ySize=(short) t.getySize();				
			}else {
				this.xSize=(short) (image.getWidth()/2);
				this.ySize=(short) (image.getHeight()/2);
			}
			ogImage= new BufferedImage(xSize*2, ySize*2, image.getType());
			Graphics2D g2d = ogImage.createGraphics();
	        g2d.drawImage(image, 0, 0, xSize*2, ySize*2, null);
	        g2d.dispose();
			ogWidth=(short) (xSize*2); ogHeight=(short) (ySize*2);
			setRotation(t.getRotation());
		} catch (IOException | IllegalStateException e) {
			System.out.println("Could not load: "+filePath);
			e.printStackTrace();
		}	    
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	/**
	 * Reload 
	 */
	public void reloadImage() {
		try {
			image = ImageIO.read(file);
			xSize=(short) (ogWidth/2);ySize=(short) (ogHeight/2);
			ogImage= new BufferedImage(xSize*2, ySize*2, image.getType());
			Graphics2D g2d = ogImage.createGraphics();
		    g2d.drawImage(image, 0, 0, xSize*2, ySize*2, null);
		    g2d.dispose();
			setRotation(owner.getTransform().getRotation());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Rotate the Sprite on itself
	 * @param rotation in degrees
	 */
	public void setRotation(short rotation) {
		if (currFinalRot==rotation)return;
		double rads = Math.toRadians(rotation);
		double sin = Math.abs(Math.sin(rads));
		double cos = Math.abs(Math.cos(rads));
		short width =  (short) (Math.abs(ogWidth  * cos) + Math.abs(ogHeight * sin));
		short height = (short) (Math.abs(ogHeight * cos) + Math.abs(ogWidth  * sin));
		BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		xSize=(short) (width/2);
		ySize=(short) (height/2);
		at.translate(width / 2, height / 2);
		at.rotate(rads, 0, 0);
		at.translate(-ogWidth/2, -ogHeight/2);
		AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		rotateOp.filter(ogImage,rotatedImage);
		image=rotatedImage;
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
		if (image!=null) {
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
			
			//Return if the sprite is out of the screen in world space coordinates
			if((camRot==0 || camRot==180) && (x+xSize<left || x-xSize>camX+camxSize|| y+ySize< up|| y-ySize>camY+camySize))
				return;
			
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
				if(x+xSize*xScale<0 || x-xSize*xScale>sWidth|| y+ySize*yScale< 0 || y-ySize*yScale>sHeight)
					return;
			}
			g.drawImage(image, (int)(x-xSize*xScale), (int)(y-ySize*yScale), (int)(xSize*2*xScale), (int)(ySize*2*yScale), null);
						
		}
	}
	
}
