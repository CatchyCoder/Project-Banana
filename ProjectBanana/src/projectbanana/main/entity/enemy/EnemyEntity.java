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

public class EnemyEntity extends BufferedEntity {
	
	private double homingSpeed = 0.25;
	private double fidgetSpeed = 0.05;
	private double range = 200;
			
	public EnemyEntity(int x, int y) {
		super(x, y, "/spaceships/SpaceShipSmallNew.png", Geometry.CIRCLE, EntityType.ENEMY, false);
		velDamping = 0.99;
		boundingRad *= 0.5;
	}
	
	@Override
	public void tick() {
		double sqrDisFromPlayer = getSqrDisFrom(World.player);
		
		// If entity is in range of Player
		if(this.inRange(World.player, range)) {
			this.lookAt(World.player);
			setRotation(getRotation() + Math.PI); // Looking in the opposite direction of the player
			
			this.accForward(homingSpeed);
			
			// If close to player, damp the velocity
			if(sqrDisFromPlayer <= Math.pow(World.player.getWidth(), 2)) this.applyVelDamping();
		}
		else {
			accX = accY = 0;
			// If not moving, fidget around
			//if(vel == 0) {
				int dir = ((int) (Math.random() * 2) == 0 ? 1 : -1);
				velX += fidgetSpeed * dir;
				dir = ((int) (Math.random() * 2) == 0 ? 1 : -1);
				velY += fidgetSpeed * dir;
			//}
			// Otherwise slow down
			//else this.applyVelDamping();
		}
		
		this.applyForces();
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
