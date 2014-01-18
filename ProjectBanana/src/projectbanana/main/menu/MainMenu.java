package projectbanana.main.menu;

import projectbanana.Core;
import projectbanana.main.Game;
import projectbanana.main.values.MenuId;

public class MainMenu extends Menu {
	
	private static final long serialVersionUID = 1L;
	
	private Button startButton = new Button(this, this.RIGHT, 100, "Start.png", "StartHover.png");
	private Button optionsButton = new Button(this, this.RIGHT, 200, "Options.png", "OptionsHover.png");
	private Button exitButton = new Button(this, this.RIGHT, 300, "Exit.png", "ExitHover.png");
	
	public MainMenu() {
		super(MenuId.MAIN_MENU.getId());
	}
	
	@Override
	public void buttonReleased(Button button) {
		if(button.equals(startButton)) Core.game.start();
		else if(button.equals(optionsButton)) Game.showMenu(new OptionsMenu());
		else if(button.equals(exitButton)) System.exit(0);
	}
}
