package projectbanana.main.menu;

import java.awt.Color;

import projectbanana.main.Engine;
import userinterface.item.ButtonItem;
import userinterface.item.InteractiveItem;
import userinterface.page.Page;
import userinterface.window.Window;

public class MainMenu extends Page {
	
	private static final long serialVersionUID = 1L;
	
	private ButtonItem startButton = new ButtonItem(this, 0, 100, "Start.png", "StartHover.png");
	private ButtonItem optionsButton = new ButtonItem(this, 0, 200, "Options.png", "OptionsHover.png");
	private ButtonItem exitButton = new ButtonItem(this, 0, 300, "Exit.png", "ExitHover.png");
	
	public MainMenu(Window window, int x, int y, int width, int height, String resourcePath) {
		super(window, x, y, width, height, resourcePath);
		this.setBackground(Color.DARK_GRAY);
	}
	
	@Override
	public void handleMousePress(InteractiveItem item) {
		if(item.equals(startButton)) Engine.start();
		else if(item.equals(optionsButton)) Engine.window.setPage(new OptionsMenu(this.getWindow(), 0, 0, this.getWidth(), this.getHeight(), this.getResourcePath()));
		else if(item.equals(exitButton)) System.exit(0);
	}
}
