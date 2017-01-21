package shapes;

import engine.Math3D;
import ships.Ship;

public class ShipCube extends Cube {
	Ship ship;
	private long drawCounter;
	
	public ShipCube(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, Ship ship, boolean[] sides) {
		super(x, y, z, angle, angleZ, angleTilt, size, sides);
		this.ship = ship;
		drawCounter = ship.drawCounter;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (drawCounter == ship.drawCounter)
			return super.draw(xSide, ySide, zSide);
		return null;
	}
	
}
