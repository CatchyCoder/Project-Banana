package projectbanana.main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Window extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public Window(int width, int height) {
		setSize(width, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(false);
		setLayout(null);
		setFocusable(true);
		
		// Full-screen mode has been removed, it is far too laggy
//		if(true) {
//			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
//			GraphicsDevice gd = env.getDefaultScreenDevice();
//			setUndecorated(true);
//			gd.setFullScreenWindow(this);
//		}
		
		setVisible(true);
		requestFocus();
		repaint();
	}
}
