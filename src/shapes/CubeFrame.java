package shapes;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

public class CubeFrame extends Cube {
	public CubeFrame(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, Ship ship) {
		super(x, y, z, angle, angleZ, angleTilt, size, ship);
	}
	
	public CubeFrame(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, boolean[] side, Color[] color, Ship ship) {
		super(x, y, z, angle, angleZ, angleTilt, size, side, color, ship);
	}
	
	void initSurfaces() {
		super.initSurfaces();
		for (int i = 0; i < surface.length; i++)
			if (surface[i] != null)
				surface[i].frame = true;
	}
}
