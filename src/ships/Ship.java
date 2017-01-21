package ships;

import engine.Controller;
import engine.Math3D;
import parts.Hull;
import parts.Part;
import shapes.ShipCube;
import shapes.ShipTrigger;
import world.World;

public class Ship {
	public boolean visible;
	public long drawCounter;
	public double x, y, z;
	public Math3D.Angle angle, angleZ, angleTilt;
	private double[] norm, rightUp;
	private double vx, vy, vz, vAngle, vAngleZ, vAngleTilt;
	
	private double mass, massX, massY, massZ;
	private double offsetX, offsetY, offsetZ;
	private Part part[][][];
	private boolean massDirty;
	
	public Ship(double x, double y, double z, double angle, double angleZ, double angleTilt, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = new Math3D.Angle(angle);
		this.angleZ = new Math3D.Angle(angleZ);
		this.angleTilt = new Math3D.Angle(angleTilt);
		computeAxis();
		visible = false;
		generateParts();
		addToWorld(world);
	}
	
	private void generateParts() {
		massDirty = true;
		
		// offsets
		offsetX = .5;
		offsetY = 2.5;
		offsetZ = 0;
		
		// parts
		part = new Part[2][5][1];
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					part[x][y][z] = new Hull();
	}
	
	private void computeMass() {
		mass = 0;
		massX = 0;
		massY = 0;
		massZ = 0;
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++) {
					mass += part[x][y][z].mass;
					massX += x * mass;
					massY += y * mass;
					massZ += z * mass;
				}
		massX /= mass;
		massY /= mass;
		massZ /= mass;
	}
	
	private void computeAxis() {
		norm = Math3D.norm(angle, angleZ, 1);
		rightUp = Math3D.axisVectorsTilt(norm, 1, angleTilt);
	}
	
	private void addToWorld(World world) {
		// triggers
		ShipTrigger trigger = new ShipTrigger(this);
		world.addShape((int) x, (int) y, (int) z, trigger);
		
		if (!visible)
			return;
		
		// body
		ShipCube shape;
		double xc, yc, zc;
		double dx, dy, dz;
		for (int xi = 0; xi < part.length; xi++)
			for (int yi = 0; yi < part[xi].length; yi++)
				for (int zi = 0; zi < part[xi][yi].length; zi++) {
					dx = xi - offsetX;
					dy = yi - offsetY;
					dz = zi - offsetZ;
					
					xc = x + dx * rightUp[0] + dy * norm[0] + dz * rightUp[3];
					yc = y + dx * rightUp[1] + dy * norm[1] + dz * rightUp[4];
					zc = z + dx * rightUp[2] + dy * norm[2] + dz * rightUp[5];
					
					// LEFT 0, RIGHT 1, FRONT 2, BACK 3, BOTTOM 4, TOP 5, NONE -1
					shape = new ShipCube(xc, yc, zc, angle, angleZ, angleTilt, .5, this, new boolean[] {xi == 0, xi == part.length - 1, yi == 0, yi == part[xi].length - 1, zi == 0, zi == part[xi][yi].length - 1});
					world.addShape((int) xc, (int) yc, (int) zc, shape);
				}
		
		visible = false;
	}
	
	public void update(World world, Controller controller) {
		if (massDirty) {
			computeMass();
			massDirty = false;
		}
		move(world, controller);
		addToWorld(world); // todo : only if moved
	}
	
	private void move(World world, Controller controller) {
		drawCounter++; // todo : only if moved
		
		final double friction = 9.5 / mass, force = .1 / 2, angleForce = .01 / 2, gravity = -.01 * 0; // todo : class vars
		
		if (controller.isKeyDown(Controller.KEY_W)) {
			vx += norm[0] * force;
			vy += norm[1] * force;
			vz += norm[2] * force;
		}
		if (controller.isKeyDown(Controller.KEY_S)) {
			vx -= norm[0] * force;
			vy -= norm[1] * force;
			vz -= norm[2] * force;
		}
		if (controller.isKeyDown(Controller.KEY_A))
			vAngleTilt += angleForce;
		if (controller.isKeyDown(Controller.KEY_D))
			vAngleTilt -= angleForce;
		if (controller.isKeyDown(Controller.KEY_SPACE)) {
			vAngle += angleTilt.sin() * angleForce;
			vAngleZ += angleTilt.cos() * angleForce;
			vAngleTilt += angleZ.sin() * angleTilt.sin() * angleForce;
		}
		if (controller.isKeyDown(Controller.KEY_SHIFT)) {
			vAngle -= angleTilt.sin() * angleForce;
			vAngleZ -= angleTilt.cos() * angleForce;
			vAngleTilt -= angleZ.sin() * angleTilt.sin() * angleForce;
		}
		
		vx *= friction;
		vy *= friction;
		vz = (vz + gravity) * friction;
		vAngle *= friction;
		vAngleZ *= friction;
		vAngleTilt *= friction;
		
		x += vx;
		y += vy;
		z += vz;
		
		angle.set(angle.get() + vAngle);
		angleZ.set(angleZ.get() + vAngleZ);
		angleTilt.set(angleTilt.get() + vAngleTilt);
		
		double[] xyz = Math3D.bound(x, y, z, world.width, world.length, world.height); // todo : safe zone bound all sides
		x = xyz[0];
		y = xyz[1];
		z = xyz[2];
		
		computeAxis();
	}
	
}

// todo : force