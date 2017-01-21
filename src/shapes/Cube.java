package shapes;

import engine.Math3D;

import java.awt.*;

import static engine.Math3D.axisVectorsTilt;

public class Cube extends Shape {
	private double x, y, z;
	private Math3D.Angle angle, angleZ, angleTilt;
	private double size;
	private boolean surfacesDirty;
	private Surface top, bottom, left, right, front, back;
	private boolean[] sides;
	
	public Cube(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size) {
		this(x, y, z, angle, angleZ, angleTilt, size, new boolean[] {true, true, true, true, true, true});
	}
	
	public Cube(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, boolean[] sides) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.angleZ = angleZ;
		this.angleTilt = angleTilt;
		this.size = size;
		surfacesDirty = true;
		this.sides = sides;
	}
	
	private void initSurfaces() {
		// axis  vectors
		double[] norm = Math3D.norm(angle, angleZ, size);
		double[] rightUp = axisVectorsTilt(norm, size, angleTilt);
		
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
		if (sides[Math3D.TOP]) {
			top = new Surface(leftBackTop, rightBackTop, rightFrontTop, leftFrontTop, true);
			top.setColor(Color.red);
			top.setLight(1);
		}
		
		// from back/left -> back/right -> front/right -> front/left
		if (sides[Math3D.BOTTOM]) {
			bottom = new Surface(leftBackBottom, rightBackBottom, rightFrontBottom, leftFrontBottom, false);
			bottom.setColor(Color.green);
			bottom.setLight(1);
		}
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		if (sides[Math3D.LEFT]) {
			left = new Surface(leftBackBottom, leftBackTop, leftFrontTop, leftFrontBottom, true);
			left.setColor(Color.cyan);
			left.setLight(1);
		}
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		if (sides[Math3D.RIGHT]) {
			right = new Surface(rightBackBottom, rightBackTop, rightFrontTop, rightFrontBottom, false);
			right.setColor(Color.gray);
			right.setLight(1);
		}
		
		// from left/bottom -> left/top -> right/top -> right/bottom
		if (sides[Math3D.FRONT]) {
			front = new Surface(leftFrontBottom, leftFrontTop, rightFrontTop, rightFrontBottom, true);
			front.setColor(Color.blue);
			front.setLight(1);
		}
		
		// from left/bottom -> left/top -> right/top -> right/bottom
		if (sides[Math3D.BACK]) {
			back = new Surface(leftBackBottom, leftBackTop, rightBackTop, rightBackBottom, false);
			back.setColor(Color.yellow);
			back.setLight(1);
		}
		
		surfacesDirty = false;
	}
	
	public Surface[] draw(int xSide, int ySide, int zSide) {
		if (surfacesDirty)
			initSurfaces();
		return new Surface[] {top, bottom, left, right, front, back};
	}
	
}
