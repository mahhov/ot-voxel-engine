package ships;

import shapes.ShipCube;
import world.Cell;
import world.World;

public class Ship {
	private final static int DRAW_HIDDEN = 0, DRAW_VISIBLE = 1, DRAW_NEWLY_VISIBLE = 2;
	private int drawState;
	private long drawCounter;
	private double x, y, z;
	
	public Ship(double x, double y, double z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		drawState = DRAW_HIDDEN;
		addToWorld(world);
	}
	
	private void addToWorld(World world) {
		ShipCube shape = new ShipCube(x, y, z, 0, 0, 3, drawCounter);
		Cell.LinkedShapes lshape = world.addShape((int) x, (int) y, (int) z, shape);
		shape.setReferences(this, world, lshape);
	}
	
	public void move(double x, double y, double z) {
		drawCounter++;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
