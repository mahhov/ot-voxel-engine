//package draw.painter;
//
//import org.lwjgl.LWJGLException;
//import org.lwjgl.opengl.Display;
//import org.lwjgl.opengl.DisplayMode;
//import org.lwjgl.opengl.GL11;
//import player.GameControl;
//
//import java.awt.*;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//
//public class PainterLwjgl extends Painter {
//
//	private LwjglInputRedirect inputRedirect;
//	private boolean closeDisplay;
//
//	public PainterLwjgl(GameControl control, int x, int y) {
//		super(x, y);
//
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				closeDisplay = true;
//			}
//		});
//
//		inputRedirect = new LwjglInputRedirect(FRAME_SIZE, control);
//	}
//
//	private void createDisplay() {
//		try {
//			Display.setDisplayMode(new DisplayMode(FRAME_SIZE, FRAME_SIZE));
//			Canvas c = new Canvas();
//			c.setSize(FRAME_SIZE, FRAME_SIZE);
//			add(c);
//			Display.setParent(c);
//			Display.create();
//		} catch (LWJGLException e) {
//			e.printStackTrace();
//		}
//	}
//
//	// toggle pause
//	public void pause() {
//	}
//
//	public void unpause() {
//	}
//
//	public void paint() {
//		if (!Display.isCreated())
//			createDisplay();
//		if (closeDisplay) {
//			Display.destroy();
//			System.exit(0);
//		} else {
//			paintLayers();
//
//			Display.update();
//			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//			inputRedirect.redirect();
//		}
//	}
//
//	public void drawLine(double x0, double y0, double x1, double y1, int thickness,
//			Color color) {
//	}
//
//	public void drawRectangle(double x0, double y0, double width, double height,
//			int thickness, Color color, boolean fill) {
//	}
//
//	public void drawCircle(double cx, double cy, double width, double height,
//			short startAngle, short spanAngle, int thickness, Color color,
//			boolean fill) {
//	}
//
//	public void drawPolygon(double[] x, double[] y, int thickness, Color color,
//			double light, boolean fill) {
//		GL11.glColor3f(color.getRed() * light / 255, color.getGreen() * light
//				/ 255, color.getBlue() * light / 255);
//		GL11.glBegin(GL11.GL_POLYGON);
//		for (byte i = 0; i < x.length; i++)
//			GL11.glVertex2d(x[i] * 2 - 1, -y[i] * 2 + 1);
//		GL11.glEnd();
//	}
//
//	public void drawMassText(String[] text) {
//	}
//
//	public void drawText(String text, double size, Color color, double x, double y) {
//	}
//
//	public void drawCenteredText(String text, double size, Color color, double x,
//			double y, double width, double height, boolean horizontallyAligned) {
//	}
//
//}