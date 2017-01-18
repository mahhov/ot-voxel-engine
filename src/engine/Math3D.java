package engine;


public class Math3D {
	public static final double EPSILON = 0.00001;
	public static final int LEFT = 0, RIGHT = 1, FRONT = 2, BACK = 3, BOTTOM = 4, TOP = 5, NONE = -1;
	
	public static final double sqrt2 = Math.sqrt(2), sqrt2Div3 = Math.sqrt(2.0 / 3), sqrt1Div2 = 1 / sqrt2, sqrt1Div5 = 1 / Math.sqrt(5);
	
	public static double magnitude(double x, double y, double z) {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public static double magnitudeSqrd(double x, double y, double z) {
		return x * x + y * y + z * z;
	}
	
	public static double magnitude(double dx, double dy) {
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static double dotProduct(double x, double y, double z, double x2, double y2, double z2) {
		double mag1 = magnitude(x, y, z);
		double mag2 = magnitude(x2, y2, z2);
		return dotProductUnormalized(x, y, z, x2, y2, z2) / mag1 / mag2;
	}
	
	public static double dotProductUnormalized(double x, double y, double z, double x2, double y2, double z2) {
		return x * x2 + y * y2 + z * z2;
	}
	
	public static double[] crossProductNormalized(double x, double y, double z, double x2, double y2, double z2) {
		double[] cross = crossProductUnormalized(x, y, z, x2, y2, z2);
		double mag = magnitude(cross[0], cross[1], cross[2]);
		cross[0] /= mag;
		cross[1] /= mag;
		cross[2] /= mag;
		return cross;
	}
	
	public static double[] crossProductUnormalized(double x, double y, double z, double x2, double y2, double z2) {
		return new double[] {y * z2 - z * y2, z * x2 - x * z2, x * y2 - y * x2};
	}
	
	public static double[] halfAxisVectors(double[] norm) {
		return axisVectors(norm, .5);
	}
	
	public static double[] axisVectors(double[] norm, double mag) {
		if (norm[0] == 0 && norm[1] == 0)
			norm[0] = EPSILON;
		
		double rightMag = magnitude(norm[1], -norm[0], 0);
		double rightx = norm[1] / rightMag * mag;
		double righty = -norm[0] / rightMag * mag;
		
		double upx = -norm[2] * norm[0];
		double upy = -norm[2] * norm[1];
		double upz = (norm[0] * norm[0] + norm[1] * norm[1]);
		double upMag = magnitude(upx, upy, upz);
		
		return new double[] {rightx, righty, 0, upx / upMag * mag, upy / upMag * mag, upz / upMag * mag};
	}
	
	public static double[] upVector(double x, double y, double z) {
		double upx = -z * x;
		double upy = -z * y;
		double upz = (x * x + y * y);
		double mag = magnitude(upx, upy, upz);
		return new double[] {upx / mag, upy / mag, upz / mag};
	}
	
	public static double[] norm(Angle angle, Angle angleZ) {
		double[] normal = new double[3];
		normal[0] = angle.cos() * angleZ.cos();
		normal[1] = angle.sin() * angleZ.cos();
		normal[2] = angleZ.sin();
		return normal;
	}
	
	public static double[] norm(Angle angle, Angle angleZ, double size) {
		double[] normal = new double[3];
		normal[0] = angle.cos() * angleZ.cos() * size;
		normal[1] = angle.sin() * angleZ.cos() * size;
		normal[2] = angleZ.sin() * size;
		return normal;
	}
	
	// rotates point x,y,z around axis u,v,w by angle cos,sin and by angle cos,-sin
	public static double[] rotation(double[] xyz, double[] uvw, double cos, double sin) {
		double temp1 = uvw[0] * xyz[0] + uvw[1] * xyz[1] + uvw[2] * xyz[2];
		double oneCos = 1 - cos;
		
		double rotxBase = uvw[0] * temp1 * oneCos + xyz[0] * cos;
		double rotyBase = uvw[1] * temp1 * oneCos + xyz[1] * cos;
		double rotzBase = uvw[2] * temp1 * oneCos + xyz[2] * cos;
		
		double rotxDelta = (uvw[2] * xyz[1] + uvw[1] * xyz[2]) * sin;
		double rotyDelta = (uvw[2] * xyz[0] + uvw[0] * xyz[2]) * sin;
		double rotzDelta = (uvw[1] * xyz[0] + uvw[0] * xyz[1]) * sin;
		
		return new double[] {rotxBase + rotxDelta, rotyBase + rotyDelta, rotzBase + rotzDelta, rotxBase - rotxDelta, rotyBase - rotyDelta, rotzBase - rotzDelta};
	}
	
	public static double maxMin(double value, double max, double min) {
		if (value > max)
			return max;
		if (value < min)
			return min;
		return value;
	}
	
	public static int min(int val, int min) {
		if (val > min)
			return min;
		return val;
	}
	
	public static double min(double val, double min) {
		if (val > min)
			return min;
		return val;
	}
	
	public static int min(int val, int val2, int val3) {
		int min;
		if (val2 < val)
			min = val2;
		else
			min = val;
		if (min < val3)
			return min;
		return val3;
	}
	
	public static int max(int val, int max) {
		if (val < max)
			return max;
		return val;
	}
	
	public static double max(double val, double max) {
		if (val < max)
			return max;
		return val;
	}
	
	private static double sinTable[];
	private static int trigAccuracy;
	
	static void loadTrig(int accuracy) {
		trigAccuracy = accuracy;
		sinTable = new double[accuracy];
		for (int i = 0; i < accuracy; i++)
			sinTable[i] = Math.sin(Math.PI / 2 * i / accuracy);
	}
	
	public static double xsin(double xd) {
		double x = xd / Math.PI * 2;
		
		int sign;
		if (x < 0) {
			sign = -1;
			x = -x;
		} else
			sign = 1;
		
		x = x - ((int) (x / 4)) * 4;
		if (x >= 2) {
			sign *= -1;
			x -= 2;
		}
		
		if (x > 1)
			x = 2 - x;
		
		if (x == 1)
			return sign;
		
		return sign * sinTable[(int) (x * trigAccuracy)];
		
	}
	
	public static double xcos(double x) {
		return xsin(x + Math.PI / 2);
	}
	
	public static double sin2(double sin1, double cos1, double sin2, double cos2) {
		// sin(a+b)
		return sin1 * cos2 + cos1 * sin2;
	}
	
	public static double cos2(double sin1, double cos1, double sin2, double cos2) {
		// cos(a+b)
		return cos1 * cos2 - sin1 * sin2;
	}
	
	public static double tan2(double sin1, double cos1, double sin2, double cos2) {
		// tan(a+b)
		double tan1 = sin1 / cos1;
		double tan2 = sin2 / cos2;
		return (tan1 + tan2) / (1 - tan1 * tan2);
	}
	
	public static double[] transform(double[] a, int scale, int shift) {
		double[] aa = new double[a.length];
		for (int i = 0; i < a.length; i++)
			aa[i] = a[i] * scale + shift;
		return aa;
	}
	
	public static int[][] transform(double[][] a, int scale, int shift) {
		int[][] aa = new int[a.length][a[0].length];
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a[i].length; j++)
				aa[i][j] = (int) (a[i][j] * scale + shift);
		return aa;
		// int[][] aa = new int[a.length][];
		// for (int i = 0; i < a.length; i++)
		// aa[i] = transform(a[i], scale, shift);
		// return aa;
	}
	
	public static double pow(double base, int p) {
		double result = 1;
		while (p > 0) {
			if ((p & 1) != 0)
				result *= base;
			base *= base;
			p = p >>> 1;
		}
		return result;
	}
	
	public static class Angle {
		private double angle;
		private double angleCos, angleSin;
		private boolean updated;
		
		public Angle(double angle) {
			this.angle = angle;
		}
		
		private void update() {
			angleCos = xcos(angle);
			angleSin = xsin(angle);
			updated = true;
		}
		
		public double get() {
			return angle;
		}
		
		public void set(double angle) {
			this.angle = angle;
			updated = false;
		}
		
		public double sin() {
			if (!updated)
				update();
			return angleSin;
		}
		
		public double cos() {
			if (!updated)
				update();
			return angleCos;
		}
		
		public void bound() {
			if (angle < -Math.PI / 2)
				set(-Math.PI / 2);
			if (angle > Math.PI / 2)
				set(Math.PI / 2);
		}
	}
}
