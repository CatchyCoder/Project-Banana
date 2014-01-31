package projectbanana.main.entity;

import java.awt.Graphics;

public interface VisibleObject {

	abstract public void tick();
	abstract public void render(Graphics g);
}
