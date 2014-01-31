package projectbanana.main.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import projectbanana.main.Engine;
import projectbanana.main.values.GeometryId;

public abstract class BufferedEntity extends Entity {
	
	// The image that every Entity uses to draw itself in the game
	protected BufferedImage image;
	
	// These are used for rotating the image (transformations)
	private AffineTransform affineTransform;
	private AffineTransformOp affineTransformOp;
	
	// Used for caching images in order to save processing time later
	private ArrayList<String> cachedPaths = new ArrayList<String>();
	private ArrayList<BufferedImage> cachedImages = new ArrayList<BufferedImage>();
	
	private BufferedEntity(int x, int y, int ID) {
		super(x, y, ID);
		
		this.startX = (int) this.x;
		this.startY = (int) this.y;
	}
	
	/*
	 * DELETE CONSTRUCTOR BELOW LATER
	 */
	
	public BufferedEntity(int x, int y, int width, int height, int ID) { 
		this(x, y, ID);
		
		this.x -= (width / 2);
		this.y -= (height / 2);
		this.width = width;
		this.height = height;
		boundingRad = width / 2;
		boundingWidth = width;
		boundingHeight = height;
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		image = resizeImage(image);
		rotateImage();
	}
	
	public BufferedEntity(int x, int y, String imagePath, int ID, boolean canRotate) {
		this(x, y, ID);
		
		image = loadImage(imagePath);
		this.width = boundingWidth = image.getWidth();
		this.height = boundingHeight = image.getHeight();
		boundingRad = width / 2;
		boundingWidth = width;
		boundingHeight = height;
		this.x -= (width / 2);
		this.y -= (height / 2);
		if(getGeometryId() == GeometryId.CIRCLE.getId()) boundingRad = image.getWidth() / 2;
		rotateImage();
	}
	
	protected void rotateImage() {
		System.out.println("Called");
		affineTransform = AffineTransform.getRotateInstance(rotation, (int)(width / 2), (int)(height / 2));
		affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
	}
	
	/**
	 * Loads the specified image and then caches the image so that if it is called to be loaded
	 * again later, it will not be reloaded.
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
			image = resizeImage(image);
			
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
	protected BufferedImage resizeImage(BufferedImage image) {
		System.out.println("Resized\n");
		// Rounding the new size since it cannot be a double
		int newSize = (int)(Math.hypot(image.getWidth(), image.getHeight()) + 0.5);
		System.out.println(newSize);
		BufferedImage newImage = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		
		// Drawing the old image in the middle of the new image
		newImage.getGraphics().drawImage(image, (int)(newSize / 2 - image.getWidth() / 2), (int)(newSize / 2 - image.getHeight() / 2), null);
		return newImage;
	}
	
	public void renderEntityImage(Graphics g) {
		//rotateImage();
		g = Engine.image.getGraphics();
		g.drawImage(affineTransformOp.filter(image, null), (int) x, (int) y, null);
	}
	
	@Override
	protected void respawn() {
		super.respawn();
		rotateImage();
	}
	
	@Override
	public void lookAt(Entity entity) {
		super.lookAt(entity);
		rotateImage();
	}
	
	@Override
	protected void turn(int dir, double maxVel, double force) throws Exception {
		super.turn(dir, maxVel, force);
		rotateImage();
	}
	
	@Override
	protected void applyRotVelDamping(double dampAmnt) {
		super.applyRotVelDamping(dampAmnt);
		rotateImage();
	}
	
	@Override
	protected void applyRotVelDamping() {
		super.applyRotVelDamping();
		rotateImage();
	}
}
