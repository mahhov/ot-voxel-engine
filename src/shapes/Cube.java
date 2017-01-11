package shapes;

import engine.Math3D;

import java.awt.*;

import static engine.Math3D.axisVectors;

public class Cube extends Shape {
	private double x, y, z;
	private double angle, angleZ;
	private double[] norm;
	private double size;
	private boolean surfacesDirty;
	private Surface top, bottom, left, right, front, back;
	
	public Cube(double x, double y, double z, double angle, double angleZ, double size) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.angleZ = angleZ;
		this.size = size;
		surfacesDirty = true;
	}
	
	private void initSurfaces() {
		// angles
		double angleSin = Math3D.xsin(angle);
		double angleCos = Math3D.xcos(angle);
		double angleZSin = Math3D.xsin(angleZ);
		double angleZCos = Math3D.xcos(angleZ);
		
		// axis  vectors
		norm = Math3D.norm(angleSin, angleCos, angleZSin * size, angleZCos * size);
		double[] rightUp = axisVectors(norm, size);
		
		// corner coordinates
		double[] leftFrontBottom = new double[] {x - norm[0] - rightUp[0] - rightUp[3], y - norm[1] - rightUp[1] - rightUp[4], z - norm[2] - rightUp[2] - rightUp[5]};
		double[] rightFrontBottom = new double[] {x - norm[0] + rightUp[0] - rightUp[3], y - norm[1] + rightUp[1] - rightUp[4], z - norm[2] + rightUp[2] - rightUp[5]};
		double[] leftBackBottom = new double[] {x + norm[0] - rightUp[0] - rightUp[3], y + norm[1] - rightUp[1] - rightUp[4], z + norm[2] - rightUp[2] - rightUp[5]};
		double[] rightBackBottom = new double[] {x + norm[0] + rightUp[0] - rightUp[3], y + norm[1] + rightUp[1] - rightUp[4], z + norm[2] + rightUp[2] - rightUp[5]};
		double[] leftFrontTop = new double[] {x - norm[0] - rightUp[0] + rightUp[3], y - norm[1] - rightUp[1] + rightUp[4], z - norm[2] - rightUp[2] + rightUp[5]};
		double[] rightFrontTop = new double[] {x - norm[0] + rightUp[0] + rightUp[3], y - norm[1] + rightUp[1] + rightUp[4], z - norm[2] + rightUp[2] + rightUp[5]};
		double[] leftBackTop = new double[] {x + norm[0] - rightUp[0] + rightUp[3], y + norm[1] - rightUp[1] + rightUp[4], z + norm[2] - rightUp[2] + rightUp[5]};
		double[] rightBackTop = new double[] {x + norm[0] + rightUp[0] + rightUp[3], y + norm[1] + rightUp[1] + rightUp[4], z + norm[2] + rightUp[2] + rightUp[5]};
		
		// from back/left -> back/right -> front/right -> front/left
		top = new Surface(leftBackTop, rightBackTop, rightFrontTop, leftFrontTop, false);
		bottom = new Surface(leftBackBottom, rightBackBottom, rightFrontBottom, leftFrontBottom, true);
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		left = new Surface(leftBackBottom, leftBackTop, leftFrontTop, leftFrontBottom, false);
		right = new Surface(rightBackBottom, rightBackTop, rightFrontTop, rightFrontBottom, true);
		
		// from left/bottom -> left/top -> right/top -> right/bottom
		front = new Surface(leftFrontBottom, leftFrontTop, rightFrontTop, rightFrontBottom, false);
		back = new Surface(leftBackBottom, leftBackTop, rightBackTop, rightBackBottom, true);
		
		// set color
		top.setColor(Color.red);
		bottom.setColor(Color.green);
		back.setColor(Color.yellow);
		front.setColor(Color.blue);
		right.setColor(Color.gray);
		left.setColor(Color.cyan);
		
		// set light
		top.setLight(1);
		bottom.setLight(1);
		back.setLight(1);
		front.setLight(1);
		right.setLight(1);
		left.setLight(1);
		
		surfacesDirty = false;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (surfacesDirty)
			initSurfaces();
		return new Surface[] {top, bottom, left, right, front, back};
	}
}
