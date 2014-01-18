package projectbanana.main.menu;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import projectbanana.main.Game;

public class Button extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	private final String FILE_PATH = "/menu/";
	
	private ImageIcon image, hoverImage;
	
	public Button(Menu menu, int x, int y, String imageName, String hoverImageName) {
		image = new ImageIcon(this.getClass().getResource(FILE_PATH + imageName));
		hoverImage = new ImageIcon(this.getClass().getResource(FILE_PATH + hoverImageName));
		
		setBounds(x, y, image.getIconWidth(), image.getIconHeight());
		setIcon(image);
		setVisible(true);
		addKeyListener(Game.inputHandler);
		addMouseListener(Game.inputHandler);
		addMouseMotionListener(Game.inputHandler);
		
		menu.getButtons().add(this);
		menu.add(this);
	}
	
	public void displayImage() {
		setIcon(image);
	}
	
	public void displayHoverImage() {
		setIcon(hoverImage);
	}
}
