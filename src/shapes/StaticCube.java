package shapes;

import java.awt.*;

public class StaticCube extends Shape {
	double x, y, z;
	Surface top, bottom, left, right, front, back;
	
	public StaticCube(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		initSurfaces();
	}
	
	// TODO: only send sides needed according to parameters
	public Surface[] draw(int xSide, int ySide, int zSide) {
		return new Surface[] {top, bottom, left, right, front, back};
	}
	
	// TODO: re-do this code
	private void initSurfaces() {
		// dimensions
		double size = 0.5;
		double topZ = z + size;
		double bottomZ = z - size;
		double leftX = x - size;
		double rightX = x + size;
		double frontY = y - size;
		double backY = y + size;
		
		// side coordinates
		double[] xs = new double[] {leftX, rightX, rightX, leftX};
		double[] ys = new double[] {backY, backY, frontY, frontY};
		double[] zs = new double[] {bottomZ, topZ, topZ, bottomZ};
		
		// from back/left -> back/right -> front/right -> front/left
		top = new Surface(xs, ys, topZ, true);
		bottom = new Surface(xs, ys, bottomZ, false);
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		left = new Surface(leftX, ys, zs, true);
		right = new Surface(rightX, ys, zs, false);
		
		xs = new double[] {leftX, leftX, rightX, rightX};
		// from left/bottom -> left/top -> right/top -> right/bottom
		front = new Surface(xs, frontY, zs, true);
		back = new Surface(xs, backY, zs, false);
		
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
	}
}
