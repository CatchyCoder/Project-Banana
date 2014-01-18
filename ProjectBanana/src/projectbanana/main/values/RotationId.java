package projectbanana.main.values;

public enum RotationId {

	CLOCKWISE(1),
	COUNTER_CLOCKWISE(-1);
	
	private final int ID;
	
	private RotationId(int ID) {
		this.ID = ID;
	}
	
	public int getId() {
		return ID;
	}
}
