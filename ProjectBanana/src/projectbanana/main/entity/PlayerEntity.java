package projectbanana.main.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import projectbanana.main.CollisionEvent;
import projectbanana.main.Engine;
import projectbanana.main.World;
import projectbanana.main.util.Sound;
import projectbanana.main.values.Geometry;
import projectbanana.main.values.Rotation;

public abstract class PlayerEntity extends BufferedEntity {
	
	protected double speed = 10.0;
	protected double thrust = 0.25;
	protected double rotSpeed = 0.075;
	protected double rotThrust = rotSpeed * 0.05;
	
	protected double health = 100.0;
	
	private static final String PATH = "/spaceships/";
	private final String imagePath;
	private final String thrustImagePath;
	
	public ArrayList<Point> points = new ArrayList<Point>();
	
	//private VolatileImage HUD = Engine.window.createVolatileImage(Engine.SIZE.width, Engine.SIZE.height);
	
	public PlayerEntity(int x, int y, String imageName, String thrustImageName) {
		super(x, y, PATH + imageName, Geometry.CIRCLE, true);
		imagePath = PATH + imageName;
		thrustImagePath = PATH + thrustImageName;
	}
	
	@Override
	public void tick() {
		try {
			/*for(Entity entity : World.enemies) {
				CollisionEvent event = this.isCollidingWith(entity);
				if(event.isColliding()) {
					
				}
			}*/
			
			
			
			// Shooting
			if(Engine.gameInputHandler.isSpace()) {
				Bullet bullet = new Bullet((int) this.getCenterX(), (int) this.getCenterY(), this.velX, this.velY, this.getRotation());
				World.bullets.add(bullet);
			}
			
			// Respawn the spaceship
			if(Engine.gameInputHandler.isEnter()) this.respawn();
			
			// Calculate movement
			else {
				// If turning
				if(Engine.gameInputHandler.isLeft() || Engine.gameInputHandler.isRight()) {
					if(Engine.gameInputHandler.isLeft()) this.turn(Rotation.COUNTER_CLOCKWISE.getId(), rotSpeed, rotThrust);
					if(Engine.gameInputHandler.isRight()) this.turn(Rotation.CLOCKWISE.getId(), rotSpeed, rotThrust);
				}
				
				// If not turning
				else if(rotVel != 0) this.applyRotVelDamping();
				
				// If accelerating
				if(Engine.gameInputHandler.isDown()) {
					this.moveForward(thrust, speed);
					image = loadImage(thrustImagePath);
				}
				
				// If not accelerating
				else {
					if(vel != 0) this.applyVelDamping();
					image = loadImage(imagePath);
				}
				
				this.applyForces();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}
	
	public void renderHUD(Graphics g) {
		// Rendering the players HUD
		int value = (int) (Math.min(this.vel / speed, 1) * 255);
		double percentage = 0.25;
		g.setColor(new Color((int)(value * percentage), (int)(value * percentage), value));
		int gaugeHeight = (int) (200 * health / 100);
		g.fillRect(Engine.cameraX, Engine.cameraY2, 50, (int) -(this.vel * 10));
	}
	
	@Override
	public void handleCollision(Entity entity) {
		accX = accY = 0;
		velX = -velX;
		velY = -velY;
		x = lastValidX;
		y = lastValidY;
		Sound.BUMP.play();
		points.add(new Point((int)entity.getCenterX(), (int)entity.getCenterY()));
		health -= 10;
	}
}
