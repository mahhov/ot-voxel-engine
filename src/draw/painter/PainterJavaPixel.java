//package draw.painter;
//
//import control.Control;
//
//import java.awt.image.BufferedImage;
//
//import static sun.audio.AudioPlayer.player;
//
//public class PainterJavaPixel extends PainterJava {
//
//	private static final byte RATIO = 2;
//	private static final int PIXEL_FRAME_SIZE = 800 / RATIO;
//
//	public PainterJavaPixel(Control control) {
//		super(control);
//		pixelateCanvas();
//	}
//
//	public PainterJavaPixel(Control control, int x, int y) {
//		super(control, x, y);
//		pixelateCanvas();
//	}
//
//	private void pixelateCanvas() {
//		canvas = new BufferedImage(PIXEL_FRAME_SIZE, PIXEL_FRAME_SIZE,
//				BufferedImage.TYPE_INT_RGB);
//		brush = canvas.createGraphics();
//	}
//
//	int stretch(double i) {
//		return (int) (i * PIXEL_FRAME_SIZE);
//	}
//
//}