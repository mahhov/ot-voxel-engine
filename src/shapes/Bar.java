package shapes;

import engine.Math3D;
import ships.Ship;

import java.awt.*;

public class Bar extends Cube {
	private double innerSize;
	
	public Bar(double x, double y, double z, Math3D.Angle angle, Math3D.Angle angleZ, Math3D.Angle angleTilt, double size, double innerSize, boolean[] side, Color[] color, Ship ship) {
		super(x, y, z, angle, angleZ, angleTilt, size, side, color, ship);
		this.innerSize = innerSize;
	}
	
	void initSurfaces() {
		Surface[] clipSurface = initSurfacesGeom(size, size, size, side, false, false);
		Surface[] backSurface = initSurfacesGeom(size, size, size, side, true, true);
		Surface[] barSurface = initSurfacesGeom(innerSize, size, innerSize, new boolean[] {true, true, true, true, true, true}, false, false);
		for (int i = 0; i < barSurface.length; i++) {
			if (barSurface[i] != null) {
				barSurface[i].setColor(color[i]);
				barSurface[i].setLight(1);
			}
			if (backSurface[i] != null) {
				backSurface[i].setColor(Cube.PRIMARY_COLOR);
				backSurface[i].setLight(1);
			}
		}
		
		if (backSurface[0] == null)
			backSurface[0] = new Surface.NullSurface();
		backSurface[0].setClip(Surface.CLIP_SET);
		if (barSurface[5] == null)
			barSurface[5] = new Surface.NullSurface();
		barSurface[5].setClip(Surface.CLIP_RESET);
		
		surface = new Surface[18];
		for (int i = 0; i < 6; i++) {
			if (clipSurface[i] != null)
				clipSurface[i].setClip(Surface.CLIP_ADD);
			surface[i] = clipSurface[i];
			surface[i + 6] = backSurface[i];
			surface[i + 12] = barSurface[i];
		}
	}
	
}
