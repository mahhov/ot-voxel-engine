package draw.elements;

import draw.painter.Painter;

import java.awt.*;

public class RotatedRectangle extends Draw {

	private double x[], y[];
	private int lineWidth;
	private Color color;
	private boolean fill;

	public RotatedRectangle(double cx, double cy, double width, double height,
			double angleCos, double angleSin, int lineWidth, Color color,
			boolean fill) {
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;

		width /= 2;
		height /= 2;
		x = new double[4];
		y = new double[4];

		x[0] = cx + height * angleCos - width * angleSin;
		y[0] = cy + height * angleSin + width * angleCos;

		x[1] = cx + height * angleCos + width * angleSin;
		y[1] = cy + height * angleSin - width * angleCos;

		x[2] = cx - height * angleCos + width * angleSin;
		y[2] = cy - height * angleSin - width * angleCos;

		x[3] = cx - height * angleCos - width * angleSin;
		y[3] = cy - height * angleSin + width * angleCos;
	}

	public void paint(Painter painter) {
		painter.drawPolygon(x, y, lineWidth, color, 1, fill);
	}

}
