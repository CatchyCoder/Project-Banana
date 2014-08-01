package projectbanana.main.entity.enemy;

import java.awt.Graphics;

import projectbanana.main.CollisionEvent;
import projectbanana.main.World;
import projectbanana.main.entity.BufferedEntity;
import projectbanana.main.entity.Bullet;
import projectbanana.main.entity.Entity;
import projectbanana.main.util.Sound;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;

public class HomingMineEntity extends BufferedEntity {
	
	private double homingSpeed = 0.1;
	private double speed = 1;
	private double range = 275;
			
	public HomingMineEntity(int x, int y) {
		super(x, y, "/spaceships/SpaceShipSmallNew.png", Geometry.CIRCLE, EntityType.ENEMY, false);
		velDamping = 0.99;
		boundingRad *= 0.5;
	}
	
	@Override
	public void tick() {
		try {
			double sqrDisFromPlayer = getSqrDisFrom(World.player);
			
			this.lookAt(World.player);
			
			// If entity is in range of Player
			if(this.inRange(World.player, range)) {
				this.accForward(homingSpeed);
				
				// If close to player, damp the velocity
				if(sqrDisFromPlayer <= Math.pow(World.player.getWidth(), 2)) this.applyVelDamping();
			}
			else
				this.moveForward(speed, speed);
			
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
