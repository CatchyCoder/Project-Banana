package projectbanana.main.menu;

import java.awt.event.MouseEvent;

import userinterface.page.Page;
import userinterface.window.Window;

public class OptionsMenu extends Page {
	
	private static final long serialVersionUID = 1L;
	
	//private OnOffButton soundButton = new OnOffButton(this, 0, 0, "SoundOn.png", "SoundOn.png", "SoundOff.png", "SoundOff.png", Engine.sound);
	
	public OptionsMenu(Window window, int x, int y, int width, int height, String resourcePath) {
		super(window, x, y, width, height, resourcePath);
	}

	@Override
	public void mousePressed(MouseEvent event) {
		Object source = event.getSource();
		
		/*Engine.sound = !Engine.sound;
		if(source.equals(soundButton)) soundButton.flipState();
		
		// Must repaint the menu on the screen to update the button  			CHECK IF WE NEED THIS NOW
		Engine.window.setPage(new OptionsMenu());*/
	}

}
