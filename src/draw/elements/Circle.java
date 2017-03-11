package draw.elements;

import draw.painter.Painter;

import java.awt.*;

public class Circle extends Draw {

	private double cx, cy;
	private double diameter;
	private short startAngle, spanAngle;
	private int lineWidth;
	private Color color;
	private boolean fill;

	public Circle(double cx, double cy, double diameter, int lineWidth,
			Color color) {
		this(cx, cy, diameter, (short) 0, (short) 360, lineWidth, color, false);
	}

	public Circle(double cx, double cy, double diameter, Color color, boolean fill) {
		this(cx, cy, diameter, (short) 0, (short) 360,  1, color, fill);
	}

	public Circle(double cx, double cy, double diameter, short startAngle,
			short endAngle, int lineWidth, Color color, boolean fill) {
		this.cx = cx;
		this.cy = cy;
		this.diameter = diameter;
		this.startAngle = startAngle;
		this.spanAngle = (short) (endAngle - startAngle);
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public void paint(Painter painter) {
		painter.drawCircle(cx - diameter / 2, cy - diameter / 2, diameter,
				diameter, startAngle, spanAngle, lineWidth, color, fill);
	}

}
