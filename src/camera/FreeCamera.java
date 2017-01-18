package camera;

import engine.Controller;

public class FreeCamera extends Camera {
	private static final double MOVE_SPEED = 1.2, ANGLE_SPEED = .04, MOUSE_DAMP_SPEED = 0.1;
	
	public void move(Controller c) {
		int dx = 0, dy = 0, dz = 0;
		if (c.isKeyDown(Controller.KEY_W))
			dy += 1;
		if (c.isKeyDown(Controller.KEY_S))
			dy -= 1;
		if (c.isKeyDown(Controller.KEY_A))
			dx -= 1;
		if (c.isKeyDown(Controller.KEY_D))
			dx += 1;
		if (c.isKeyDown(Controller.KEY_Z))
			dz -= 1;
		if (c.isKeyDown(Controller.KEY_X))
			dz += 1;
		if (dx != 0 || dy != 0 || dz != 0)
			moveRelative(dx, dy, dz, MOVE_SPEED);
		
		double dangle = 0, danglez = 0;
		if (c.isKeyDown(Controller.KEY_Q))
			dangle += 1;
		if (c.isKeyDown(Controller.KEY_E))
			dangle -= 1;
		if (c.isKeyDown(Controller.KEY_R))
			danglez += 1;
		if (c.isKeyDown(Controller.KEY_F))
			danglez -= 1;
		
		int[] mouse = c.getMouseMovement();
		dangle -= mouse[0] * MOUSE_DAMP_SPEED;
		danglez -= mouse[1] * MOUSE_DAMP_SPEED;
		
		if (dangle != 0 || danglez != 0)
			rotate(dangle, danglez, ANGLE_SPEED);
	}
}