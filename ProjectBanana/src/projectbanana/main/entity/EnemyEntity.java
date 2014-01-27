package projectbanana.main.entity;

import java.awt.Color;
import java.awt.Graphics;

import projectbanana.main.CollisionEvent;
import projectbanana.main.World;
import projectbanana.main.util.Sound;
import projectbanana.main.values.GeometryId;

public class EnemyEntity extends DrawnEntity {
	
	private static final int SIZE = 7;
	public static final double radius = SIZE / 2;
	
	private double homingSpeed = 0.25;
	private double fidgetSpeed = 0.05;
	private double range = 100;
		
	private int renderCount = 0;
	
	private Color color = new Color((int) (Math.random() * 210) + 45, 0, 0);
	
	public EnemyEntity(int x, int y) {
		super(x, y, SIZE, GeometryId.CIRCLE.getId());
		velDamping = 0.99;
	}
	
	@Override
	public void tick() {
		for(Entity entity : World.enemies) {
			if(entity != this && this.isCollidingWith(entity).isColliding()) {
				accX = accY = 0;
				velX = -velX;
				velY = -velY;
				x = lastValidX;
				y = lastValidY;
				Sound.BUMP.play();
			}
		}
		
		CollisionEvent event = this.isCollidingWith(World.player);
		if(event.isColliding()) {
			accX = accY = 0;
			velX = -velX;
			velY = -velY;
			x = lastValidX;
			y = lastValidY;
			//Sound.BUMP.play();
		}
		
		
		double sqrDisFromPlayer = getSqrDisFrom(World.player);
		//if(this.equals(World.enemys[0])) System.out.format("X Dis: %s, Y Dis: %s, Total Dis: %s\n", getXDisFrom(World.player), getYDisFrom(World.player), disFromPlayer);
		
		// If entity is in range of Player
		if(this.inRange(World.player, range)) {
			this.lookAt(World.player);
			this.accForward(-homingSpeed);
			
			// If close to player, damp the velocity
			if(sqrDisFromPlayer <= Math.pow(World.player.width, 2)) this.applyVelDamping();
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
		// Randomly change the red hue
		if(renderCount >= 10) {
			color = new Color((int) (Math.random() * 211) + 45, 0, 0);
			renderCount = 0;
		}
		
		// Draw the entity
		g.setColor(Color.DARK_GRAY);
		g.fillOval((int) this.x, (int) this.y, (int) this.width, (int) this.height);
		g.setColor(color);
		g.drawOval((int) this.x, (int) this.y, (int) this.width, (int) this.height);
		renderCount++;
	}
}
