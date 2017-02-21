package world;

import camera.Camera;
import engine.Painter;
import shapes.Shape;
import shapes.Surface;

public class Cell {
	Chunk chunk;
	private LinkedShapes shapes;
	
	Cell(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public void add(Shape shape) {
		chunk.count++;
		if (shapes == null) {
			shapes = new LinkedShapes(null, null, shape);
			return;
		}
		shapes = new LinkedShapes(shapes, null, shape);
		shapes.next.prev = shapes;
	}
	
	public void remove(LinkedShapes lShape) {
		chunk.count--;
		if (lShape.prev != null)
			lShape.prev.next = lShape.next;
		if (lShape.next != null)
			lShape.next.prev = lShape.prev;
		if (shapes == lShape)
			shapes = shapes.next;
	}
	
	public void drawAll(Painter painter, Camera c, int xSide, int ySide, int zSide) {
		LinkedShapes lShape = shapes;
		Surface surfaces[];
		while (lShape != null) {
			surfaces = lShape.shape.draw(xSide, ySide, zSide, c);
			if (surfaces == null)
				remove(lShape);
			else
				for (Surface s : surfaces)
					if (s != null)
						painter.clipPolygon(s.toCamera(c), s.tempDistanceLight, s.color, s.clipState);
			lShape = lShape.next;
		}
	}
	
	public class LinkedShapes {
		private LinkedShapes next, prev;
		private Shape shape;
		
		private LinkedShapes(LinkedShapes next, LinkedShapes prev, Shape shape) {
			this.next = next;
			this.prev = prev;
			this.shape = shape;
		}
	}
}
