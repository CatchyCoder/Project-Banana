package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.values.Geometry;

public class Bullet extends BufferedEntity {
	
	private final double speed = 15.0;
	
	public Bullet(int x, int y, double velX, double velY) {
		super(x, y, "/bullet/bullet.jpg", Geometry.RECTANGLE, false);
		
		// Adding an initial speed to the bullet, that way bullets
		// move faster when shot from a moving object
		this.velX =  velX * 2;
		this.velY =  velY * 2;
	}

	@Override
	public void tick() {
		this.applyForces();
	}

	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}
}
