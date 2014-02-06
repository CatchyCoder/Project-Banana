package projectbanana.main.entity;

import java.awt.Graphics;

import projectbanana.main.values.Geometry;

public class HomeBaseEntity extends BufferedEntity {
	
	public HomeBaseEntity(int x, int y) {
		super(x, y, "/homebase/homebase.jpg", Geometry.RECTANGLE, false);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		this.renderEntityImage(g);
	}
}
