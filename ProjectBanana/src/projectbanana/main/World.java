package projectbanana.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import projectbanana.main.entity.*;
import projectbanana.main.values.Geometry;

public class World {
	
	private static final double SCALE = 3; // 3 is normal
	public static final Dimension SIZE = new Dimension((int) (1000 * SCALE), (int) (1000 * SCALE));
	
	public static ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public static HomeBaseEntity homeBase = new HomeBaseEntity(SIZE.width / 2, SIZE.height / 2);
	
	public static EnemyCarrierEntity[] carriers = new EnemyCarrierEntity[10];
	
	public static TrinityShipEntity player = new TrinityShipEntity((int) homeBase.getCenterX(), (int) homeBase.getCenterY() + 200);
	//public static EnemyEntity[] enemies = new EnemyEntity[(int) (((SIZE.width * SIZE.height) / 100) * 0.01)];
	public static EnemyEntity[] enemies = new EnemyEntity[0];
	public static ArrayList<TestAI> AIs = new ArrayList<TestAI>();
	private Point[] stars = new Point[(int) (((SIZE.width * SIZE.height) / 100) * 0.006)];
	public static ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	// For efficiency statistics
	private ArrayList<Integer> loads = new ArrayList<Integer>();
	
	private boolean showCollisionBoxes = false;
	
	public World() {
		// Loading stars
		for(int x = 0; x < stars.length; x++) stars[x] = new Point((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height));
		
		// Loading enemies
		for(int x = 0; x < enemies.length; x++) enemies[x] = new EnemyEntity((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height));
		
		// Loading AI
		for(int x = 0; x < AIs.size(); x++) AIs.add(new TestAI((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height)));
		
		// Loading carriers
		for(int x = 0; x < carriers.length; x++) carriers[x] = new EnemyCarrierEntity((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height));
	}
	
	public void tick() {
		for(TestAI ai : AIs)ai.tick();
		for(EnemyCarrierEntity carrier : carriers) carrier.tick();
		homeBase.tick();
		player.tick();
		for(Bullet bullet : bullets) bullet.tick();
		for(EnemyEntity enemy : enemies) enemy.tick();
		
		checkForCollisions();
	}
	
	public void render(Graphics g) {
		renderBackground(g);
		renderStars(g);
		
		for(Bullet bullet : bullets) if(bullet.isOnScreen()) bullet.render(g);
		if(homeBase.isOnScreen()) homeBase.render(g);
		for(EnemyCarrierEntity carrier : carriers) if(carrier.isOnScreen()) carrier.render(g);
		for(EnemyEntity enemy : enemies) if(enemy.isOnScreen()) enemy.render(g);
		for(TestAI ai : AIs)ai.render(g);
		player.render(g);
		
		/*// Rendering points
		g.setColor(Color.GREEN);
		for(Point point : player.points) {
			g.drawLine(point.x, point.y, point.x, point.y);
		}*/
		
		renderBorder(g);
		player.renderHUD(g);
		if(showCollisionBoxes) renderCollisionBoxes(g);
		if(Engine.showPerformance) renderPerformance(g);
	}
	
	private void renderBackground(Graphics g) {
		int value = 15;
		g.setColor(new Color(value, value, value));
		g.fillRect(0, 0, SIZE.width, SIZE.height);
	}
	
	private void renderStars(Graphics g) {
		g.setColor(Color.WHITE);
		int size = 1;
		
		for(int x = 0; x < stars.length; x++) {
			g.fillRect((int) (stars[x].x), (int) (stars[x].y), size, size);
		}
	}
	
	private void renderBorder(Graphics g) {
		int width = 15;
		
		// This draws the border around the world
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, SIZE.width, width); // Top
		g.fillRect(0, SIZE.height - width, SIZE.width, width); // Bottom
		g.fillRect(0, 0, width, SIZE.height); // Left
		g.fillRect(SIZE.width - width, 0, width, SIZE.height); // Right
	}
	
	private void renderCollisionBoxes(Graphics g) {
		for(Entity entity : entities) {
			g.setColor(Color.GREEN);
			if(entity.getGeometry() == Geometry.CIRCLE)
				g.drawOval((int)(entity.getCenterX() - entity.getBoundingRad() / 2 + 0.5), (int)(entity.getCenterY() - entity.getBoundingRad() / 2 + 0.5), 
						(int) (entity.getBoundingRad() + 0.5), (int) (entity.getBoundingRad() + 0.5)); 
			else
				g.drawRect((int) (entity.getCenterX() - entity.getBoundingWidth() / 2 + 0.5), (int) (entity.getCenterY() - entity.getBoundingHeight() / 2 + 0.5), 
						(int) (entity.getBoundingWidth() + 0.5), (int) (entity.getBoundingHeight() + 0.5));
		}
	}
	
	private void renderPerformance(Graphics g) {
		if(loads.size() >= 10) loads.remove(0);
		loads.add((int) Engine.load);
		int avg = 0;
		for(Integer load : loads) avg += load;
		avg /= loads.size();
		g.setColor(Color.GREEN);
		int font = (int) (20 / Engine.zoom * 2);
		if(font < 20) g.setFont(new Font("Arial", Font.PLAIN, 20));
		else g.setFont(new Font("Arial", Font.PLAIN, font));
		g.drawString(String.valueOf(avg) + "% Load", (int) player.getX() + 100, (int) player.getY());
	}
	
	private void checkForCollisions() {
		for(Entity entity : entities) {
			// Setting collisionChecked value right away, that
			// way it doesn't check against itself
			entity.setCollisionChecked(true);
			
			for(Entity entity2 : entities) {
				// Making sure that the entity has not already been checked for collision
				if(!entity2.isCollisionChecked()) {
					if(entity.isCollidingWith(entity2).isColliding()) {
						entity.handleCollision(entity2);
						entity2.handleCollision(entity);
					}
				}
			}
		}
		
		// Resetting the 'collisionChecked' value for all entities
		for(Entity entity : entities) entity.setCollisionChecked(false);
	}
}
