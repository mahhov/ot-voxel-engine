package shapes;

import ships.Ship;

public class ShipCube extends Cube {
	Ship ship;
	private long drawCounter;
	
	public ShipCube(double x, double y, double z, double angle, double angleZ, double size, Ship ship, boolean[] sides) {
		super(x, y, z, angle, angleZ, size, sides);
		this.ship = ship;
		drawCounter = ship.drawCounter;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (drawCounter == ship.drawCounter)
			return super.draw(xSide, ySide, zSide);
		return null;
	}
	
}
