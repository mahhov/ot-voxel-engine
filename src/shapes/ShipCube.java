package shapes;

import ships.Ship;
import world.Cell;
import world.World;

public class ShipCube extends Cube {
	Ship ship;
	World world;
	Cell.LinkedShapes lshapes;
	long drawCounter;
	
	public ShipCube(double x, double y, double z, double angle, double angleZ, double size, long drawCounter) {
		super(x, y, z, angle, angleZ, size);
		this.drawCounter = drawCounter;
	}
	
	public void setReferences(Ship ship, World world, Cell.LinkedShapes lshapes) {
		this.ship = ship;
		this.world = world;
		this.lshapes = lshapes;
	}
	
}
