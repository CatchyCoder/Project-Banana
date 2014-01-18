package projectbanana.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

import projectbanana.Core;
import projectbanana.main.menu.MainMenu;
import projectbanana.main.menu.Menu;

public class Game implements Runnable {
	
	/* TODO:
	 * + Add a loading screen after play is pressed
	 * * Change how collision handling works, have the World check for collision, and then
	 * 		notify the objects if & what they are colliding with (calls a method all Entities
	 * 		have specifically for collision "colliding(Entity)")
	 * * Manage FPS in Options Menu (Make an IncrementItem class that contains two buttons (for inc/decrement) and a label w/a number of what you're changing)
	 * * Change the isOnScreen() method to work like the collision detection
	 * + Add a HUD - HUD image could just be another separate image (3rd layer), and the pixels could just be opaque
	 * * Use (velX, y) || (x, velY) in fidgetSpeed for Enemy Entities for cool effects
	 * * Only render stars on screen
	 * * Base direction of movement off of rotation for ALL objects -- will help with collision detection later (linear algebra)
	 * 
	 * Bugs:
	 * - Ship gets slightly bigger when on the left or top side of the map (Easier to see with circle drawn around it)
	 */
	
	public static final Dimension SIZE = new Dimension(840, 840); // 840x525 was original
	private static final double SCALE = 1;
	
	public static double MAX_FPS = 60.0;
	
	public static Window window = new Window((int) (SIZE.width * SCALE), (int) (SIZE.height * SCALE));
	public static InputHandler inputHandler = new InputHandler();
	public static World world;
	public static VolatileImage image;
	public static VolatileImage backgroundImage;
	public static final String FONT_STYLE = "Matisse ITC";
	private static Menu visibleMenu;
	private Graphics g;
	
	public static boolean sound = false;
	private static boolean isRunning = false;
	public static boolean showPerformance = true;
	
	public static double zoom = 2.0;
	
	public static double load = 0.0;
	
	public Game() {
		window.addMouseListener(inputHandler);
		window.addKeyListener(inputHandler);
		window.addMouseWheelListener(inputHandler);
		visibleMenu = new MainMenu();
		window.repaint();
	}
	
	public void start() {
		if(world == null) world = new World();
		isRunning = true;
		image = window.createVolatileImage(World.SIZE.width, World.SIZE.height);
		backgroundImage = window.createVolatileImage(SIZE.width, SIZE.height);
		removeCurrentMenu();
		
		new Thread(this).start();
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void tick() {
		world.tick();
	}
	
	private void render() {
		renderBackground();
		renderWorld();
		//World.player.getHUD().render(g);	*** Just put in PlayerEntity?? ***
		
		if(inputHandler.isMenuCalled()) inputHandler.showCalledMenu();
		else {
			double x = World.player.getCenterX();
			double y = World.player.getCenterY();
			
			g = backgroundImage.getGraphics();
			g.drawImage(image, 0, 0, SIZE.width, SIZE.height, 
					(int)(x - (SIZE.width / zoom)), (int)(y - (SIZE.height / zoom)), 
					(int)(x + (SIZE.width / zoom)), (int)(y + (SIZE.height / zoom)), null);
			
			g = window.getGraphics();
			g.drawImage(backgroundImage, 0, 0, window.getSize().width, window.getSize().height, null);
		}
		
		g.dispose();
	}
	
	private void renderBackground() {
		g = backgroundImage.getGraphics();
		g.setColor(new Color(30, 30, 30));
		g.fillRect(0, 0, SIZE.width, SIZE.height);
	}
	
	private void renderWorld() {
		g = image.getGraphics();
		world.render(g);
	}
	
	public static Menu getVisibleMenu() {
		return visibleMenu;
	}
	
	public static boolean isRunning() {
		return isRunning;
	}
	
	public static void removeCurrentMenu() {
		// Removing the menu that is currently being displayed
		if(visibleMenu != null) window.remove(visibleMenu);
		visibleMenu = null;
		window.repaint();
	}
	
	public static void showMenu(Menu menu) {
		Core.game.stop();
		removeCurrentMenu();
		
		visibleMenu = menu;
		
		window.revalidate();
		visibleMenu.revalidate();
		window.requestFocus();
		visibleMenu.requestFocus();
		window.repaint();
		visibleMenu.repaint();
	}
	
	@Override
	public void run() { 
		try {
			final double FRAME_DELAY = (1000.0 / MAX_FPS) * 1000000.0;
			final int SLEEP_TIME_NANO = 10000;
			long frameTime = System.nanoTime();
			
			if(showPerformance) {
				int frames = 0, sec = 0;
				long lastTime = System.nanoTime();
				long calcTime, spareTime;
				
				while(isRunning) {
					// Perform calculations
					calcTime = System.nanoTime();
					tick();
					render();
					calcTime = System.nanoTime() - calcTime;
					spareTime = System.nanoTime();
					
					// Wait until ready for next frame
					while(Math.abs(System.nanoTime() - frameTime) < FRAME_DELAY) Thread.sleep(0, SLEEP_TIME_NANO);
					
					spareTime = System.nanoTime() - spareTime;
					frames++;
					load = (calcTime * 100) / FRAME_DELAY;
					System.out.format("  [Frame %2s] Calc Time: %2.5s   |   Spare Time: %2.5s   |   %2.5s%s Load.\n", frames, calcTime / 1000000.0, spareTime / 1000000.0, (calcTime * 100) / FRAME_DELAY, '%');
					frameTime = System.nanoTime();
					
					// Calculating FPS
					if((System.nanoTime() - lastTime) >= (1000 * 1000000)) {
						sec++;
						System.out.println(sec + ".) " + frames + " fps");
						if(spareTime > 0 && frames < MAX_FPS - 2) System.out.println("! === WARNING === ! --> There is spare time, yet max fps is not reached.");
						System.out.println();
						frames = 0;
						lastTime = System.nanoTime();
					}
				}
			}
			else {
				while(isRunning) {
					// Perform calculations
					tick();
					render();
					
					// Wait until ready for next frame
					while((System.nanoTime() - frameTime) < FRAME_DELAY) Thread.sleep(0, SLEEP_TIME_NANO);
					frameTime = System.nanoTime();
				}
			}
			
			image.flush();
			backgroundImage.flush();
		}
		catch(Exception e) {
			System.out.println("Error running main loop:\n");
			e.printStackTrace();
		}
	}
}
