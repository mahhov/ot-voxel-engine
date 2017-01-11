package engine;

public class Camera {
	static final double MOVE_SPEED = 1.2, ANGLE_SPEED = .04, MOUSE_DAMP_SPEED = 0.1;
	public static final double FOG = 0.987;
	static final double MIN_LIGHT = .12;
	
	public double x, y, z;
	private double angle, angleZ;
	public double angleSin, angleCos, angleZSin, angleZCos;
	private double[] normal;
	private int maxCull, maxCullSqrd;
	
	boolean dirtyAngles;
	
	Camera() {
		maxCull = (int) (Math.log(MIN_LIGHT) / Math.log(FOG)) + 1;
		System.out.println("maxCull set to " + maxCull);
		if (maxCull > 200)
			maxCull = 200;
		maxCullSqrd = maxCull * maxCull;
		
		normal = new double[3];
		
		x = 50.5;
		y = 50.5;
		z = 25;
		
		computeAngles();
	}
	
	void move(Controller c) {
		int dx = 0, dy = 0, dz = 0;
		if (c.isKeyDown(Controller.KEY_W))
			dy += 1;
		if (c.isKeyDown(Controller.KEY_S))
			dy -= 1;
		if (c.isKeyDown(Controller.KEY_A))
			dx -= 1;
		if (c.isKeyDown(Controller.KEY_D))
			dx += 1;
		if (c.isKeyDown(Controller.KEY_Z))
			dz -= 1;
		if (c.isKeyDown(Controller.KEY_X))
			dz += 1;
		if (dx != 0 || dy != 0 || dz != 0)
			moveRelative(dx, dy, dz);
		
		double dangle = 0, danglez = 0;
		if (c.isKeyDown(Controller.KEY_Q))
			dangle += 1;
		if (c.isKeyDown(Controller.KEY_E))
			dangle -= 1;
		if (c.isKeyDown(Controller.KEY_R))
			danglez += 1;
		if (c.isKeyDown(Controller.KEY_F))
			danglez -= 1;
		
		int[] mouse = c.getMouseMovement();
		dangle -= mouse[0] * MOUSE_DAMP_SPEED;
		danglez -= mouse[1] * MOUSE_DAMP_SPEED;
		
		if (dangle != 0 || danglez != 0)
			rotate(dangle, danglez);
	}
	
	private void moveRelative(double dx, double dy, double dz) {
		x += dy * angleCos * MOVE_SPEED + dx * angleSin * MOVE_SPEED;
		y += dy * angleSin * MOVE_SPEED - dx * angleCos * MOVE_SPEED;
		z += dz * MOVE_SPEED;
	}
	
	private void moveTo(double toX, double toY, double toZ) {
		x = (x + toX) / 2;
		y = (y + toY) / 2;
		z = (z + toZ) / 2;
		
		double dy = -toY + y;
		double dx = toX - x;
		double dz = -toZ + z;
		angle = Math.atan2(dy, dx);
		angleZ = Math.atan2(dz, Math3D.magnitude(dx, dy));
		dirtyAngles = true;
	}
	
	private void rotate(double dangle, double dangleZ) {
		angle += dangle * ANGLE_SPEED;
		angleZ += dangleZ * ANGLE_SPEED;
		dirtyAngles = true;
	}
	
	void update(int width, int length, int height) {
		boundCoordinates(width, length, height);
		if (dirtyAngles) {
			dirtyAngles = false;
			computeAngles();
		}
	}
	
	private void boundCoordinates(int width, int length, int height) {
		if (x < 0)
			x = 0;
		if (x > width - Math3D.EPSILON)
			x = width - Math3D.EPSILON;
		if (y < 0)
			y = 0;
		if (y > length - Math3D.EPSILON)
			y = length - Math3D.EPSILON;
		if (z < 0)
			z = 0;
		if (z > height - Math3D.EPSILON)
			z = height - Math3D.EPSILON;
		
		if (angleZ < -Math.PI / 2)
			angleZ = -Math.PI / 2;
		if (angleZ > Math.PI / 2)
			angleZ = Math.PI / 2;
	}
	
	private void computeAngles() {
		// sin/cos
		angleSin = Math3D.xsin(angle);
		angleCos = Math3D.xcos(angle);
		angleZSin = Math3D.xsin(angleZ);
		angleZCos = Math3D.xcos(angleZ);
		
		// norm
		normal = Math3D.norm(angleSin, angleCos, angleZSin, angleZCos);
	}
	
	public boolean facingTowards(double[] normal, double[] position) {
		return (Math3D.dotProductUnormalized(x - position[0], y - position[1], z - position[2], normal[0], normal[1], normal[2])) > 0;
	}
	
	public boolean inView(double[][] coord) {
		double dx, dy, dz;
		for (double[] c : coord) {
			dx = c[0] - x;
			dy = c[1] - y;
			dz = c[2] - z;
			if (Math3D.magnitudeSqrd(dx, dy, dz) < maxCullSqrd && Math3D.dotProduct(dx, dy, dz, normal[0], normal[1], normal[2]) > Math3D.sqrt2Div3)
				return true;
		}
		return false;
	}
	
	public int[] cullBoundaries() {
		// right & up axis vectors
		double[] axis = Math3D.halfAxisVectors(normal);
		
		// top & bottom vectors
		double[] topVector = new double[] {normal[0] + axis[3], normal[1] + axis[4], normal[2] + axis[5]};
		double[] bottomVector = new double[] {normal[0] - axis[3], normal[1] - axis[4], normal[2] - axis[5]};
		
		// corner points
		double[][] points = new double[4][3]; // top right, top left, bottom right, bottom left;
		points[0] = new double[] {topVector[0] + axis[0], topVector[1] + axis[1], topVector[2] + axis[2]};
		points[1] = new double[] {topVector[0] - axis[0], topVector[1] - axis[1], topVector[2] - axis[2]};
		points[2] = new double[] {bottomVector[0] + axis[0], bottomVector[1] + axis[1], bottomVector[2] + axis[2]};
		points[3] = new double[] {bottomVector[0] - axis[0], bottomVector[1] - axis[1], bottomVector[2] - axis[2]};
		
		// find min & max
		double left = 0, right = 0, front = 0, back = 0, bottom = 0, top = 0;
		for (double[] p : points) {
			if (p[0] < left)
				left = p[0];
			else if (p[0] > right)
				right = p[0];
			if (p[1] < front)
				front = p[1];
			else if (p[1] > back)
				back = p[1];
			if (p[2] < bottom)
				bottom = p[2];
			else if (p[2] > top)
				top = p[2];
		}
		
		return new int[] {(int) (left * maxCull + x), (int) (right * maxCull + x), (int) (front * maxCull + y), (int) (back * maxCull + y), (int) (bottom * maxCull + z), (int) (top * maxCull + z)};
	}
}