package projectbanana.main.menu;

import projectbanana.main.Engine;

import userinterface.item.InteractiveItem;
import userinterface.item.StateButtonItem;
import userinterface.page.Page;
import userinterface.window.Window;

public class OptionsMenu extends Page {
	
	private static final long serialVersionUID = 1L;
	
	private String[] paths = {
			"SoundOn.png",
			"SoundOff.png"
	};
	private String[] hoverPaths = {
			"SoundOn.png",
			"SoundOff.png"
	};
	private StateButtonItem soundSwitch = new StateButtonItem(this, 0, 0, paths, hoverPaths);
	
	public OptionsMenu(Window window, int x, int y, int width, int height, String resourcePath) {
		super(window, x, y, width, height, resourcePath);
		
		// Setting the soundSwitch to the correct state
		if(Engine.sound) soundSwitch.setState(0);
		else soundSwitch.setState(1);
	}

	@Override
	public void handleMousePress(InteractiveItem item) {		
		if(item.equals(soundSwitch)) {
			Engine.sound = !Engine.sound;
			System.out.println("Sound change : " + Engine.sound);
		}
	}

}
