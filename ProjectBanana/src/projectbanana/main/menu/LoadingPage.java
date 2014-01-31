package projectbanana.main.menu;

import java.awt.Font;

import projectbanana.main.Engine;
import userinterface.item.TextItem;
import userinterface.page.Page;
import userinterface.window.Window;

public class LoadingPage extends Page {
	
	private static final long serialVersionUID = 1L;
	
	private final TextItem MESSAGE = new TextItem(this, 0, 0, "Loading World, Please Wait...", new Font(Engine.FONT_STYLE, Font.BOLD, 100));

	public LoadingPage(Window window, int x, int y, int width, int height, String resourcePath) {
		super(window, x, y, width, height, resourcePath);
	}
}
