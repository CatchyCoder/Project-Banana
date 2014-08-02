package projectbanana.main.entity.enemy;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import projectbanana.main.Engine;
import projectbanana.main.World;
import projectbanana.main.entity.BufferedEntity;
import projectbanana.main.entity.Bullet;
import projectbanana.main.entity.Entity;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;
import userinterface.InteractiveComponent;

public class EnemyCarrierEntity extends BufferedEntity {

	private double thrust = 0.025, speed = 1;
	
	public EnemyCarrierEntity(int x, int y) {
		super(x, y, "/HQ/homebase.jpg", Geometry.RECTANGLE, EntityType.ENEMY, true);
		this.setRotation(this.randomRotation());
	}

	@Override
	public void tick() {
		try {
			this.moveForward(thrust, speed);
			// Moves the ship around in circles
			if((int) (Math.random() * 2) == 0) this.setRotation(this.getRotation() * 1.0009);
			
			// Chance to spawn an enemy
			if((int) (Math.random() * 500) == 0) spawnShip();
			
			this.applyForces();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
