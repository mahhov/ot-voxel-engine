package module;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

import static engine.Math3D.TOP_VECTOR;

public class ForwBlade extends Module {
	private static final double force = .05;
	private double[] location;
	private boolean[] controlUp, controlDown;
	private static final int STATE_INACTIVE = 0, STATE_UP = 1, STATE_DOWN = 2;
	private int state;
	private Color[] color;
	private Ship ship;
	
	public ForwBlade(Ship ship) {
		controlUp = new boolean[6];
		controlDown = new boolean[6];
		color = new Color[6];
		this.ship = ship;
	}
	
	public Color[] getColors() {
		return color;
	}
	
	public void set(int dir, double[] location) {
		this.location = location;
		
		if (location[0] < 0) {
			controlUp[Math3D.RIGHT] = true;
			controlDown[Math3D.LEFT] = true;
		} else if (location[0] > 0) {
			controlUp[Math3D.LEFT] = true;
			controlDown[Math3D.RIGHT] = true;
		}
		
		controlUp[Math3D.TOP] = true;
		controlDown[Math3D.BOTTOM] = true;
		
		for (int i = 0; i < color.length; i++)
			color[i] = Color.YELLOW;
	}
	
	public void addForce(Math3D.Force force, boolean[] control) {
		state = STATE_INACTIVE;
		for (int i = 0; i < this.controlUp.length; i++)
			if (control[i])
				if (this.controlUp[i]) {
					state = STATE_UP;
					break;
				} else if (this.controlDown[i]) {
					state = STATE_DOWN;
					break;
				}
		
		double forwVelocity = ship.getVForward();
		double f = this.force * forwVelocity * forwVelocity;
		if (state == STATE_UP)
			force.add(f, TOP_VECTOR, location);
		else if (state == STATE_DOWN)
			force.add(-f, TOP_VECTOR, location);
	}
}
