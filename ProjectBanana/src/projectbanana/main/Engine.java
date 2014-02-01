package projectbanana.main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

import projectbanana.Core;
import projectbanana.main.menu.LoadingPage;
import projectbanana.main.menu.MainMenu;
import userinterface.window.Window;

public final class Engine implements Runnable {
	
	/* TODO:
	 * * Clean up code
	 * * Force ALL images to be loaded upon startup, somehow, some-way
	 * 
	 * * Make every entity draw itself on a bufferedimage, and then draw that image DONE
	 * + Pause the game if it looses focus DONE
	 * + Add a loading screen after play is pressed
	 * * Change how collision handling works, have the World check for collision, and then
	 * 		notify the objects if & what they are colliding with (calls a method all Entities
	 * 		have specifically for collision "colliding(Entity)")
	 * * Manage FPS in Options Menu (Use 2 ButtonItems and 1 TextItem)
	 * * Change the isOnScreen() method to work like the collision detection (WILL FIX EDGE OF WORLD BUG)
	 * + Add a HUD
	 * * Use (velX, y) || (x, velY) in fidgetSpeed for Enemy Entities for cool effects
	 * * Only render stars on screen
	 * * Base direction of movement off of rotation for ALL objects -- will help with collision detection later (linear algebra)
	 * + Add collision detection with edge of world
	 * 
	 * Bugs:
	 * * NOT EXACTLY A BUG.. but there is a slight chance that the menu will not pop put correctly
	 * 		when stopping the game (if stop() gets called inside the isRunning() if statement in render())
	 * - Sometimes, if load a menu while playing the game and your mouse is over the menu buttons,
	 * 		the screen does not refresh
	 * - Frame timing gets messed up once you stop and start the game again... is noticeable when you
	 * 		stop and start the game several times at 1 frame a second
	 * - Ship gets slightly bigger when on the left or top side of the map (Easier to see with circle drawn around it)
	 */
	
	public static final Dimension SIZE = new Dimension(840, 840);
	private static final double SCALE = 1;
	
	public static double MAX_FPS = 60.0;
	public static double zoom = 2.0;
	public static double load = 0.0;
	
	public static Window window = new Window((int) (SIZE.width * SCALE), (int) (SIZE.height * SCALE));
	public static InputHandler gameInputHandler = new InputHandler(); // Have a separate Input just for the game
	public static World world;
	
	public static VolatileImage image;
	private Graphics g;
	
	public static final String FONT_STYLE = "Matisse ITC";
	private static final int baseColor = 235;
	public static final Color TEXT_COLOR = new Color(baseColor + 15, baseColor + 10, baseColor);
	public static final Color MENU_COLOR = new Color(25, 25, 25);
	
	public static boolean sound = false;
	private static boolean isRunning = false;
	public static boolean showPerformance = true;
	
	private Thread gameThread;
	
	public Engine() {
		// Adding input for the actual game
		window.addKeyListener(gameInputHandler);
		window.addMouseWheelListener(gameInputHandler);
		
		// Displaying the main menu
		window.addPage(new MainMenu(window, 0, 0, window.getWidth(), window.getHeight(), "/menu/"));
		window.setVisible(true);
	}
	
	public void loadWorld() {
		window.removeAllPages();
		if(world != null) return;
		
		// Displaying loading page, loading world, and then displaying the world
		window.setPage(new LoadingPage(window, 0, 0, window.getSize().width, window.getSize().height, "/menu/"));
		world = new World();
		window.removeAllPages(); 
	}
	
	public void start() {
		image = window.createVolatileImage(World.SIZE.width, World.SIZE.height);
		
		isRunning = true;
		gameThread = new Thread(Core.ENGINE);
		gameThread.start();
	}
	
	public void stop() {
		isRunning = false;
		Engine.window.setPage(new MainMenu(Engine.window, 0, 0, Engine.SIZE.width, Engine.SIZE.height, "/menu/"));
	}
	
	private void tick() {
		// If the window has lost focus, pause the game
		if(!window.isFocusOwner()) stop();
		
		world.tick();
	}
	
	private void render() {
		g = image.getGraphics();
		renderWorld();
				
		double x = World.player.getCenterX();
		double y = World.player.getCenterY();
		
		int xSource = (int)(x - (SIZE.width / zoom));
		int ySource = (int)(y - (SIZE.height / zoom));
		int xSource2 = (int)(x + (SIZE.width / zoom));
		int ySource2 = (int)(y + (SIZE.height / zoom));
		
		// If the user camera starts to reach the end of the map, stop the camera
		if(xSource <= 0 || ySource <= 0 || xSource2 >= image.getWidth() || ySource2 >= image.getHeight()) {
			if(xSource <= 0) {
				xSource = 0;
				xSource2 = (int) (SIZE.width * 2 / zoom);
			}
			if(ySource <= 0) {
				ySource = 0;
				ySource2 = (int) (SIZE.height * 2 / zoom);
			}
			if(xSource2 >= image.getWidth()) {
				xSource2 = image.getWidth();
				xSource = image.getWidth() - (int) (SIZE.width * 2 / zoom);
			}
			if(ySource2 >= image.getHeight()) {
				ySource2 = image.getHeight();
				ySource = image.getHeight() - (int) (SIZE.height * 2 / zoom);
			}
		}
		
		// Checking once more if the game is running, since the game can be
		// stopped at any time (in the middle of a frame), we need to make sure
		// we still want to draw on the screen.
		if(isRunning) {
			g = window.getGraphics();
			g.drawImage(image, 0, 0, window.getWidth(), window.getHeight(), 
					xSource, ySource, xSource2, ySource2, null);
		}
		
		g.dispose();
	}
	
	private void renderWorld() {
		g = image.getGraphics();
		world.render(g);
	}
	
	public static boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void run() {
		loadWorld();
		
		try {
			// Calculating how much time for each frame, and multiplying it by 1 million to convert it to nanoseconds
			double FRAME_DELAY = (1000.0 / MAX_FPS) * 1000000.0;
			// The amount of time it sleeps before it checks if it needs to advance to the next frame
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
					
					// Perform more calculations
					spareTime = System.nanoTime() - spareTime;
					frames++;
					load = (calcTime * 100) / FRAME_DELAY;
					System.out.format("  [Frame %2s] Calc Time: %2.5s   |   Spare Time: %2.5s   |   %2.5s%s Load.\n", frames, calcTime / 1000000.0, spareTime / 1000000.0, load, '%');
					frameTime = System.nanoTime();
					
					// Calculating FPS
					if((System.nanoTime() - lastTime) >= (1000 * 1000000)) {
						sec++;
						System.out.println(sec + ".) " + frames + " fps");
						if(spareTime > 0 && frames < MAX_FPS - 2) System.err.println("! === WARNING === ! --> There is spare time, yet max fps is not reached.");
						System.out.println();
						frames = 0;
						lastTime = System.nanoTime();
					}
					
					// FPS could have changed... so recalculating
					FRAME_DELAY = (1000.0 / MAX_FPS) * 1000000.0;
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
			g.dispose();
		}
		catch(Exception e) {
			System.err.println("Error running main loop:\n");
			e.printStackTrace();
		}
	}
}
