package engine;

public class Camera {
	static final double MOVE_SPEED = 1.2, ANGLE_SPEED = .04, MOUSE_DAMP_SPEED = 0.1;
	public static final double FOG = 0.987;
	static final double MIN_LIGHT = .12;
	
	public double x, y, z;
	double angle, angleZ;
	public double angleSin, angleCos, angleZSin, angleZCos;
	private double viewCos, viewSin;
	double[] normal;
	int maxCull, maxCullSqrd;
	
	boolean dirtyAngles;
	
	Camera() {
		maxCull = (int) (Math.log(MIN_LIGHT) / Math.log(FOG)) + 1;
		System.out.println("maxCull set to " + maxCull);
		if (maxCull > 200)
			maxCull = 200;
		maxCullSqrd = maxCull * maxCull;
		
		viewCos = 2 * Math3D.sqrt1Div5;
		viewSin = 1 * Math3D.sqrt1Div5;
		
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
		normal[0] = angleCos * angleZCos;
		normal[1] = angleSin * angleZCos;
		normal[2] = angleZSin;
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
	
	public int[] cullBoundaries2() {
		int c = maxCull;
		int left = (int) x - c;
		int right = (int) x + c + 1;
		int front = (int) y - c;
		int back = (int) y + c + 1;
		int bottom = (int) z - c;
		int top = (int) z + c + 1;
		if (Math.random() > .9)
			System.out.println("voluem: " + ((right - left) * (back - front) * (top - bottom)) / 100000);
		return new int[] {left, right, front, back, bottom, top};
	}
	
	public int[] cullBoundaries() {
		double left, right, front, back, bottom, top;
		double forwardMult, backwardMult;
		
		// vertical view angle
		double topCos = angleZCos * viewCos - angleZSin * viewSin;
		double topSin = angleZCos * viewSin + angleZSin * viewCos;
		double bottomCos = angleZCos * viewCos + angleZSin * viewSin;
		double bottomSin = -angleZCos * viewSin + angleZSin * viewCos;
		
		// zangle
		if (topCos < 0) { // up - vertical axis visible
			forwardMult = bottomCos * maxCull;
			backwardMult = topCos * maxCull;
			top = z + maxCull;
			bottom = z;
		} else if (bottomCos < 0) { // down - vertical axis visible
			forwardMult = topCos * maxCull;
			backwardMult = bottomCos * maxCull;
			top = z;
			bottom = z - maxCull;
		} else if (bottomSin > 0) { // up - no axis visible
			forwardMult = bottomCos * maxCull;
			backwardMult = 0;
			top = z + topSin * maxCull;
			bottom = z;
		} else if (topSin < 0) { // down - no axis visible
			forwardMult = topCos * maxCull;
			backwardMult = 0;
			top = z;
			bottom = z + bottomSin * maxCull;
		} else { // straight - horizontal plane visible
			forwardMult = maxCull;
			backwardMult = 0;
			top = z + topSin * maxCull;
			bottom = z + bottomSin * maxCull;
		}
		
		// horizontal view angles
		double leftCos = angleCos * viewCos - angleSin * viewSin;
		double leftSin = angleCos * viewSin + angleSin * viewCos;
		double rightCos = angleCos * viewCos + angleSin * viewSin;
		double rightSin = -angleCos * viewSin + angleSin * viewCos;
		
		// angle
		if (rightCos > 0)
			if (leftCos < 0) { // top
				left = x + leftCos * forwardMult;
				right = x + rightCos * forwardMult;
				front = y + backwardMult; // backwardMult is always <= 0
				back = y + forwardMult;
			} else if (rightSin > 0) { // top right
				left = x + rightCos * backwardMult;
				right = x + rightCos * forwardMult;
				front = y + leftSin * backwardMult;
				back = y + leftSin * forwardMult;
			} else if (leftSin > 0) { // right
				left = x + backwardMult;
				right = x + forwardMult;
				front = y + rightSin * forwardMult;
				back = y + leftSin * forwardMult;
			} else { // bottom right
				left = x + leftCos * backwardMult;
				right = x + leftCos * forwardMult;
				front = y + rightSin * forwardMult;
				back = y + rightSin * backwardMult;
			}
		else if (leftCos > 0) { // bottom
			left = x + rightCos * forwardMult;
			right = x + leftCos * forwardMult;
			front = y - forwardMult;
			back = y - backwardMult;
		} else if (rightSin < 0) { // bottom left
			left = x + rightCos * forwardMult;
			right = x + rightCos * backwardMult;
			front = y + leftSin * forwardMult;
			back = y + leftSin * backwardMult;
		} else if (leftSin < 0) { // left
			left = x - forwardMult;
			right = x - backwardMult;
			front = y + leftSin * forwardMult;
			back = y + rightSin * forwardMult;
		} else { // top left
			left = x + leftCos * forwardMult;
			right = x + leftCos * backwardMult;
			front = y + rightSin * backwardMult;
			back = y + rightSin * forwardMult;
		}
		
		//		Painter.debugString = new String[4];
		//		Painter.debugString[0] = (left - x) / maxCull + " " + (right - x) / maxCull;
		//		Painter.debugString[1] = (front - y) / maxCull + " " + (back - y) / maxCull;
		//		Painter.debugString[2] = (forwardMult) / maxCull + " " + backwardMult / maxCull;
		//		Painter.debugString[3] = Math.round(leftCos * 100) / 100. + " " + Math.round(leftSin * 100) / 100. + " " + Math.round(rightCos * 100) / 100. + " " + Math.round(rightSin * 100) / 100.;
		
		// TODO : horizontal view rotation incorrect when zangle isn't 0
		
		return new int[] {(int) (left), (int) (right) + 1, (int) (front), (int) (back) + 1, (int) (bottom), (int) (top) + 1};
	}
}