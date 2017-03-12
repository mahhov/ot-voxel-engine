package projectiles;

import control.Controller;
import engine.Math3D;
import shapes.CubeFrame;
import shapes.ShapeParent;
import world.World;

public class Projectile implements ShapeParent {
	private final double FRICTION = .96, GRAVITY = -.003; // todo : join projectile and ship physics constants to math3d
	private final int MAX_DURATION = 1000;
	private long drawCounter;
	private double x, y, z;
	private Math3D.Angle angle, angleZ, angleTilt;
	private double[] norm, rightUp;
	private double vx, vy, vz;
	
	public Projectile(double x, double y, double z, double angle, double angleZ, double angleTilt, double vx, double vy, double vz) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = new Math3D.Angle(angle);
		this.angleZ = new Math3D.Angle(angleZ);
		this.angleTilt = new Math3D.Angle(angleTilt);
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		computeAxis();
		drawCounter = MAX_DURATION;
	}
	
	private void computeAxis() {
		norm = Math3D.norm(angle, angleZ, 1);
		rightUp = Math3D.axisVectorsTilt(norm, 1, angleZ, angleTilt);
	}
	
	void addToWorld(World world) {
		world.addShape((int) x, (int) y, (int) z, new CubeFrame(x, y, z, angle, angleZ, angleTilt, .1, this));
	}
	
	public void update(World world, Controller controller) {
		drawCounter++;
		move(world, controller);
		// todo: check collide
		addToWorld(world);
	}
	
	private void move(World world, Controller controller) {
		vx *= FRICTION;
		vy *= FRICTION;
		vz = (vz + GRAVITY) * FRICTION;
		
		x += vx;
		y += vy;
		z += vz;
		
		double[] xyz = Math3D.bound(x, y, z, world.width, world.length, world.height);
		x = xyz[0];
		y = xyz[1];
		z = xyz[2];
	}
	
	public long getDrawCounter() {
		return drawCounter;
	}
	
}
