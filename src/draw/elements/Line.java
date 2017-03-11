package draw.elements;

import draw.painter.Painter;

import java.awt.*;

public class Line extends Draw {

	private double x0, y0, x1, y1;
	private int width;
	private Color color;

	public Line(double x0, double y0, double x1, double y1, int width) {
		this(x0, y0, x1, y1, width, Color.BLACK);
	}

	public Line(double x0, double y0, double x1, double y1, int width, Color color) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.width = width;
		this.color = color;
	}

	public void paint(Painter painter) {
		painter.drawLine(x0, y0, x1, y1, width, color);
	}

}
