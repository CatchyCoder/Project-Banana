package projectbanana.main.entity.enemy;

import java.awt.Graphics;

import projectbanana.main.World;
import projectbanana.main.entity.BufferedEntity;
import projectbanana.main.entity.Bullet;
import projectbanana.main.entity.Entity;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;
import projectbanana.main.values.Rotation;

public class EnemyCarrierEntity extends BufferedEntity {

	private double thrust = 0.025, speed = 1, turnThrust = 0.001, turnSpeed = 0.01;
	private boolean orbiting = false, turned = false;
	
	private int turnCount = 0;
	
	public EnemyCarrierEntity(int x, int y) {
		super(x, y, "/HQ/homebase.jpg", Geometry.RECTANGLE, EntityType.ENEMY, true);
		
		// Rotate the ship towards the center of the map
		this.lookAt(World.SIZE.width / 2, World.SIZE.height / 2);
	}

	@Override
	public void tick() {
		try {
			this.moveForward(thrust, speed);
			
			if(orbiting) {
				if(turned) {
					if(rotVel != 0)
						this.applyRotVelDamping();
				}
				else {
					// Make an initial turn to begin the orbit
					turnCount++;
					if(turnCount < 100)
						this.turn(Rotation.CLOCKWISE, turnSpeed, turnThrust);
					else {
						turned = true;
						//this.rotAcc = 0;
					}
						
					//rotateImage();
				}
			}
			else {
				// Check if the ship has entered the world
				double centerX = getCenterX();
				double centerY = getCenterY();
				if(centerX >= 0 && centerX <= World.SIZE.width &&
						centerY >= 0 && centerY <= World.SIZE.height) {
					// Ship is now ready to orbit players base
					orbiting = true;
				}
			}
			
			
			
			// Moves the ship around in circles
			//if((int) (Math.random() * 2) == 0) this.setRotation(this.getRotation() * 1.0009);
			
			// Chance to spawn an enemy
			//if((int) (Math.random() * 500) == 0) spawnShip();
			
			this.applyForces();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void spawnShip() {
		new SeekerEntity((int) this.getCenterX(), (int) this.getCenterY());
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
