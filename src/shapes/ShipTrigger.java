package shapes;

import ships.Ship;

public class ShipTrigger extends Shape {
	
	public ShipTrigger(Ship ship) {
		super(ship);
	}
	
	Surface[] getSurfaces(int xSide, int ySide, int zSide) {
		ship.visible = true;
		return null;
	}
}
