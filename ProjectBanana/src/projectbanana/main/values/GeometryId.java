package projectbanana.main.values;

public enum GeometryId {

	RECTANGLE(0),
	CIRCLE(1);
	
	private final int ID;
	
	private GeometryId(int ID) {
		this.ID = ID;
	}
	
	public int getId() {
		return ID;
	}
}
