package shapes;

import ships.Ship;

public class ShipTrigger extends Shape {
	Ship ship;
	private long drawCounter;
	
	public ShipTrigger(Ship ship) {
		this.ship = ship;
		drawCounter = ship.drawCounter;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (drawCounter == ship.drawCounter)
			ship.visible = true;
		return null;
	}
	
}
