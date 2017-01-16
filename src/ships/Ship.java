package ships;

import engine.Math3D;
import shapes.ShipCube;
import shapes.ShipTrigger;
import world.World;

public class Ship {
	public boolean visible;
	public long drawCounter;
	private double x, y, z;
	private Math3D.Angle angle;
	
	public Ship(double x, double y, double z, double angle, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = new Math3D.Angle(angle);
		visible = false;
		addToWorld(world);
	}
	
	private void addToWorld(World world) {
		// triggers
		ShipTrigger trigger = new ShipTrigger(x, y, z, this);
		world.addShape((int) x, (int) y, (int) z, trigger);
		
		if (!visible)
			return;
		
		// body
		int scale = 5;
		ShipCube shape = new ShipCube(x, y, z, angle.get(), 0, scale, this, new boolean[] {true, true, true, false, true, true});
		world.addShape((int) x, (int) y, (int) z, shape);
		double x = this.x + angle.cos() * scale * 2;
		double y = this.y + angle.sin() * scale * 2;
		ShipCube forward = new ShipCube(x, y, z, angle.get(), 0, scale, this, new boolean[] {true, true, false, true, true, true});
		world.addShape((int) x, (int) y, (int) z, forward);
		visible = false;
	}
	
	public void update(World world) {
		move();
		addToWorld(world);
	}
	
	private void move() {
		drawCounter++;
		double speed = .1;
		x += angle.cos() * speed;
		y += angle.sin() * speed;
		angle.set(angle.get() + .005);
	}
	
}
