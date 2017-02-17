package shapes;


import ships.Ship;

public class Shape {
	
	Ship ship;
	private long drawCounter;
	
	Shape(Ship ship) {
		this.ship = ship;
		if (ship != null)
			drawCounter = ship.drawCounter;
	}
	
	Surface[] getSurfaces(int xSide, int ySide, int zSide) {
		return new Surface[0];
	}
	
	// return: empty Surface[] -> nothing to draw. null -> ready to be removed from the world
	final public Surface[] draw(int xSide, int ySide, int zSide) {
		if (ship != null && drawCounter != ship.drawCounter)
			return null;
		return getSurfaces(xSide, ySide, zSide);
	}
}
