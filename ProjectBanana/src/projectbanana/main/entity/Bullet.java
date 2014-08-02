package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.Engine;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;

public class Bullet extends BufferedEntity {
	
	private double thrust = 0.65;
	private int delta = 0;
	private int range = 350;
		
	public Bullet(int x, int y, double velX, double velY, double rotation, EntityType type) {
		super(x, y, "/bullet/bullet.png", Geometry.RECTANGLE, type, false);
		
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
		
		delta++;
		
		if(delta >= range)
			isDone = true;
		
		this.applyForces();
	}

	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}

	@Override
	public void handleCollision(Entity entity) {
		if(!entity.getType().equals(this.getType())) {
			isDone = true;
		}
	}
}
