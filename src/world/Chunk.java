package world;

import engine.Camera;
import engine.Painter;
import shapes.Shape;

class Chunk {
	private Cell[][][] cell;
	private int count;
	
	void init(int size) {
		cell = new Cell[size][size][size];
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				for (int z = 0; z < size; z++)
					cell[x][y][z] = new Cell();
		count = 0;
	}
	
	boolean isEmpty() {
		return count == 0;
	}
	
	void add(int x, int y, int z, Shape shape) {
		cell[x][y][z].add(shape);
		count++;
	}
	
	void remove(int x, int y, int z, Cell.LinkedShapes shape) {
		cell[x][y][z].remove(shape);
		count--;
	}
	
	void draw(int x, int y, int z, Painter painter, Camera c, int xSide, int ySide, int zSide) {
		cell[x][y][z].drawAll(painter, c, xSide, ySide, zSide);
	}
}