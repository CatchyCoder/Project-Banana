package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.values.Geometry;

public class Bullet extends BufferedEntity {
	
	private double thrust = 0.65; // 0.65
	
	public Bullet(int x, int y, double velX, double velY, double rotation) {
		super(x, y, "/bullet/bullet.jpg", Geometry.RECTANGLE, false);
		
		// Adding an initial speed to the bullet, that way bullets
		// move faster when shot from a moving object
		this.velX =  velX;
		this.velY =  velY;
		this.setRotation(rotation);
	}

	@Override
	public void tick() {
		if(thrust > 0.01) {
			this.accForward(thrust);
			// Slowly decreasing thrust over time
			thrust *= 0.925;
		}
		
		this.applyForces();
	}

	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}

	@Override
	public void handleCollision(Entity entity) {
		// TODO Auto-generated method stub
		
	}
}
