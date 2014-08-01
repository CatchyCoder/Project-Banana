package projectbanana.main.entity.enemy;

import java.awt.Graphics;

import projectbanana.main.World;
import projectbanana.main.entity.BufferedEntity;
import projectbanana.main.entity.Bullet;
import projectbanana.main.entity.Entity;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;
import projectbanana.main.values.Rotation;

public class TestAI extends BufferedEntity {
	
	private double thrust = 0.15, speed = 5.0, passiveSpeed = 0.75;
	private int visionRange = (int) (Math.random() * 200) + 700;
	private int shootDis = (int) (Math.random() * 400) + 300;
	private boolean movesRandomly = true;
	
	public TestAI(int x, int y) {
		super(x, y, "/spaceships/ai.png", Geometry.CIRCLE, EntityType.ENEMY, true);
		
		// Giving the entity a random start rotation
		this.setRotation(this.randomRotation());
	}
	
	@Override
	public void tick() {
		try {
			// Checking if within range of player
			if(this.inRange(World.player, visionRange)) {
				this.lookAt(World.player);
				if(this.inRange(World.player, shootDis))
					this.applyVelDamping(0.95);
				else
					this.moveForward(thrust, speed);
				
				// Picking a random action
				int action = (int) (Math.random() * 300);
				
				switch(action) {
				case 0:
					// Shoot the player
					this.lookAt(World.player);
					
					
					
					// Giving the Entity a little bit of error
					//this.setRotation(this.getRotation());
					
					new Bullet((int) this.getCenterX(), (int) this.getCenterY(), this.velX, this.velY, this.getRotation(), this.getType());
					break;
				}
			}
			else {
				if(movesRandomly) {
					double turnAmount = (Math.PI / ((Math.random() * 75) + 150));
					turnAmount *= ((int) (Math.random() * 2) == 0 ? -1 : 1);
					this.setRotation(this.getRotation() + turnAmount);
					
					this.moveForward(thrust, passiveSpeed);
				}
				else this.applyVelDamping(0.97);
			}
			
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

	@Override
	public void handleCollision(Entity entity) {
		// If colliding with the players bullet
		if(entity instanceof Bullet && !entity.getType().equals(this.getType())) {
			isDone = true;
		}
	}
}
