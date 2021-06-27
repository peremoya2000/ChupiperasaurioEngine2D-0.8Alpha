package Engine;

import java.io.Serializable;

/**
 * Game camera with a transform that specifies position, rotation and area viewed by the camera.
 * @author Pere
 */
@SuppressWarnings("serial")
public class Camera implements Serializable{
	private Transform cameraTransform;
	private float zoom;
	public Camera() {
		zoom=1.0f;
	}
	
	public Camera(Transform transform) {
		this.cameraTransform=transform;
		this.zoom=1.f;
	}

	public Transform getCameraTransform() {
		return cameraTransform;
	}

	public void setCameraTransform(Transform cameraTransform) {
		this.cameraTransform = cameraTransform;
	}
	
	public void setCameraX(float x) {
		this.cameraTransform.setX(x);
	}
	
	public void setCameraY(float y) {
		this.cameraTransform.setY(y);
	}
	
	public float getCameraX() {
		return this.cameraTransform.getX();
	}	
	public float getCameraY() {
		return this.cameraTransform.getY();
	}
	
	public void setCameraXSize(float xSize) {
		this.cameraTransform.setxSize(xSize);
	}
	public void setCameraYSize(float ySize) {
		this.cameraTransform.setySize(ySize);
	}
	
	public float getCameraXSize() {
		return this.cameraTransform.getxSize()/zoom;
	}	
	public float getCameraYSize() {
		return this.cameraTransform.getySize()/zoom;
	}
	
	public void setCameraRot(int rot) {
		this.cameraTransform.setRotation(rot);
	}
	public short getCameraRot() {
		return this.cameraTransform.getRotation();
	}
	
	public void setZoom(float zoom) {
		this.zoom=zoom;
	}	
	public float getZoom() {
		return this.zoom;
	}

}
