package projectbanana.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import projectbanana.main.entity.*;

public class World {
	
	private static final int SCALE = 2;
	public static final Dimension SIZE = new Dimension((int) (1000 * SCALE), (int) (1000 * SCALE));
	
	public static TrinityShipEntity player = new TrinityShipEntity(SIZE.width / 2, SIZE.height / 2);
	
	//public static EnemyEntity[] enemies = new EnemyEntity[(int) (((SIZE.width * SIZE.height) / 100) * 0.01)];
	public static EnemyEntity[] enemies = new EnemyEntity[200];
	
	private Point[] stars = new Point[(int) (((SIZE.width * SIZE.height) / 100) * 0.006)];
	
	// For efficiency statistics
	private ArrayList<Integer> loads = new ArrayList<Integer>();
	
	public World() {
		// Loading stars
		for(int x = 0; x < stars.length; x++) stars[x] = new Point((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height));
		
		// Loading enemies
		for(int x = 0; x < enemies.length; x++) enemies[x] = new EnemyEntity((int) (Math.random() * SIZE.width), (int) (Math.random() * SIZE.height));
	}
	
	public void tick() {
		player.tick();
		for(EnemyEntity enemy : enemies) enemy.tick();
	}
	
	public void render(Graphics g) {
		renderBackground(g);
		renderParticles(g);
		
		for(EnemyEntity enemy : enemies) if(enemy.isOnScreen()) enemy.render(g); // REAL
		//for(EnemyEntity enemy : enemies) enemy.render(g);
		player.render(g);
		
		// Rendering points
		g.setColor(Color.GREEN);
		for(Point point : player.points) {
			g.drawLine(point.x, point.y, point.x, point.y);
		}
		
		renderBorder(g);
		
		if(Game.showPerformance) renderPerformance(g);
	}
	
	private void renderBackground(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, SIZE.width, SIZE.height);
	}
	
	private void renderParticles(Graphics g) {
		g.setColor(new Color(225, 225, 225));
		int size = 1;
		
		for(int x = 0; x < stars.length; x++) {
			g.fillRect((int) (stars[x].x), (int) (stars[x].y), size, size);
		}
	}
	
	private void renderBorder(Graphics g) {
		int width = 25;
		
		// This draws the border around the world
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, SIZE.width, width); // Top
		g.fillRect(0, SIZE.height - width, SIZE.width, width); // Bottom
		g.fillRect(0, 0, width, SIZE.height); // Left
		g.fillRect(SIZE.width - width, 0, width, SIZE.height); // Right
	}
	
	private void renderPerformance(Graphics g) {
		if(loads.size() >= 10) loads.remove(0);
		loads.add((int) Game.load);
		int avg = 0;
		for(Integer load : loads) avg += load;
		avg /= loads.size();
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString(String.valueOf(avg) + "% Load", (int) player.getX() + 100, (int) player.getY());
	}
}
