package parts;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

public class Helium extends Part {
	
	private static final double force = .0005;
	private double[] location;
	private Color[] color;
	Ship ship;
	
	public Helium(Ship ship) {
		color = new Color[6];
		this.ship = ship;
	}
	
	public Color[] getColors() {
		return color;
	}
	
	public void set(int dir, double[] location) {
		this.location = location;
		this.location = new double[] {0, 5, 0};
		for (int i = 0; i < color.length; i++)
			color[i] = Color.YELLOW;
	}
	
	public void addForce(Math3D.Force force, boolean[] control) {
		double[] dir = new double[3];
		
		double[] absDir = new double[] {0, 0, 1};
		double[] up = new double[] {ship.rightUp[3], ship.rightUp[4], ship.rightUp[5]};
		dir[0] = Math3D.dotProductUnormalized(absDir, ship.rightUp);
		dir[1] = Math3D.dotProductUnormalized(absDir, ship.norm);
		dir[2] = Math3D.dotProductUnormalized(absDir, up);
		
		force.add(this.force, dir, location);
	}
}
