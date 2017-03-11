package draw.elements;

import draw.painter.Painter;

import java.awt.*;

public class Polygon extends Draw {

	double x[], y[];
	int thickness;
	Color color;
	double light;
	boolean fill;

	public Polygon(double[] x, double[] y, int thickness, Color color,
			double light, boolean fill) {
		this.x = x;
		this.y = y;
		this.thickness = thickness;
		this.color = color;
		this.light = light;
		this.fill = fill;
	}

	public Polygon(double cx, double cy, double forwardx, double forwardy,
			double sidex, double sidey, int thickness, Color color, boolean fill) {
		this.thickness = thickness;
		this.color = color;
		this.fill = fill;

		x = new double[] { cx + forwardx, cx + sidex, cx - forwardx, cx - sidex };
		y = new double[] { cy + forwardy, cy + sidey, cy - forwardy, cy - sidey };

		light = 1;
	}

	public void paint(Painter painter) {
		painter.drawPolygonIfInScreen(x, y, thickness, color, light, fill);
	}

}
