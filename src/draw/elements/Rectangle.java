package draw.elements;

import draw.manage.Manageable;
import draw.painter.Painter;

import java.awt.*;

public class Rectangle extends Draw implements Manageable {

	private double x0, y0, width, height;
	private int lineWidth;
	private Color color;
	private boolean fill;

	public Rectangle(double x0, double y0, double width, double height,
			int lineWidth, Color color, boolean fill) {
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public Rectangle(double x0, double y0, double x1, double y1, int lineWidth,
			Color color, boolean fill,
			@SuppressWarnings("unused") boolean corners) {
		width = x1 - x0;
		height = y1 - y0;

		if (width > 0)
			this.x0 = x0;
		else {
			width = -width;
			this.x0 = x1;
		}

		if (height > 0)
			this.y0 = y0;
		else {
			height = -height;
			this.y0 = y1;
		}

		this.lineWidth = lineWidth;
		this.color = color;
		this.fill = fill;
	}

	public void setPosition(double x, double y) {
		x0 = x;
		y0 = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void paint(Painter painter) {
		painter.drawRectangle(x0, y0, width, height, lineWidth, color, fill);
	}

}
