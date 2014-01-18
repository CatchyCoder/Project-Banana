package projectbanana.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import projectbanana.main.menu.Button;
import projectbanana.main.menu.MainMenu;
import projectbanana.main.values.MenuId;

public class InputHandler extends KeyAdapter implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private boolean isLeft, isRight, isUp, isDown, isEnter;
	private boolean isMenuCalled;
	
	public void showCalledMenu() {
		if(Game.getVisibleMenu() == null) Game.showMenu(new MainMenu());
		else {
			int menuId = Game.getVisibleMenu().getMenuId();
			
			if(menuId == MenuId.MAIN_MENU.getId()) System.exit(0);
			else if(menuId == MenuId.OPTIONS_MENU.getId()) { Game.showMenu(new MainMenu());
				System.out.println("Go to main menu");
			}
		}
		
		isMenuCalled = false;
	}
	
	@Override
	public void keyPressed(KeyEvent pEvent) {
		int src = pEvent.getKeyCode();
		pEvent.consume();
		
		if(src == KeyEvent.VK_A || src == KeyEvent.VK_LEFT) isLeft = true;
		if(src == KeyEvent.VK_D || src == KeyEvent.VK_RIGHT) isRight = true;
		if(src == KeyEvent.VK_W || src == KeyEvent.VK_UP) isUp = true;
		if(src == KeyEvent.VK_S || src == KeyEvent.VK_DOWN) isDown = true;
		if(src == KeyEvent.VK_ENTER) isEnter = true;
		
		// Checking if a menu was called to showHandling Menu pop-ups
		else if(src == KeyEvent.VK_ESCAPE) {
			if(Game.isRunning()) isMenuCalled = true;
			else showCalledMenu();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent rEvent) {
		int src = rEvent.getKeyCode();
		rEvent.consume();
		
		if(src == KeyEvent.VK_A || src == KeyEvent.VK_LEFT) isLeft = false;
		if(src == KeyEvent.VK_D || src == KeyEvent.VK_RIGHT) isRight = false;
		if(src == KeyEvent.VK_W || src == KeyEvent.VK_UP) isUp = false;
		if(src == KeyEvent.VK_S || src == KeyEvent.VK_DOWN) isDown = false;
		if(src == KeyEvent.VK_ENTER) isEnter = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent mClicked) {
		mClicked.consume();
	}

	@Override
	public void mouseEntered(MouseEvent mEntered) {
		Object object = mEntered.getSource();
		mEntered.consume();
		
		if(Game.getVisibleMenu() == null) return;
		
		for(Button button : Game.getVisibleMenu().getButtons()) {
			if(object.equals(button)) button.displayHoverImage();
		}
	}

	@Override
	public void mouseExited(MouseEvent mExited) {
		Object object = mExited.getSource();
		mExited.consume();
		
		if(Game.getVisibleMenu() == null) return;
		
		for(Button button : Game.getVisibleMenu().getButtons()) {
			if(object.equals(button)) button.displayImage();
		}		
	}

	@Override
	public void mousePressed(MouseEvent mPressed) {
		Object object = mPressed.getSource();
		mPressed.consume();
		
		if(Game.getVisibleMenu() == null) return;
		
		// Finding out what button was pressed, and letting the current Menu handle the rest
		for(Button button : Game.getVisibleMenu().getButtons()) {
			if(object.equals(button)) {
				button.displayHoverImage();
				Game.getVisibleMenu().buttonReleased(button);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent mReleased) {
		mReleased.consume();
	}
	
	@Override
	public void mouseDragged(MouseEvent mDragged) {
		mousePressed(mDragged);
		mDragged.consume();
	}
	
	@Override
	public void mouseMoved(MouseEvent mMoved) {
		Object object = mMoved.getSource();
		mMoved.consume();
		
		if(Game.getVisibleMenu() == null) return;
		
		// If the mouse moves on or off the JPanel, all images are set to their default image
		if(object.equals(Game.getVisibleMenu())) Game.getVisibleMenu().resetButtonImages();		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent mWheel) {
		double amount = 0.1;
		if(mWheel.isControlDown()) Game.zoom = 2;
		if(mWheel.getWheelRotation() == 1) Game.zoom -= amount;
		else Game.zoom += amount;
		mWheel.consume();
	}
	
	public boolean isMenuCalled() {
		return isMenuCalled;
	}
	
	public boolean isLeft() {
		return isLeft;
	}
	
	public boolean isRight() {
		return isRight;
	}
	
	public boolean isUp() {
		return isUp;
	}
	
	public boolean isDown() {
		return isDown;
	}
	
	public boolean isEnter() {
		return isEnter;
	}
}
