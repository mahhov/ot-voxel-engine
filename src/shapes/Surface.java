package shapes;

import engine.Camera;
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
	
	void setNormal(boolean flipNormal) {
		// uses first 3 coordinates
		// when flipNormal is false, normal points toward right hand rule
		normal = new double[3];
		
		// set coord 0 to origin
		double dx1 = coord[1][0] - coord[0][0];
		double dy1 = coord[1][1] - coord[0][1];
		double dz1 = coord[1][2] - coord[0][2];
		double dx2 = coord[2][0] - coord[0][0];
		double dy2 = coord[2][1] - coord[0][1];
		double dz2 = coord[2][2] - coord[0][2];
		
		// cross product
		normal[0] = dy1 * dz2 - dz1 * dy2;
		normal[1] = dz1 * dx2 - dx1 * dz2;
		normal[2] = dx1 * dy2 - dy1 * dx2;
		
		if (flipNormal) {
			normal = Math3D.transform(normal, -1, 0);
		}
	}
	
	void setColor(Color c) {
		color = c;
	}
	
	void setLight(double l) {
		light = l;
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
				double ox2 = ox * camera.angleSin - oy * camera.angleCos;
				oy = ox * camera.angleCos + oy * camera.angleSin;
				ox = ox2;
				
				// camera z angle
				double oz2 = -oz * camera.angleZCos + oy * camera.angleZSin;
				oy = oz * camera.angleZSin + oy * camera.angleZCos;
				oz = oz2;
				
				// projection
				x[i] = ox / oy;
				y[i] = oz / oy;
			}
			tempDistanceLight = light < 1 ? light : 1;
			tempDistanceLight *= Math3D.pow(Camera.FOG, (int) oy);
			return new double[][] {x, y};
		}
		return null;
	}
	
}