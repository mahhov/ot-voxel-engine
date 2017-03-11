package draw.elements;

import draw.manage.Manageable;
import draw.painter.Painter;
import draw.painter.PainterJava;

import java.awt.*;

public class Text extends Draw implements Manageable {
	
	String text;
	int size;
	double x, y;
	
	public Text(String text, double x, double y) {
		this(text, 12, x, y);
	}
	
	public Text(String text, int size, double x, double y) {
		this.text = text;
		this.size = size;
		this.x = x;
		this.y = y;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setWidth(double width) {
	}
	
	public void setHeight(double height) {
	}
	
	public double getWidth() {
		Font font = PainterJava.BODY_FONT.deriveFont(size);
		FontMetrics metric = Toolkit.getDefaultToolkit().getFontMetrics(font);
		double textWidth = metric.stringWidth(text) * 1f / Painter.FRAME_SIZE;
		return textWidth;
	}
	
	public void paint(Painter painter) {
		painter.drawText(text, size, Color.BLACK, x, y);
	}
	
}
