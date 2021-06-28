package Components;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import Engine.Camera;
import Engine.GameObject;
import Engine.Transform;

/**
 * Version of the sprite component that takes a GIF file and displays it. Certain GIF compression formats can produce visual glitches.
 * @author Pere
 */
@SuppressWarnings("serial")
public class AnimatedSprite extends Component {
	//These two arrays are not saved but generated from the file
	private transient BufferedImage[] images,ogImages;
	private File file;
	//xSize:half width, ySize:half height
	short xSize,ySize,ogWidth,ogHeight,index,animLength,currFinalRot;
	float delay,delta;
	long lastTime;
	
	public AnimatedSprite(GameObject owner) {super(owner);}
	
	/**
	 * Constructor
	 * @param filePath path to image file with name and extension
	 * @param matchCollision Whether to show the sprite with the same size as the collision box(if true) or use the images original size(if false)
	 * @param fps the speed at which the animation has to run at
	 * @param owner (usually, "this")
	 */
	public AnimatedSprite(String filePath, boolean matchCollision, float fps, GameObject owner) {
		super(owner);		
		index=0;
		delta=0;
		delay=1/fps;
	    try {
	    	file = new File(filePath);
	        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
	        ImageInputStream stream = ImageIO.createImageInputStream(file);
	        reader.setInput(stream);
	        animLength = (short) reader.getNumImages(true);
	        images = new BufferedImage[animLength];
	        ogImages = new BufferedImage[animLength];
	        for (short i = 0; i < animLength; ++i) {
	        	ogImages[i] = reader.read(i);	   
	            images[i] = reader.read(i);	            
	        }	    	    	

			Transform t = owner.getTransform();
			if(matchCollision) {
				xSize=(short) t.getxSize();
				ySize=(short) t.getySize();				
			}else {
				this.xSize=(short) (images[index].getWidth()/2);
				this.ySize=(short) (images[index].getHeight()/2);
			}
			ogWidth=(short) (xSize*2); ogHeight=(short) (ySize*2);
			setRotation(t.getRotation());
		} catch (IOException | IllegalStateException e) {
			System.out.println("Could not load: "+filePath);
			e.printStackTrace();
		}	    
	}
	
	public BufferedImage getImage() {
		return images[index];
	}
	public void setImage(BufferedImage image, int frame) {
		this.images[frame] = image;
	}
	
	/**
	 * Reload 
	 */
	public void reloadImage() {
		try {
			xSize=(short) (ogWidth/2);ySize=(short) (ogHeight/2);
		    ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		    ImageInputStream stream = ImageIO.createImageInputStream(file);
		    reader.setInput(stream);
		    animLength = (short) reader.getNumImages(true);
		    images = new BufferedImage[animLength];
		    ogImages = new BufferedImage[animLength];
		    for (short i = 0; i < animLength; ++i) {
		    	ogImages[i] = reader.read(i);	   
		    	images[i] = reader.read(i);	            
		    }
			setRotation(owner.getTransform().getRotation());
		} catch (IOException | IllegalStateException e) {
			System.out.println("Could not load: "+file);
			e.printStackTrace();
		}		
	}
	
	/**
	 * Rotate the Sprite on itself
	 * @param rotation in degrees
	 */
	public void setRotation(short rotation) {
		if (currFinalRot==rotation) return;
		double rads = Math.toRadians(rotation);
		double sin = Math.abs(Math.sin(rads));
		double cos = Math.abs(Math.cos(rads));
		short width =  (short) (Math.abs(ogWidth  * cos) + Math.abs(ogHeight * sin));
		short height = (short) (Math.abs(ogHeight * cos) + Math.abs(ogWidth  * sin));
		xSize=(short) (width/2);
		ySize=(short) (height/2);
		BufferedImage rotatedImage;
		AffineTransform at;
		AffineTransformOp rotateOp;
		for (short i=0; i<animLength; ++i) {
			rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			at = new AffineTransform();
			at.translate(width / 2, height / 2);
			at.rotate(rads, 0, 0);
			at.translate(-ogWidth/2, -ogHeight/2);
			rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			rotateOp.filter(ogImages[i],rotatedImage);
			images[i]=rotatedImage;
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
		if (images!=null) {	
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
			
			//Return if the animation sprite is out of the screen in world space coordinates
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
			
			//Calculate deltas
			long now=System.currentTimeMillis();
			delta+=(now-lastTime)/1000.f;
			lastTime=now;
			
			//Draw
			g.drawImage(images[index], (int)(x-xSize*xScale), (int)(y-ySize*yScale), (int)(xSize*2*xScale), (int)(ySize*2*yScale), null);
			
			//Advance animation
			if(delta>delay) {
				delta=0;
				if (++index>=animLength)index=0;
			}						
		}
	}
	
}
