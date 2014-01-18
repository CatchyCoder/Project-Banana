package projectbanana.main.entity;

import projectbanana.main.values.GeometryId;

public abstract class DrawnEntity extends Entity {
	
	public DrawnEntity(int x, int y, int width, int height, int ID){
		super(x, y, ID);
		this.width = boundingWidth = width;
		this.height = boundingHeight = height;
		if(ID == GeometryId.CIRCLE.getId()) {
			if(width == height) boundingRad = width / 2 ;
			else {
				try {
					throw new Exception("Entity declared as a circle - but width != height ???");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.x -= (width / 2);
		this.y -= (height / 2);
		this.startX = (int) this.x;
		this.startY = (int) this.y;
	}
	
	public DrawnEntity(int x, int y, int size, int ID) {
		this(x, y, size, size, ID);
		boundingRad = size / 2;
	}
}
