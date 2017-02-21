package shapes;

import camera.Camera;
import ships.Ship;

public class ShipTrigger extends Shape {
	
	public ShipTrigger(Ship ship) {
		super(ship);
	}
	
	Surface[] getSurfaces(int xSide, int ySide, int zSide, Camera camera) {
		return ship.getSurfaces(xSide, ySide, zSide, camera);
	}
}
