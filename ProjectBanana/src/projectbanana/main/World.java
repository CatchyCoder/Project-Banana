package projectbanana.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import projectbanana.main.entity.*;
import projectbanana.main.entity.enemy.EnemyCarrierEntity;
import projectbanana.main.entity.enemy.EnemyEntity;
import projectbanana.main.entity.enemy.TestAI;
import projectbanana.main.values.Geometry;

public class World {
	
	private static final double SCALE = 3; // 3 is normal
	public static final Dimension SIZE = new Dimension((int) (1000 * SCALE), (int) (1000 * SCALE));
	
	public static ArrayList<Entity> entities = Entity.entities;
	public static HomeBaseEntity HQ = new HomeBaseEntity(SIZE.width / 2, SIZE.height / 2);
	public static TrinityShipEntity player = new TrinityShipEntity((int) HQ.getCenterX(), (int) HQ.getCenterY() - 100);
	
	private Point[] stars = new Point[(int) (SIZE.width * SIZE.height * 0.00006)];
	
	private int wave = 1;
	
	// For efficiency statistics
	private ArrayList<Integer> loads = new ArrayList<Integer>();
	private boolean showCollisionBoxes = false;
	
	public World() {
		// Generating stars
		for(int x = 0; x < stars.length; x++) stars[x] = new Point(randomX(), randomY());
		
		loadWave();
	}
	
	public void tick() {
		
		for(int n = 0; n < entities.size(); n++) entities.get(n).tick();
		
		checkForCollisions();
		
		// Check if any entities need to be removed
		for(int n = 0; n < entities.size(); n++) {
			if(entities.get(n).isDone()) {
				entities.get(n).onDone();
				entities.remove(n);
			}	
		}
		
		// Checking if the wave is over
		if(entities.size() == 1 && entities.get(0).equals(player));
	}
	
	public void render(Graphics g) {
		renderBackground(g);
		renderStars(g);
		
		for(Entity entity : entities)
			if(entity.isOnScreen()) entity.render(g);
		
		/*// Rendering points
		g.setColor(Color.GREEN);
		for(Point point : player.points) {
			g.drawLine(point.x, point.y, point.x, point.y);
		}*/
		
		renderBorder(g);
		
		
		if(showCollisionBoxes) renderCollisionBoxes(g);
		if(Engine.showPerformance) renderPerformance(g);
		
		// HUD must be on top of everything, so it is rendered last
		player.renderHUD(g);
	}
	
	private void loadWave() {
		double easiness = 10; // 0 is the absolute hardest
		int maxEnemies = 50;
		int enemyCount = (int) ((wave / (wave + easiness)) * maxEnemies);
		
		// Generating enemies
		for(int x = 0; x < (int) (enemyCount * 0.45); x++) new EnemyEntity(randomX(), randomY());
		for(int x = 0; x < (int) (enemyCount * 0.45); x++) new TestAI(randomX(), randomY());
		for(int x = 0; x < (int) (enemyCount * 0.1); x++) new EnemyCarrierEntity(randomX(), randomY());		
	}
	
	private void renderBackground(Graphics g) {
		int value = 15;
		g.setColor(new Color(value, value, value));
		g.fillRect(0, 0, SIZE.width, SIZE.height);
	}
	
	private void renderStars(Graphics g) {
		g.setColor(new Color(245, 245, 245));
		int size = 3;
		
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
	
	public int randomX() {
		return (int) (Math.random() * SIZE.width);
	}
	
	public int randomY() {
		return (int) (Math.random() * SIZE.height);
	}
}
