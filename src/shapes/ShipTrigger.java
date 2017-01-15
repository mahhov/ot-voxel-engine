package shapes;

import ships.Ship;

public class ShipTrigger extends Shape {
	Ship ship;
	private double x, y, z;
	private long drawCounter;
	
	public ShipTrigger(double x, double y, double z, Ship ship) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.ship = ship;
		drawCounter = ship.drawCounter;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (drawCounter == ship.drawCounter)
			ship.visible = true;
		return null;
	}
	
}
