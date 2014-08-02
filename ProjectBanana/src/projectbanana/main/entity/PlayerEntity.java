package projectbanana.main.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import projectbanana.main.Engine;
import projectbanana.main.util.Sound;
import projectbanana.main.values.EntityType;
import projectbanana.main.values.Geometry;
import projectbanana.main.values.Rotation;

public abstract class PlayerEntity extends BufferedEntity {
	
	protected double speed = 10.0;
	protected double thrust = 0.25;
	protected double rotSpeed = 0.075;
	protected double rotThrust = rotSpeed * 0.05;
	
	protected double health = 100.0;
	private double reloadTime = 5; //333
	
	private long startTime = 0;
	
	private static final String PATH = "/spaceships/";
	private final String imagePath;
	private final String thrustImagePath;
	
	public ArrayList<Point> points = new ArrayList<Point>();
	
	private VolatileImage HUD = Engine.window.createVolatileImage(Engine.SIZE.width, Engine.SIZE.height);
	
	public PlayerEntity(int x, int y, String imageName, String thrustImageName) {
		super(x, y, PATH + imageName, Geometry.CIRCLE, EntityType.FRIENDLY, true);
		imagePath = PATH + imageName;
		thrustImagePath = PATH + thrustImageName;
	}
	
	@Override
	public void tick() {
		try {
			// Re-spawn the spaceship
			if(Engine.gameInputHandler.isEnter()) this.respawn();
			
			// Calculate movement
			else {
				// If turning
				if(Engine.gameInputHandler.isLeft()) this.turn(Rotation.COUNTER_CLOCKWISE.getId(), rotSpeed, rotThrust);
				if(Engine.gameInputHandler.isRight()) this.turn(Rotation.CLOCKWISE.getId(), rotSpeed, rotThrust);
				
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
			
			// Shooting
			if(Engine.gameInputHandler.isSpace()) {
				if(System.currentTimeMillis() - startTime >= reloadTime) {
					startTime = System.currentTimeMillis();
					new Bullet((int) this.getCenterX(), (int) this.getCenterY(), this.velX, this.velY, this.getRotation(), EntityType.FRIENDLY);
				}
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
		/*int value = (int) (Math.min(this.vel / speed, 1) * 255);
		double blackWhitePercent = 0.33;
		g.setColor(new Color((int)(value * blackWhitePercent), (int)(value * blackWhitePercent), value));
		int gaugeHeight = (int) (200 * health / 100);
		g.fillRect(Engine.cameraX, Engine.cameraY2, (int) (150 / Engine.zoom), (int) -((this.vel * 30) / Engine.zoom));
		
		g.setColor(Color.RED);
		g.drawString("Health: ", Engine.cameraX, (int) (Engine.cameraY * 1.1));
		*/
		
		g = HUD.getGraphics();
		g.setColor(new Color(255, 0, 0, 20));
		g.fillRect(0, 0, HUD.getWidth(), HUD.getHeight());
		
		//g = Engine.window.getGraphics();
		//g.drawImage(HUD, 0, 0, null);
		
	}
	
	@Override
	public void handleCollision(Entity entity) {
		// If colliding with an enemy's bullet
		if(entity instanceof Bullet) {
			if(!entity.getType().equals(this.getType())) {
				health -= 10;
				if(health <= 0)
					isDone = true;
			}
		}
		else {
			accX = accY = 0;
			velX = -velX / 1.5;
			velY = -velY / 1.5;
			x = lastValidX;
			y = lastValidY;
			Sound.BUMP.play();
			points.add(new Point((int)entity.getCenterX(), (int)entity.getCenterY()));
		}
	}
	
	public VolatileImage getHUD() {
		return HUD;
	}
}
