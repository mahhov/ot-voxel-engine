package draw.painter;

import control.Control;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PainterJava extends Painter {
	
	private static final Font HEAD_FONT = new Font(Font.MONOSPACED, Font.BOLD,
			20);
	public static final Font BODY_FONT = new Font(Font.MONOSPACED, Font.BOLD,
			12);
	
	BufferedImage canvas;
	Graphics2D brush;
	
	public PainterJava(Control control) {
		this(control, 0, 0);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((dim.width - FRAME_SIZE) / 2,
				(dim.height - FRAME_SIZE) / 2);
	}
	
	public PainterJava(Control control, int x, int y) {
		super(x, y);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addKeyListener(control);
		addMouseMotionListener(control);
		addMouseListener(control);
		addMouseWheelListener(control);
		
		canvas = new BufferedImage(FRAME_SIZE, FRAME_SIZE,
				BufferedImage.TYPE_INT_RGB);
		brush = canvas.createGraphics();
	}
	
	public void paint() {
		paintLayers();
		
		if (brush != null) {
			brush.setFont(HEAD_FONT);
			brush.setColor(Color.WHITE);
			for (int i = 0; i < write.length; i++) {
				brush.drawString(write[i], 10, 30 + 30 * i);
			}
		}
		
		paint(getGraphics());
	}
	
	public void paint(Graphics g) {
		if (brush != null) {
			// write("paint count: " + count, 4);
			count = 0;
			
			// draw
			g.drawImage(canvas, 0, 0, getWidth(), getHeight(), null);
			
			// erase
			if (fade)
				brush.setColor(new Color(0x29001F));
			else
				brush.setColor(Color.WHITE);
			brush.fillRect(0, 0, getWidth(), getHeight());
		}
	}
	
	private void setColor(Color color) {
		brush.setColor(color);
	}
	
	private void setColor(Color color, double light) {
		if (light >= 1)
			setColor(color);
		else {
			light /= 255;
			setColor(new Color((float) (color.getRed() * light), (float) (color.getGreen() * light), (float) (color.getBlue() * light)));
		}
	}
	
	private void setLineWidth(int width) {
		brush.setStroke(new BasicStroke(width));
	}
	
	public void drawLine(double x0, double y0, double x1, double y1, int thickness,
						 Color color) {
		setColor(color);
		setLineWidth(thickness);
		brush.drawLine(stretch(x0), stretch(y0), stretch(x1), stretch(y1));
	}
	
	public void drawRectangle(double x0, double y0, double width, double height,
							  int thickness, Color color, boolean fill) {
		setColor(color);
		setLineWidth(thickness);
		if (fill)
			brush.fillRect(stretch(x0) - 1, stretch(y0) - 1,
					stretch(width) + 2, stretch(height) + 2);
		if (!fill || thickness != 1)
			brush.drawRect(stretch(x0), stretch(y0), stretch(width),
					stretch(height));
	}
	
	public void drawCircle(double x0, double y0, double width, double height,
						   short startAngle, short spanAngle, int thickness, Color color,
						   boolean fill) {
		setColor(color);
		setLineWidth(thickness);
		if (fill)
			brush.fillArc(stretch(x0), stretch(y0), stretch(width),
					stretch(height), startAngle, spanAngle);
		else
			brush.drawArc(stretch(x0), stretch(y0), stretch(width),
					stretch(height), startAngle, spanAngle);
	}
	
	public void drawPolygon(double[] x, double[] y, int thickness, Color color,
							double light, boolean fill) {
		count++;
		setColor(color, light);
		Polygon poly = new Polygon(stretch(x), stretch(y), x.length);
		if (fill)
			brush.fillPolygon(poly);
		else {
			setLineWidth(thickness);
			brush.drawPolygon(poly);
		}
	}
	
	public void drawMassText(String[] text) {
		brush.setFont(BODY_FONT);
		brush.setColor(Color.BLACK);
		for (int i = 0; i < text.length; i++)
			brush.drawString(text[i], stretch(.15f), stretch(.3f + .03f * i));
	}
	
	public void drawText(String text, int size, Color color, double x, double y) {
		setFont(size);
		brush.setColor(Color.WHITE);
		brush.drawString(text, stretch(x), stretch(y));
	}
	
	public void drawCenteredText(String text, int size, Color color, double x,
								 double y, double width, double height, boolean horizontallyAligned) {
		setFont(size);
		brush.setColor(color);
		
		FontMetrics metric = brush.getFontMetrics();
		if (horizontallyAligned) {
			double textWidth = metric.stringWidth(text) * 1f / FRAME_SIZE;
			x += (width - textWidth) / 2;
		}
		double textHeight = metric.getHeight() * .5f / FRAME_SIZE;
		y += (height - textHeight) / 2 + textHeight;
		
		brush.drawString(text, stretch(x), stretch(y));
	}
	
	// not private so pixel painter can override
	void setFont(int size) {
		brush.setFont(BODY_FONT.deriveFont(size));
	}
	
	// not private so pixel painter can override
	int stretch(double i) {
		return (int) (i * FRAME_SIZE);
	}
	
	int[] stretch(double[] i) {
		int[] r = new int[i.length];
		for (int j = 0; j < i.length; j++)
			r[j] = stretch(i[j]);
		return r;
	}
	
	public void drawImage(BufferedImage image, double x0, double y0, double width,
						  double height) {
		brush.drawImage(image, stretch(x0), stretch(y0), stretch(width),
				stretch(height), null);
	}
}