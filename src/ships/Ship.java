package ships;

import engine.Controller;
import engine.Math3D;
import engine.Painter;
import module.ForwBlade;
import module.Hull;
import module.Module;
import module.Rotor;
import shapes.Shape;
import shapes.ShipTrigger;
import world.World;

public class Ship {
	private final double FRICTION = .96, FORCE = 10, ANGLE_FORCE = .75, GRAVITY = -.003;
	
	public boolean visible;
	public long drawCounter;
	public double x, y, z; // mass center
	public Math3D.Angle angle, angleZ, angleTilt;
	public double[] norm, rightUp;
	private double vx, vy, vz, vAngleFlat, vAngleUp, vAngleTilt;
	
	private double mass, massX, massY, massZ; // in relative axis system, offset from corner to mass center
	private double inertiaFlat, inertiaUp, inertiaTilt;
	private Module part[][][];
	
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
		// 5 hull
		// 4 blade
		// 3 hull
		// 2 rotor - back
		// 1 hull
		// 0 rotor - front
		
		part = new Module[2][6][1];
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					if (y == 0 || y == 2)
						part[x][y][z] = new Rotor();
					else if (y == 4)
						part[x][y][z] = new ForwBlade(this);
					else
						part[x][y][z] = new Hull();
		
		computeMass();
		computeInertia();
		
		int x = 0, y = 4, z = 0;
		part[x][y][z].set(Math3D.RIGHT, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.LEFT, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 3;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 2;
		part[x][y][z].set(Math3D.BACK, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BACK, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 0;
		part[x][y][z].set(Math3D.FRONT, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.FRONT, new double[] {x - massX, y - massY, z - massZ});
		
		x = 0;
		y = 5;
		part[x][y][z].set(Math3D.NONE, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.NONE, new double[] {x - massX, y - massY, z - massZ});
	}
	
	private void computeMass() {
		double tmass;
		mass = 0;
		massX = 0;
		massY = 0;
		massZ = 0;
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++) {
					tmass = part[x][y][z].mass;
					mass += tmass;
					massX += x * tmass;
					massY += y * tmass;
					massZ += z * tmass;
				}
		massX /= mass;
		massY /= mass;
		massZ /= mass;
	}
	
	private void computeInertia() {
		inertiaFlat = 0; // todo : fix these initial value
		inertiaUp = 0;
		inertiaTilt = 0;
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++) {
					inertiaFlat += Math3D.magnitudeSqrd(x - massX, y - massY) * part[x][y][z].mass;
					inertiaUp += Math3D.magnitudeSqrd(y - massY, z - massZ) * part[x][y][z].mass;
					inertiaTilt += Math3D.magnitudeSqrd(x - massX, z - massZ) * part[x][y][z].mass;
				}
	}
	
	private void computeAxis() {
		norm = Math3D.norm(angle, angleZ, 1);
		rightUp = Math3D.axisVectorsTilt(norm, 1, angleZ, angleTilt);
		Painter.debugString[2] = "(norm) " + Math3D.doubles2Str(norm, 0) + "(right) " + Math3D.doubles2Str(rightUp, 0) + "(up) " + Math3D.doubles2Str(rightUp, 3);
	}
	
	public double getVForward() {
		return Math3D.dotProductUnormalized(norm, new double[] {vx, vy, vz});
	}
	
	private void addToWorld(World world) {
		// triggers
		ShipTrigger trigger = new ShipTrigger(this);
		world.addShape((int) x, (int) y, (int) z, trigger);
		
		if (!visible)
			return;
		
		// body
		Shape shape;
		double xc, yc, zc;
		double dx, dy, dz;
		for (int xi = 0; xi < part.length; xi++)
			for (int yi = 0; yi < part[xi].length; yi++)
				for (int zi = 0; zi < part[xi][yi].length; zi++) {
					dx = xi - massX;
					dy = yi - massY;
					dz = zi - massZ;
					
					xc = x + dx * rightUp[0] + dy * norm[0] + dz * rightUp[3];
					yc = y + dx * rightUp[1] + dy * norm[1] + dz * rightUp[4];
					zc = z + dx * rightUp[2] + dy * norm[2] + dz * rightUp[5];
					
					boolean[] side = new boolean[] {xi == 0, xi == part.length - 1, yi == 0, yi == part[xi].length - 1, zi == 0, zi == part[xi][yi].length - 1};
					boolean[] innerSide = null;
					if (yi == 4) { // forw blades
						innerSide = new boolean[] {side[0], side[1], side[2], side[3], side[4], side[5]};
						side[Math3D.LEFT] = true;
						side[Math3D.RIGHT] = true;
					}
					shape = part[xi][yi][zi].getShape(xc, yc, zc, angle, angleZ, angleTilt, rightUp, side, innerSide, this);
					world.addShape((int) xc, (int) yc, (int) zc, shape);
				}
		
		visible = false;
	}
	
	public void update(World world, Controller controller) {
		move(world, controller);
		addToWorld(world); // todo : only if moved
	}
	
	private void doControl(Controller controller) {
		// controls
		boolean[] control = new boolean[6];
		if (controller.isKeyDown(Controller.KEY_W))
			control[Math3D.FRONT] = true;
		if (controller.isKeyDown(Controller.KEY_S))
			control[Math3D.BACK] = true;
		if (controller.isKeyDown(Controller.KEY_A))
			control[Math3D.LEFT] = true;
		if (controller.isKeyDown(Controller.KEY_D))
			control[Math3D.RIGHT] = true;
		if (controller.isKeyDown(Controller.KEY_SPACE))
			control[Math3D.TOP] = true;
		if (controller.isKeyDown(Controller.KEY_SHIFT))
			control[Math3D.BOTTOM] = true;
		
		// force
		Math3D.Force force = new Math3D.Force();
		for (Module[][] ppp : part)
			for (Module[] pp : ppp)
				for (Module p : pp)
					p.addForce(force, control);
		
		// apply
		applyForce(force);
	}
	
	void doControlAuto(Controller controller) {
		if (controller.isKeyDown(Controller.KEY_W)) {
			vx += norm[0] * FORCE;
			vy += norm[1] * FORCE;
			vz += norm[2] * FORCE;
		}
		if (controller.isKeyDown(Controller.KEY_S)) {
			vx -= norm[0] * FORCE;
			vy -= norm[1] * FORCE;
			vz -= norm[2] * FORCE;
		}
		if (controller.isKeyDown(Controller.KEY_A))
			vAngleTilt += ANGLE_FORCE;
		if (controller.isKeyDown(Controller.KEY_D))
			vAngleTilt -= ANGLE_FORCE;
		if (controller.isKeyDown(Controller.KEY_SPACE))
			vAngleUp += ANGLE_FORCE;
		if (controller.isKeyDown(Controller.KEY_SHIFT))
			vAngleUp -= ANGLE_FORCE;
		if (controller.isKeyDown(Controller.KEY_Q))
			vAngleFlat += ANGLE_FORCE;
		if (controller.isKeyDown(Controller.KEY_E))
			vAngleFlat -= ANGLE_FORCE;
	}
	
	private void move(World world, Controller controller) {
		drawCounter++; // todo : only if moved
		doControl(controller);
		
		vx *= FRICTION;
		vy *= FRICTION;
		vz = (vz + GRAVITY) * FRICTION;
		vAngleFlat *= FRICTION;
		vAngleUp *= FRICTION;
		vAngleTilt *= FRICTION;
		
		x += vx;
		y += vy;
		z += vz;
		
		// vAngleFlat & vAngleUp
		Math3D.Angle.rotate(angle, angleZ, angleTilt, rightUp, vAngleFlat, vAngleUp);
		// vAngleTilt
		angleTilt.set(angleTilt.get() + vAngleTilt);
		
		computeAxis();
		
		int safeZone = 6;
		double[] xyz = Math3D.bound(x - safeZone, y - safeZone, z - safeZone, world.width - safeZone * 2, world.length - safeZone * 2, world.height - safeZone * 2); // todo : *better* safe zone bound all sides
		x = xyz[0] + safeZone;
		y = xyz[1] + safeZone;
		z = xyz[2] + safeZone;
	}
	
	private void applyForce(Math3D.Force f) {
		f.prepare(norm, rightUp);
		double force = FORCE / mass;
		vx += f.x * force;
		vy += f.y * force;
		vz += f.z * force;
		vAngleFlat += f.angleFlat * FORCE / inertiaFlat;
		vAngleUp += f.angleUp * FORCE / inertiaUp;
		vAngleTilt += f.angleTilt * FORCE / inertiaTilt;
	}
	
}

// todo : angleZ invisible bug
// todo : smaller than cube drawing
