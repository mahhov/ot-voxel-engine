package parts;

import engine.Math3D;
import shapes.Cube;

import java.awt.*;

public class Rotor extends Part {
	
	private static final double force = .01;
	private double[] direction, location;
	private int dir, opDir;
	private boolean[] control;
	private boolean active;
	private Color[] color;
	
	public Rotor() {
		control = new boolean[6];
		color = new Color[6];
	}
	
	public Color[] getColors() {
		if (active)
			color[opDir] = Cube.TERENARY_COLOR;
		else
			color[opDir] = Cube.SECONDARY_COLOR;
		return color;
	}
	
	public void set(int dir, double[] location) {
		this.dir = dir;
		opDir = Math3D.flipDirection(dir);
		this.location = location;
		
		switch (dir) {
			case Math3D.LEFT:
				if (location[1] < 0)
					control[Math3D.RIGHT] = true;
				else
					control[Math3D.LEFT] = true;
				direction = new double[] {-1, 0, 0};
				break;
			case Math3D.RIGHT:
				if (location[1] < 0)
					control[Math3D.LEFT] = true;
				else
					control[Math3D.RIGHT] = true;
				direction = new double[] {1, 0, 0};
				break;
			case Math3D.FRONT:
				control[Math3D.FRONT] = true;
				direction = new double[] {0, 1, 0};
				break;
			case Math3D.BACK:
				control[Math3D.BACK] = true;
				direction = new double[] {0, -1, 0};
				break;
			case Math3D.TOP:
				if (location[1] < 0)
					control[Math3D.BOTTOM] = true;
				else
					control[Math3D.TOP] = true;
				direction = new double[] {0, 0, 1};
				break;
			case Math3D.BOTTOM:
				if (location[1] < 0)
					control[Math3D.TOP] = true;
				else
					control[Math3D.BOTTOM] = true;
				direction = new double[] {0, 0, -1};
				break;
			default:
				direction = new double[] {0, 0, 0};
		}
		
		for (int i = 0; i < color.length; i++)
			if (i == opDir)
				color[i] = Cube.SECONDARY_COLOR;
			else
				color[i] = Cube.PRIMARY_COLOR;
	}
	
	public void addForce(Math3D.Force force, boolean[] control) {
		active = false;
		for (int i = 0; i < this.control.length; i++)
			if (this.control[i] && control[i]) {
				active = true;
				break;
			}
		
		if (active)
			force.add(this.force, direction, location);
	}
}
