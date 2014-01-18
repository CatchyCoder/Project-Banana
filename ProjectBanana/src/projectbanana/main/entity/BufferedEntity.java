package projectbanana.main.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import projectbanana.main.values.GeometryId;

public abstract class BufferedEntity extends Entity {
	
	protected BufferedImage image;
	
	private AffineTransform at;
	protected AffineTransformOp op;
	
	private Image preImage;
	
	private ArrayList<String> cachedPaths = new ArrayList<String>();
	private ArrayList<BufferedImage> cachedImages = new ArrayList<BufferedImage>();
	
	public BufferedEntity(int x, int y, String imagePath, int ID) {
		super(x, y, ID);
		loadImage(imagePath);
		
		this.width = boundingWidth = image.getWidth();
		this.height = boundingHeight = image.getHeight();
		if(getGeometryId() == GeometryId.CIRCLE.getId()) boundingRad = image.getWidth() / 2;
		this.x -= (width / 2);
		this.y -= (height / 2);
		this.startX = (int) this.x;
		this.startY = (int) this.y;
	}
	
	protected void rotateImage() {
		at = AffineTransform.getRotateInstance(rotation, (int)(width / 2), (int)(height / 2));
		op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
	}
	
	/**
	 * Loads the specified image and then caches the image so that if it is called to be loaded
	 * again later, it will not be reloaded.
	 * @param imagePath
	 */
	protected void loadImage(String imagePath) {		
		for(int i = 0; i < cachedPaths.size(); i++) {
			// If this image has already been cached, don't load it again
			if(imagePath.equals(cachedPaths.get(i))) {
				this.image = cachedImages.get(i);
				return;
			}
		}
		
		// Otherwise, load & store the image for future use
		try {
			// Loading image
			preImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
			image = new BufferedImage((int)(preImage.getWidth(null)), (int)(preImage.getHeight(null)), BufferedImage.TYPE_INT_ARGB);
			image.getGraphics().drawImage(preImage, 0, 0, null);
			resizeImage();
			
			// Caching image
			cachedPaths.add(imagePath);
			cachedImages.add(image);
		}
		catch(Exception e) {
			System.out.format("\nError loading the image \"%s\"", imagePath);
			e.printStackTrace();
			
			image = loadBackupImage();
		}
	}
	
	/**
	 * Resizes the image, so that when the image is rotated it is not cut off.
	 */
	protected void resizeImage() {
		BufferedImage oldImage = image;
		// Rounding the new size, since it cannot be a double
		int newSize = (int)(Math.sqrt(Math.pow(image.getWidth(), 2) + Math.pow(image.getHeight(), 2)) + 0.5);
		image = new BufferedImage(newSize, newSize, BufferedImage.TYPE_INT_ARGB);
		image.getGraphics().drawImage(oldImage, (int)(newSize / 2 - oldImage.getWidth() / 2), (int)(newSize / 2 - oldImage.getHeight() / 2), null);
	}
	
	protected BufferedImage loadBackupImage() {
		BufferedImage backupImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
		Graphics g = backupImage.getGraphics();
		g.setColor(Color.PINK);
		g.fillRect(0, 0, backupImage.getWidth(), backupImage.getHeight());
		g.setColor(Color.RED);
		g.fillRect((int) (backupImage.getWidth() * 0.8), 0, (int) (backupImage.getWidth() * 0.2), backupImage.getHeight());
		g.dispose();
		
		return backupImage;
	}
	
	@Override
	protected void respawn() {
		super.respawn();
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
