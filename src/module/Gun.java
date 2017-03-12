package module;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

public class Gun extends Module {
	private double[] direction;
	private int load;
	private final int loadTime;
	private double[] location;
	private Color[] color;
	Ship ship;
	
	public Gun() {
		this.loadTime = 100;
		color = new Color[6];
	}
	
	public void set(int dir, double[] location) {
		this.location = location;
		for (int i = 0; i < color.length; i++)
			color[i] = Color.PINK;
	}
	
	public void react(Math3D.Force force, boolean[] control) {
		if (load > 0)
			load--;
		if (load == 0) {
			load = loadTime;
			// todo: fire
		}
	}
	
	public Color[] getColors() {
		return color;
	}
}


// todo : visual indicator of load and aim direciton