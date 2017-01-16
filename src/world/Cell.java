package world;

import engine.Camera;
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
			surfaces = lShape.shape.draw(xSide, ySide, zSide);
			if (surfaces == null)
				remove(lShape);
			else
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
