package projectbanana.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import projectbanana.Core;

public class InputHandler extends KeyAdapter implements MouseWheelListener {
	
	private boolean isLeft, isRight, isUp, isDown, isEnter, isSpace;
	
	@Override
	public void keyPressed(KeyEvent pEvent) {
		int src = pEvent.getKeyCode();
		pEvent.consume();
		
		if(src == KeyEvent.VK_A || src == KeyEvent.VK_LEFT) isLeft = true;
		if(src == KeyEvent.VK_D || src == KeyEvent.VK_RIGHT) isRight = true;
		if(src == KeyEvent.VK_W || src == KeyEvent.VK_UP) isUp = true;
		if(src == KeyEvent.VK_S || src == KeyEvent.VK_DOWN) isDown = true;
		else if(src == KeyEvent.VK_ENTER) isEnter = true;
		else if(src == KeyEvent.VK_SPACE) isSpace = true;
		else if(src == KeyEvent.VK_ESCAPE) Core.ENGINE.stop();
	}
	
	@Override
	public void keyReleased(KeyEvent rEvent) {
		int src = rEvent.getKeyCode();
		rEvent.consume();
		
		if(src == KeyEvent.VK_A || src == KeyEvent.VK_LEFT) isLeft = false;
		if(src == KeyEvent.VK_D || src == KeyEvent.VK_RIGHT) isRight = false;
		if(src == KeyEvent.VK_W || src == KeyEvent.VK_UP) isUp = false;
		if(src == KeyEvent.VK_S || src == KeyEvent.VK_DOWN) isDown = false;
		else if(src == KeyEvent.VK_ENTER) isEnter = false;
		else if(src == KeyEvent.VK_SPACE) isSpace = false;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent mWheel) {
		double percentage = 0.1;
		
		// Resetting zoom amount
		if(mWheel.isControlDown()) Engine.zoom = 2;
		// Zooming out
		else if(mWheel.getWheelRotation() == 1) Engine.zoom *= 1 - percentage;
		// Zooming in
		else Engine.zoom *= 1 + percentage;
		
		mWheel.consume();
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
	
	public boolean isSpace() {
		return isSpace;
	}
}
