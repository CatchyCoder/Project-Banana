package projectbanana.main.menu;

public class OnOffButton {

	private Button stateOne, stateTwo;
	
	public OnOffButton(Menu menu, int x, int y, String imageName1, String hoverImageName1, String imageName2, String hoverImageName2, boolean condition) {
		stateOne = new Button(menu, x, y, imageName1, hoverImageName1);
		stateTwo = new Button(menu, x, y, imageName2, hoverImageName2);
		
		stateOne.setVisible(false);
		stateTwo.setVisible(false);
		
		if(condition) stateOne.setVisible(true);
		else stateTwo.setVisible(true);
	}
	
	public void flipState() {
		stateOne.setVisible(!stateOne.isVisible());
		stateTwo.setVisible(!stateTwo.isVisible());
	}
}
