package projectbanana.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import projectbanana.main.menu.MainMenu;

public class InputHandler extends KeyAdapter implements MouseListener, MouseMotionListener, MouseWheelListener {
	
	private boolean isLeft, isRight, isUp, isDown, isEnter;
	private boolean isMenuCalled;
	
	public void showCalledMenu() {
		
		Engine.stop();
		Engine.window.setPage(new MainMenu(Engine.window, 0, 0, Engine.SIZE.width, Engine.SIZE.height, "/menu/"));
		
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
			if(Engine.isRunning()) isMenuCalled = true;
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
	public void mouseClicked(MouseEvent mClicked) {}

	@Override
	public void mouseEntered(MouseEvent mEntered) {}

	@Override
	public void mouseExited(MouseEvent mExited) {}

	@Override
	public void mousePressed(MouseEvent mPressed) {}

	@Override
	public void mouseReleased(MouseEvent mReleased) {}
	
	@Override
	public void mouseDragged(MouseEvent mDragged) {}
	
	@Override
	public void mouseMoved(MouseEvent mMoved) {}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent mWheel) {
		double amount = 0.1;
		if(mWheel.isControlDown()) Engine.zoom = 2;
		else if(mWheel.getWheelRotation() == 1) Engine.zoom -= amount;
		else Engine.zoom += amount;
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
