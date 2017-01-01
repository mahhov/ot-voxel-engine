package engine;

import static engine.Math3D.sqrt1Div2;

public class Camera {
	static final double MOVE_SPEED = 1.2, ANGLE_SPEED = .04, MOUSE_DAMP_SPEED = 0.1;
	public static final double FOG = 0.987;
	static final double MIN_LIGHT = .12;
	
	public double x, y, z;
	double angle, angleZ;
	public double angleSin, angleCos, angleZSin, angleZCos;
	double angleView, angleViewCos, angleViewSin, angleViewTan;
	double[] normal;
	int maxCull;
	
	boolean dirtyAngles;
	
	Camera() {
		maxCull = (int) (Math.log(MIN_LIGHT) / Math.log(FOG)) + 1;
		System.out.println("maxCull set to " + maxCull);
		if (maxCull > 200)
			maxCull = 200;
		
		angleViewCos = 1;
		angleViewSin = 2;
		double viewMag = Math3D.magnitude(angleViewCos, angleViewSin);
		angleViewCos /= viewMag;
		angleViewSin /= viewMag;
		angleViewTan = angleViewSin / angleViewCos;
		angleView = Math.atan2(angleViewSin, angleViewCos);
		//		System.out.println(angleView);
		
		normal = new double[3];
		
		x = 5.5;
		y = 5.5;
		z = 55;
		
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
		normal[0] = angleCos * angleZCos;
		normal[1] = angleSin * angleZCos;
		normal[2] = angleZSin;
	}
	
	public boolean facingTowards(double[] normal, double[] position) {
		return (Math3D.dotProductUnormalized(x - position[0], y - position[1], z - position[2], normal[0], normal[1], normal[2])) > 0;
	}
	
	public boolean inView(double[][] coord) {
		for (double[] c : coord)
			if (Math3D.dotProduct(c[0] - x, c[1] - y, c[2] - z, normal[0], normal[1], normal[2]) > Math3D.sqrt2Div3)
				return true;
		return false;
	}
	
	private int cull() {
		// farthest reaching ray
		double angleZTopPlane = angleZ - angleView;
		double angleZTopPlaneSin = Math3D.xsin(angleZTopPlane);
		
		// if horizon in view (infinite vision)
		if (angleZTopPlaneSin <= 0)
			return maxCull;
		
		// how far away ray hits 0-z-plane
		double angleZTopPlaneCos = Math3D.xcos(angleZTopPlane);
		double distance = z * angleZTopPlaneCos / angleZTopPlaneSin;
		
		// expand to see corners
		//		distance *= Math3D.sqrt2;
		distance *= angleViewTan;
		
		// no need to see farther than fog's extent
		return Math3D.min((int) distance, maxCull);
	}
	
	public int[] cullBoundaries2() {
		double left;
		double right;
		double front;
		double back;
		double bottom;
		double top;
		
		// view angles
		double leftCos = (angleCos - angleSin) * sqrt1Div2;
		double leftSin = (angleCos + angleSin) * sqrt1Div2;
		double rightCos = (angleCos + angleSin) * sqrt1Div2;
		double rightSin = (angleCos - angleSin) * sqrt1Div2;
		
		// angle
		if (angleSin > angleCos) // 45 to 225
			if (angleSin > -angleCos) { // 45 to 135 TOP
				left = x + leftCos * maxCull;
				right = x + rightCos * maxCull;
				front = y;
				back = y + maxCull;
			} else {// 135 to 225 LEFT
				left = x - maxCull;
				right = x;
				front = y + leftSin * maxCull;
				back = y + rightSin * maxCull;
			}
		else if (angleSin > -angleCos) { // 45 to -45 RIGHT
			left = x;
			right = x + maxCull;
			front = y + rightSin * maxCull;
			back = y + leftSin * maxCull;
		} else { // 225 to -45 BOTTOM
			left = x + rightCos * maxCull;
			right = x + leftCos * maxCull;
			front = y - maxCull;
			back = y;
		}
		
		// zangle
		
		bottom = (int) z - maxCull;
		top = (int) z + maxCull + 1;
		return new int[] {(int) left, (int) right + 1, (int) front, (int) back + 1, (int) bottom, (int) top};
	}
	
	public int[] cullBoundaries() {
		int c = cull();
		int left = (int) x - c;
		int right = (int) x + c + 1;
		int front = (int) y - c;
		int back = (int) y + c + 1;
		int bottom = (int) z - c;
		int top = (int) z + c + 1;
		return new int[] {left, right, front, back, bottom, top};
		// TODO: use min/max of c.x, c - viewAngle.x, c + viewAngle.x and y and z
	}
	
	// TODO: dot product to check if given x and y within viewAngle range
	
	// TODO: get z range visible given x and y coordinate
	// TODO: keep camera coordinates bounded to world
	
	// todo: better boundaries, in view dot product, facing dot product
}
