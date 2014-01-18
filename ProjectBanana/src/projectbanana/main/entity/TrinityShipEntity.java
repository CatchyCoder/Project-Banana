package projectbanana.main.entity;

public class TrinityShipEntity extends PlayerEntity {
	
	public TrinityShipEntity(int x, int y) {
		super(x, y, "TrinityShip.png", "TrinityShipThrust.png");
		boundingRad *= 0.4;
	}
}
