package camera;

import engine.Controller;
import engine.Math3D;
import ships.Ship;

public class TrailingCamera extends Camera {
	private final static double MIN_TRAIL = 5, MAX_TRAIL = 60;
	private final static double TRAIL_SPEED = 2.5, ANGLE_SPEED = .1, MOTION_AVG = .2, MOUSE_DAMP_SPEED = .01;
	private double trailDistance;
	private Math3D.Angle trailAngle, trailAngleZ;
	Ship followShip;
	
	public TrailingCamera() {
		trailDistance = (MIN_TRAIL + MAX_TRAIL) / 2;
		trailAngle = new Math3D.Angle(0);
		trailAngleZ = new Math3D.Angle(0);
	}
	
	public void setFollowShip(Ship followShip) {
		this.followShip = followShip;
	}
	
	public void move(Controller c) {
		// distance
		if (c.isKeyDown(Controller.KEY_X))
			trailDistance = Math3D.min(trailDistance + TRAIL_SPEED, MAX_TRAIL);
		if (c.isKeyDown(Controller.KEY_Z))
			trailDistance = Math3D.max(trailDistance - TRAIL_SPEED, MIN_TRAIL);
		
		// trail angle
		int[] mouse = c.getMouseMovement();
		double angle = trailAngle.get() - mouse[0] * MOUSE_DAMP_SPEED;
		double angleZ = trailAngleZ.get() + mouse[1] * MOUSE_DAMP_SPEED;
		
		trailAngle.set(angle);
		trailAngleZ.set(angleZ);
		trailAngleZ.bound();
		
		// position
		double goalx = followShip.x + trailAngle.cos() * trailAngleZ.cos() * trailDistance;
		double goaly = followShip.y + trailAngle.sin() * trailAngleZ.cos() * trailDistance;
		double goalz = followShip.z + trailAngleZ.sin() * trailDistance;
		moveTo(goalx, goaly, goalz, MOTION_AVG);
		
		// camera angle
		lookAt(followShip.x, followShip.y, followShip.z);
	}
}
