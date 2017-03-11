package draw.painter;

import draw.elements.Draw;
import list.Queue;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Painter extends JFrame implements Drawer {
	public static final int FRAME_SIZE = 800;

	public int count;
	private boolean pause;

	boolean fade;

	Queue<Draw> foreground;
	Queue<Draw> midground;
	Queue<Draw> background;
	Queue<Draw> overlay;

	String[] write;

	Painter(int x, int y) {
		super();
		setLocation(x, y);

		setResizable(false);
		setUndecorated(true);
		setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		pack();
		setVisible(true);

		foreground = new Queue<Draw>();
		midground = new Queue<Draw>();
		background = new Queue<Draw>();
		overlay = new Queue<Draw>();

		write = new String[5];
		for (int i = 0; i < write.length; i++)
			write[i] = "";
	}

	// toggle pause
	public void pause() {
		pause = !pause;
		dispose();
		setUndecorated(!pause);
		setVisible(true);
	}

	public void unpause() {
		if (pause)
			pause();
	}

	public void write(String s, int line) {
		if (s != null && line < write.length && line >= 0)
			write[line] = s;
		else
			System.out.println("painter.write error");
	}

	public void setFade(boolean fade) {
		this.fade = fade;
	}

	public abstract void paint();

	public void addForeground(Draw d) {
		foreground.add(d);
	}

	public void addMidground(Draw d) {
		midground.add(d);
	}

	public void addBackground(Draw d) {
		background.add(d);
	}

	public void addOverlay(Draw d) {
		overlay.add(d);
	}

	void paintLayers() {
		Draw d;
		while ((d = background.remove()) != null)
			d.paint(this);
		while ((d = midground.remove()) != null)
			d.paint(this);
		while ((d = foreground.remove()) != null)
			d.paint(this);
		while ((d = overlay.remove()) != null)
			d.paint(this);
	}

	public boolean inScreen(double[] x, double[] y) {
		for (int i = 0; i < x.length; i++)
			if (x[i] > 0 && x[i] < 1 && y[i] > 0 && y[i] < 1)
				return true;
		return false;
	}

	public abstract void drawLine(double x0, double y0, double x1, double y1,
			int thickness, Color color);

	public abstract void drawRectangle(double x0, double y0, double width,
			double height, int thickness, Color color, boolean fill);

	public abstract void drawCircle(double cx, double cy, double width,
			double height, short startAngle, short spanAngle, int thickness,
			Color color, boolean fill);

	public void drawPolygonIfInScreen(double[] x, double[] y, int thickness,
			Color color, double light, boolean fill) {
		if (inScreen(x, y))
			drawPolygon(x, y, thickness, color, light, fill);
	}

	public abstract void drawPolygon(double[] x, double[] y, int thickness,
			Color color, double light, boolean fill);

	public abstract void drawMassText(String[] text);

	public abstract void drawText(String text, int size, Color color,
			double x, double y);

	public abstract void drawCenteredText(String text, int size, Color color,
			double x, double y, double width, double height,
			boolean horizontallyAligned);

	public abstract void drawImage(BufferedImage image, double x0, double y0,
			double width, double height);

}
