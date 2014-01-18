package projectbanana.main.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.VolatileImage;

import projectbanana.main.Game;
import projectbanana.main.World;
import projectbanana.main.entity.DrawnEntity;
import projectbanana.main.values.GeometryId;

public class HUD extends DrawnEntity {
	
	private final VolatileImage HUD;
	
	private Font font = new Font(Game.FONT_STYLE, Font.BOLD, 30);
	
	public HUD() {
		super(0, 0, Game.SIZE.width, Game.SIZE.height, GeometryId.RECTANGLE.getId());
		HUD = Game.window.createVolatileImage((int) width, (int) height);
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void render(Graphics g) {
		g = HUD.getGraphics();
		try {
			g.setColor(new Color((int) (255 / vel), (int) (vel * 10), 0));
			//g.setColor(new Color(0, 255, 0));
		}
		catch(Exception e) {}
		//g.fillRect(0, Game.SIZE.height, 20, (int) -vel * 20);
		g.fillRect(0, 0, 100, 100);
		
		int x = (int) (World.player.getCenterX() - (Game.SIZE.getSize().width / 2));
		int y = (int) (World.player.getCenterY() - (Game.SIZE.getSize().height / 2));
		g = Game.image.getGraphics();
		g.drawImage(HUD, x, y, null);
		//g = Game.backgroundImage.getGraphics();
		//g.drawImage(HUD, (int) this.x, (int) this.y, null);
	}
	
	public Font getFont() {
		return font;
	}
}
