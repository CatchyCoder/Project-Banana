package projectbanana.main.entity;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import projectbanana.main.Engine;
import projectbanana.main.values.Geometry;
import userinterface.InteractiveComponent;

public class EnemyCarrierEntity extends BufferedEntity {

	private double thrust = 0.025, speed = 1;
	
	public EnemyCarrierEntity(int x, int y) {
		super(x, y, "/homebase/homebase.jpg", Geometry.RECTANGLE, true);
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
		Engine.world.AIs.add(new TestAI((int) this.getCenterX(), (int) this.getCenterY()));
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
