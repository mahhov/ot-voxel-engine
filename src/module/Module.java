package module;

import engine.Math3D;
import shapes.Cube;
import shapes.Shape;
import ships.Ship;

import java.awt.*;

public class Module {
	public int mass;
	
	Module() {
		mass = 1;
	}
	
	public void set(int dir, double[] location) {
	}
	
	public void addForce(Math3D.Force force, boolean[] control) {
	}
	
	public Color[] getColors() {
		return null;
	}
	
	public Shape getShape(double xc, double yc, double zc, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double[] rightUp, boolean[] side, boolean[] innerSide, Ship ship) {
		return new Cube(xc, yc, zc, angle, angleZ, angleTilt, .5, side, getColors(), ship);
	}
}
