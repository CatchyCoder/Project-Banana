package projectbanana.main;

public class CollisionEvent {

	private boolean collision;
	private double xDis, yDis;
	
	public CollisionEvent(boolean collision, double xDis, double yDis) {
		this.collision = collision;
		this.xDis = xDis;
		this.yDis = yDis;
	}
	
	public boolean isColliding() {
		return collision;
	}
	
	public double getXDis() {
		return xDis;
	}
	
	public double getYDis() {
		return yDis;
	}
}
