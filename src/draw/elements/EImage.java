package draw.elements;

import draw.painter.Painter;

import java.awt.image.BufferedImage;

public class EImage extends Draw {

	private double x0, y0, width, height;
	private BufferedImage image;

	public EImage(BufferedImage image, double x0, double y0, double width,
			double height) {
		this.image = image;
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		this.height = height;
	}

	public void paint(Painter painter) {
		painter.drawImage(image, x0, y0, width, height);
	}

}
