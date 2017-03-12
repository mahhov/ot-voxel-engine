package module;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

public class Gun extends Module {
	private static final int LOAD_TIME = 100;
	private int load;
	private double[] direction;
	private double[] location;
	private Color[] color;
	Ship ship;
	
	public Gun(Ship ship) {
		color = new Color[6];
		this.ship = ship;
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
			load = LOAD_TIME;
			System.out.println("fire!!!");
			// todo: fire
		}
	}
	
	public Color[] getColors() {
		return color;
	}
}


// todo : visual indicator of load and aim direciton