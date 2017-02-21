package shapes;


import camera.Camera;
import ships.Ship;

public class Shape {
	
	Ship ship;
	private long drawCounter; // todo : drawCounter compare to constant in world rather than per ship
	
	Shape(Ship ship) {
		this.ship = ship;
		if (ship != null)
			drawCounter = ship.drawCounter;
	}
	
	Surface[] getSurfaces(int xSide, int ySide, int zSide, Camera camera) {
		return new Surface[0];
	}
	
	// return: empty Surface[] -> nothing to draw. null -> ready to be removed from the world
	final public Surface[] draw(int xSide, int ySide, int zSide, Camera camera) {
		if (ship != null && drawCounter != ship.drawCounter)
			return null;
		return getSurfaces(xSide, ySide, zSide, camera);
	}
}
