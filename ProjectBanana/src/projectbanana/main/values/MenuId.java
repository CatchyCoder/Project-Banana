package projectbanana.main.values;

public enum MenuId {

	MAIN_MENU(0),
	PAUSE_MENU(1),
	OPTIONS_MENU(2);
	
	private final int ID;
	
	private MenuId(int ID) {
		this.ID = ID;
	}
	
	public int getId() {
		return ID;
	}
}
