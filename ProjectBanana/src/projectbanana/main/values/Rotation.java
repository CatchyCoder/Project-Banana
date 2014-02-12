package projectbanana.main.values;

public enum Rotation {

	CLOCKWISE(1),
	COUNTER_CLOCKWISE(-1);
	
	private final int ID;
	
	private Rotation(int ID) {
		this.ID = ID;
	}
	
	public int getId() {
		return ID;
	}
}
