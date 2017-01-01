package world;

import engine.Camera;
import engine.Painter;
import shapes.Shape;
import shapes.Surface;

public class Cell {
	LinkedShapes shapes;
	int count;
	
	public LinkedShapes add(Shape shape) {
		if (shapes == null) {
			shapes = new LinkedShapes(null, null, shape);
			return shapes;
		}
		shapes = new LinkedShapes(null, shapes, shape);
		if (shapes.next != null)
			shapes.next.prev = shapes;
		return shapes;
	}
	
	public Shape remove(LinkedShapes lShape) {
		if (lShape.prev != null)
			lShape.prev.next = lShape.next;
		if (lShape.next != null)
			lShape.next.prev = lShape.prev;
		return lShape.shape;
	}
	
	public void drawAll(Painter painter, Camera c, int xSide, int ySide, int zSide) {
		LinkedShapes lShape = shapes;
		Surface surfaces[];
		while (lShape != null) {
			surfaces = lShape.shape.draw(xSide, ySide, zSide);
			for (Surface s : surfaces)
				if (s != null)
					painter.polygon(s.toCamera(c), s.tempDistanceLight, s.color);
			lShape = lShape.next;
		}
	}
	
	public void drawFirst(Painter painter, Camera c, int xSide, int ySide, int zSide) {
		if (shapes != null) {
			Surface[] surfaces = shapes.shape.draw(xSide, ySide, zSide);
			for (Surface s : surfaces)
				if (s != null)
					painter.polygon(s.toCamera(c), s.tempDistanceLight, s.color);
		}
	}
	
	class LinkedShapes {
		private LinkedShapes next, prev;
		private Shape shape;
		
		private LinkedShapes(LinkedShapes next, LinkedShapes prev, Shape shape) {
			this.next = next;
			this.prev = prev;
			this.shape = shape;
		}
	}
}
