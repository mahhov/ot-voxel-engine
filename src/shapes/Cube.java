package shapes;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

import static engine.Math3D.axisVectorsTilt;

public class Cube extends Shape {
	//			public static final Color PRIMARY_COLOR = new Color(120, 100, 20), SECONDARY_COLOR = new Color(240, 200, 40), TERNARY_COLOR = new Color(250, 0, 0);
	public static final Color PRIMARY_COLOR = Color.WHITE, SECONDARY_COLOR = Color.GREEN, TERNARY_COLOR = Color.RED;
	
	private double x, y, z;
	private Math3D.Angle angle, angleZ, angleTilt;
	private double size;
	private boolean surfacesDirty;
	private Surface top, bottom, left, right, front, back;
	private boolean[] side;
	private Color[] color;
	
	public Cube(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, Ship ship) {
		this(x, y, z, angle, angleZ, angleTilt, size, null, null, ship);
	}
	
	public Cube(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, boolean[] side, Color[] color, Ship ship) {
		super(ship);
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
		this.angleZ = angleZ;
		this.angleTilt = angleTilt;
		this.size = size;
		surfacesDirty = true;
		this.side = side != null ? side : new boolean[] {true, true, true, true, true, true};
		this.color = color != null ? color : new Color[] {PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR, PRIMARY_COLOR};
	}
	
	private void initSurfaces() {
		// axis  vectors
		double[] norm = Math3D.norm(angle, angleZ, size);
		double[] rightUp = axisVectorsTilt(norm, size, angleZ, angleTilt);
		
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
		if (side[Math3D.TOP]) {
			top = new Surface(leftBackTop, rightBackTop, rightFrontTop, leftFrontTop, true);
			top.setColor(color[Math3D.TOP]);
			top.setLight(1);
		}
		
		// from back/left -> back/right -> front/right -> front/left
		if (side[Math3D.BOTTOM]) {
			bottom = new Surface(leftBackBottom, rightBackBottom, rightFrontBottom, leftFrontBottom, false);
			bottom.setColor(color[Math3D.BOTTOM]);
			bottom.setLight(1);
		}
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		if (side[Math3D.LEFT]) {
			left = new Surface(leftBackBottom, leftBackTop, leftFrontTop, leftFrontBottom, true);
			left.setColor(color[Math3D.LEFT]);
			left.setLight(1);
		}
		
		// from bottom/back -> top/back -> top/front -> bottom/front
		if (side[Math3D.RIGHT]) {
			right = new Surface(rightBackBottom, rightBackTop, rightFrontTop, rightFrontBottom, false);
			right.setColor(color[Math3D.RIGHT]);
			right.setLight(1);
		}
		
		// from left/bottom -> left/top -> right/top -> right/bottom
		if (side[Math3D.FRONT]) {
			front = new Surface(leftFrontBottom, leftFrontTop, rightFrontTop, rightFrontBottom, true);
			front.setColor(color[Math3D.BACK]);
			front.setLight(1);
		}
		
		// from left/bottom -> left/top -> right/top -> right/bottom
		if (side[Math3D.BACK]) {
			back = new Surface(leftBackBottom, leftBackTop, rightBackTop, rightBackBottom, false);
			back.setColor(color[Math3D.FRONT]);
			back.setLight(1);
		}
		
		surfacesDirty = false;
	}
	
	Surface[] getSurfaces(int xSide, int ySide, int zSide) {
		if (surfacesDirty)
			initSurfaces();
		return new Surface[] {top, bottom, left, right, front, back};
	}
	
}
