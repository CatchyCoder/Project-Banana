package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.World;
import projectbanana.main.values.Geometry;

public class TestAI extends BufferedEntity {
	
	private double thrust = 0.15, speed = 5.0;
	
	public TestAI(int x, int y) {
		super(x, y, "/spaceships/ai.png", Geometry.CIRCLE, true);
	}
	
	@Override
	public void tick() {
		try {
			// Checking if within range of player
			if(this.inRange(World.player, 800)) {
				this.lookAt(World.player);
				if(this.inRange(World.player, 400)) {
					this.applyVelDamping(0.95);
				}
				else this.moveForward(thrust, speed);
				
				// Picking a random action
				int action = (int) (Math.random() * 100);
				
				switch(action) {
				case 0:
					// Shoot the player
					this.lookAt(World.player);
					World.bullets.add(new Bullet((int) this.getCenterX(), (int) this.getCenterY(), this.velX, this.velY, this.getRotation()));
					break;
				}
			}
			else this.applyVelDamping(0.97);
			
			this.applyForces();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}
}
