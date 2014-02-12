package projectbanana.main.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import projectbanana.Core;
import projectbanana.main.Engine;
import userinterface.item.ButtonItem;
import userinterface.item.InteractiveItem;
import userinterface.page.Page;
import userinterface.window.Window;

public class MainMenu extends Page {
	
	private static final long serialVersionUID = 1L;
	
	private ButtonItem startButton = new ButtonItem(this, 0, 0, "Play", new Font(Engine.FONT_STYLE, Font.BOLD, 100), Engine.TEXT_COLOR, Color.CYAN);
	private ButtonItem optionsButton = new ButtonItem(this, 0, 120, "Options", new Font(Engine.FONT_STYLE, Font.BOLD, 100), Engine.TEXT_COLOR, Color.CYAN);
	private ButtonItem exitButton = new ButtonItem(this, 0, 240, "Exit", new Font(Engine.FONT_STYLE, Font.BOLD, 100), Engine.TEXT_COLOR, Color.RED);
	
	public MainMenu(Window window, int x, int y, int width, int height, String resourcePath) {
		super(window, x, y, width, height, resourcePath);
		this.setBackground(Engine.MENU_COLOR);
	}
	
	@Override
	public void handleMousePress(InteractiveItem item) {
		if(item.equals(startButton)) Core.ENGINE.start();
		else if(item.equals(optionsButton)) Engine.window.setPage(new OptionsMenu(this.getWindow(), 0, 0, this.getWidth(), this.getHeight(), this.getResourcePath()));
		else if(item.equals(exitButton)) System.exit(0);
	}
}
