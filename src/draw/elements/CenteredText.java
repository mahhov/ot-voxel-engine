package draw.elements;

import draw.painter.Painter;

import java.awt.*;

public class CenteredText extends Text {

	private double width, height;
	private boolean centerHorizontally;
	private boolean outline;

	public CenteredText(String text, int size, double x, double y, double width,
			double height, boolean centerHorizontally, boolean outline) {
		super(text, size, x, y);
		if (width != -1)
			this.width = width;
		else
			this.width = super.getWidth();
		this.height = height;
		this.centerHorizontally = centerHorizontally;
		this.outline = outline;
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
		if (outline)
			painter.drawRectangle(x, y, width, height,  3, Color.BLACK,
					false);
		painter.drawCenteredText(text, size, Color.BLACK, x, y, width, height,
				centerHorizontally);
	}

}
