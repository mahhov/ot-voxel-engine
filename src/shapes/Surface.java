package shapes;

import camera.Camera;
import engine.Math3D;

import java.awt.*;

public class Surface {
	double[][] coord;
	double[] normal;
	double light;
	public double tempDistanceLight;
	public Color color;
	
	Surface(double x, double[] y, double[] z, boolean flipNormal) {
		int n = Math3D.min(y.length, z.length);
		coord = new double[n][];
		for (int i = 0; i < n; i++)
			coord[i] = new double[] {x, y[i], z[i]};
		setNormal(flipNormal);
	}
	
	Surface(double[] x, double y, double[] z, boolean flipNormal) {
		int n = Math3D.min(x.length, z.length);
		coord = new double[n][];
		for (int i = 0; i < n; i++)
			coord[i] = new double[] {x[i], y, z[i]};
		setNormal(flipNormal);
	}
	
	Surface(double[] x, double[] y, double z, boolean flipNormal) {
		int n = Math3D.min(x.length, y.length);
		coord = new double[n][];
		for (int i = 0; i < n; i++)
			coord[i] = new double[] {x[i], y[i], z};
		setNormal(flipNormal);
	}
	
	Surface(double[] x, double[] y, double[] z, boolean flipNormal) {
		int n = Math3D.min(x.length, y.length, z.length);
		coord = new double[n][];
		for (int i = 0; i < n; i++)
			coord[i] = new double[] {x[i], y[i], z[i]};
		setNormal(flipNormal);
	}
	
	Surface(double[] p1, double[] p2, double[] p3, double[] p4, boolean flipNormal) {
		coord = new double[][] {p1, p2, p3, p4};
		setNormal(flipNormal);
	}
	
	void setNormal(boolean flipNormal) {
		// uses first 3 coordinates
		// when flipNormal is false, normal points toward right hand rule
		normal = new double[3];
		
		// set coord 0 to origin
		double[] d1 = new double[] {coord[1][0] - coord[0][0], coord[1][1] - coord[0][1], coord[1][2] - coord[0][2]};
		double[] d2 = new double[] {coord[2][0] - coord[0][0], coord[2][1] - coord[0][1], coord[2][2] - coord[0][2]};
		
		// cross product
		normal = Math3D.crossProductNormalized(d1, d2, flipNormal);
	}
	
	void setColor(Color c) {
		color = c;
	}
	
	void setLight(double l) {
		light = l * (Math3D.dotProductUnormalized(normal, Camera.LIGHT_SOURCE) + 1);
		light = Math3D.min(light, 1);
	}
	
	public double[][] toCamera(Camera camera) {
		// only the surfaces with normals pointing towards camera and within camera view
		if (camera.facingTowards(normal, coord[0]) && camera.inView(coord)) {
			double[] x = new double[coord.length], y = new double[coord.length];
			double ox, oy = 0, oz;
			for (int i = 0; i < coord.length; i++) {
				// set origin at camera
				ox = coord[i][0] - camera.x;
				oy = coord[i][1] - camera.y;
				oz = coord[i][2] - camera.z;
				
				// camera angle
				double ox2 = ox * camera.angle.sin() - oy * camera.angle.cos();
				oy = ox * camera.angle.cos() + oy * camera.angle.sin();
				ox = ox2;
				
				// camera z angle
				double oz2 = -oz * camera.angleZ.cos() + oy * camera.angleZ.sin();
				oy = oz * camera.angleZ.sin() + oy * camera.angleZ.cos();
				oz = oz2;
				
				// projection
				x[i] = ox / oy;
				y[i] = oz / oy;
			}
			tempDistanceLight = light * Math3D.pow(Camera.FOG, (int) oy);
			return new double[][] {x, y};
		}
		return null;
	}
	
}

// TODO: move camera.inview check to prior to cube/shape logic
// TODO: make sure facingTowards checks smaller than 180 degrees
// TODO : cache / reuse surface points