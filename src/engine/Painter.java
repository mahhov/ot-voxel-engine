package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static engine.Camera.MIN_LIGHT;

public class Painter extends JFrame {
	
	private final int FRAME_SIZE, IMAGE_SIZE;
	private static int borderSize = 0;
	Graphics2D brush;
	private BufferedImage canvas;
	int surfaceCount, drawCount;
	
	Painter(int frameSize, int imageSize, Controller controller) {
		FRAME_SIZE = frameSize;
		IMAGE_SIZE = imageSize;
		canvas = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, 1);
		brush = (Graphics2D) canvas.getGraphics();
		getContentPane().setSize(FRAME_SIZE, FRAME_SIZE);
		pack();
		borderSize = getHeight();
		setSize(FRAME_SIZE, FRAME_SIZE + borderSize);
		setLocationRelativeTo(null);
		addMouseListener(controller);
		addKeyListener(controller);
		addMouseMotionListener(controller);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	void clear() {
		surfaceCount = 0;
		drawCount = 0;
		//		brush.setBackground(Color.WHITE);
		//		brush.clearRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
		canvas = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, 1);
		brush = (Graphics2D) canvas.getGraphics();
	}
	
	public void paint(Graphics graphics) {
		// graphics.drawImage(canvas, 0, borderSize, FRAME_SIZE, FRAME_SIZE, null);
		graphics.drawImage(canvas, 0, borderSize, null);
	}
	
	private void setColor(double light, Color color) {
		if (light < MIN_LIGHT)
			brush.setColor(Color.black);
		else {
			light = Math3D.min(1, light);
			brush.setColor(new Color((int) (light * color.getRed()), (int) (light * color.getGreen()), (int) (light * color.getBlue())));
		}
	}
	
	public void polygon(double[][] xy, double light, Color color) {
		if (xy != null) {
			surfaceCount++;
			for (int i = 0; i < xy[0].length; i++)
				if (xy[0][i] > -.5 && xy[0][i] < .5 && xy[1][i] < .5 && xy[1][i] > -.5) {
					drawCount++;
					int[][] xyScaled = Math3D.transform(xy, IMAGE_SIZE, IMAGE_SIZE / 2);
					setColor(light, color);
					brush.fillPolygon(xyScaled[0], xyScaled[1], xyScaled[0].length);
					return;
				}
		}
	}
}
