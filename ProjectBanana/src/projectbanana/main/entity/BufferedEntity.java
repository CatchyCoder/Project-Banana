package projectbanana.main.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import projectbanana.main.Engine;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;
import projectbanana.main.values.Rotation;

public abstract class BufferedEntity extends Entity {
	
	// The image that every Entity uses to draw itself in the game
	protected BufferedImage image;
	
	// These are used for rotating the image (transformations)
	private AffineTransform affineTransform;
	private AffineTransformOp affineTransformOp;
	
	// Used for caching images in order to save processing time later
	private ArrayList<String> cachedPaths = new ArrayList<String>();
	private ArrayList<BufferedImage> cachedImages = new ArrayList<BufferedImage>();
	
	private final boolean canRotate;
	
	public BufferedEntity(int x, int y, String imagePath, Geometry geometry, EntityType type, boolean canRotate) {
		super(x, y, geometry, type);
		
		// Determining whether the image can rotate, if false calculation time will be saved
		this.canRotate = canRotate;
		
		// Loading the image, then retrieving the width and height
		image = loadImage(imagePath);
		this.width = boundingWidth = image.getWidth();
		this.height = boundingHeight = image.getHeight();
		
		// Updating initial rotation of the entity
		if(canRotate) rotateImage();
		
		// Setting the default bounding value for circle entities
		boundingRad = width / 2;
		
		// Setting the x and y coordinates for the center of the entity
		this.x -= (width / 2);
		this.y -= (height / 2);
		this.startX = x;
		this.startY = y;
		
	}
	
	protected void rotateImage() {
		affineTransform = AffineTransform.getRotateInstance(getRotation(), (int)(width / 2), (int)(height / 2));
		affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
	}
	
	/**
	 * Loads the specified image and then caches the image so that if it is called to be loaded
	 * again later it will not be reloaded.
	 * @param imagePath
	 */
	protected BufferedImage loadImage(String imagePath) {
		// If this image has already been cached, don't load it again
		for(int i = 0; i < cachedPaths.size(); i++) {
			if(imagePath.equals(cachedPaths.get(i))) {
				return cachedImages.get(i);
			}
		}
		
		// Otherwise, load & store the image for future use
		try {
			// Loading image
			Image preImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
			BufferedImage image = new BufferedImage((int)(preImage.getWidth(null)), (int)(preImage.getHeight(null)), BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(preImage, 0, 0, null);
			
			// Only resize the image if it needs to be
			if(canRotate) image = resizeImage(image);
			
			// Caching image
			cachedPaths.add(imagePath);
			cachedImages.add(image);
			return image;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.format("\nError loading the image \"%s\"", imagePath);
		}
		return null;
	}
	
	/**
	 * Resizes the image, so that when the image is rotated it is not cut off.
	 */
	private BufferedImage resizeImage(BufferedImage image) {
		// Rounding the new size since it cannot be a double
		int newSize = (int)(Math.hypot(image.getWidth(), image.getHeight()) + 0.5);
		BufferedImage newImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		
		// Drawing the old image in the middle of the new image
		newImage.getGraphics().drawImage(image, (int)(newSize / 2 - image.getWidth() / 2), (int)(newSize / 2 - image.getHeight() / 2), null);
		return newImage;
	}
	
	/**
	 * Renders the Entity's volatile image.
	 * 
	 * @param g the graphics object
	 */
	public void renderEntityImage(Graphics g) {
		g = Engine.image.getGraphics();
		if(canRotate) g.drawImage(affineTransformOp.filter(image, null), (int) x, (int) y, null);
		else g.drawImage(image, (int) x, (int) y, null);
	}
	
	/**
	 * Moves the Entity to the point where it was originally spawned at.
	 * If the Entity can rotate, then the Entity's rotation is reset.
	 */
	@Override
	public void respawn() {
		super.respawn();
		if(canRotate) rotateImage();
	}
	
	@Override
	public void lookAt(Entity entity) {
		super.lookAt(entity);
		if(canRotate) rotateImage();
	}
	
	@Override
	protected void turn(Rotation rot, double maxVel, double force) throws Exception {
		super.turn(rot, maxVel, force);
		if(canRotate) rotateImage();
	}
	
	@Override
	protected void applyRotVelDamping(double dampAmnt) {
		super.applyRotVelDamping(dampAmnt);
		if(canRotate) rotateImage();
	}
	
	@Override
	protected void applyRotVelDamping() {
		super.applyRotVelDamping();
		if(canRotate) rotateImage();
	}
	
	@Override
	public void setRotation(double value) {
		super.setRotation(value);
		if(canRotate) rotateImage();
	}
}
