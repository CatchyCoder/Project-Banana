package projectbanana.main.menu;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JPanel;

import projectbanana.main.Game;

public abstract class Menu extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	
	protected final int MENU_ID;
	protected final int RIGHT = Game.window.getSize().width - 550;				 // ADD THIS TO VALUES ENUM .. AND JUST CALCULATE IT IN BUTTON (just check if it is RIGHT then minus the width of the image in the button constructor
	
	public Menu(int MENU_ID) {
		this.MENU_ID = MENU_ID;
		
		setBounds(0, 0, Game.window.getSize().width, Game.window.getSize().height);
		setLayout(null);
		setFocusable(true);
		setVisible(true);
		setBackground(Color.DARK_GRAY);
		
		addKeyListener(Game.inputHandler);
		addMouseListener(Game.inputHandler);
		addMouseMotionListener(Game.inputHandler);
		
		Game.window.add(this);
		requestFocus();
	}
	
	public abstract void buttonReleased(Button button);
	
	public void resetButtonImages() {
		for(Button button : buttons) button.displayImage();
	}
	
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	
	public int getMenuId() {
		return MENU_ID;
	}
}
