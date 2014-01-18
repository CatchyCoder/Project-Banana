package projectbanana.main.menu;

import projectbanana.main.Game;
import projectbanana.main.values.MenuId;

public class OptionsMenu extends Menu {
	
	private static final long serialVersionUID = 1L;
	
	private OnOffButton soundButton = new OnOffButton(this, 0, 0, "SoundOn.png", "SoundOn.png", "SoundOff.png", "SoundOff.png", Game.sound);

	public OptionsMenu() {
		super(MenuId.OPTIONS_MENU.getId());	}

	@Override
	public void buttonReleased(Button button) {
		Game.sound = !Game.sound;
		if(button.equals(soundButton)) soundButton.flipState();
		
		// Must repaint the menu on the screen to update the button
		Game.showMenu(new OptionsMenu());
	}

}
