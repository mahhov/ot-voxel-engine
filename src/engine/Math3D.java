package engine;


public class Math3D {
	public static final double EPSILON = 0.00001;
	public static final int RIGHT = 1, BACK = 1, TOP = 1, LEFT = 0, FRONT = 0, BOTTOM = 0, NONE = -1;
	
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
	
	public static int min(int val, int min, int val3) {
		if (val > min)
			return min;
		return val;
	}
	
	public static int max(int val, int max) {
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
			return sign * 1;
		
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
}
