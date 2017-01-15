package world;

import engine.Camera;
import engine.Math3D;
import engine.Painter;
import particles.Particle;
import projectiles.Projectile;
import shapes.Cube;
import shapes.Shape;
import shapes.StaticCube;
import ships.Ship;

public class World {
	public final int width, length, height, chunkSize;
	private Chunk[][][] chunk;
	
	private Ship[] ship;
	private Projectile[] projectile;
	private Particle[] particle;
	
	public World(int chunkWidth, int chunkLength, int chunkHeight, int chunkSize, int chunksFill) {
		width = chunkWidth * chunkSize;
		height = chunkLength * chunkSize;
		length = chunkHeight * chunkSize;
		this.chunkSize = chunkSize;
		chunk = new Chunk[chunkWidth][chunkLength][chunkHeight];
		for (int x = 0; x < chunkWidth; x++)
			for (int y = 0; y < chunkLength; y++)
				for (int z = 0; z < chunkHeight; z++)
					chunk[x][y][z] = new Chunk();
		System.out.println("done creating chunks");
		
		fillWorldRand(chunksFill);
		System.out.println("done loading world");
		
		ship = new Ship[1];
		ship[0] = new Ship(50, 50, 25, 0, this);
	}
	
	// FILLING
	
	private void fillWorldRand(int scale) {
		for (int cx = 0; cx < 3 * scale; cx++)
			for (int cy = 0; cy < 3 * scale; cy++)
				for (int cz = 0; cz < scale; cz++) {
					chunk[cx][cy][cz].init(chunkSize);
					for (int x = 0; x < chunkSize; x++)
						for (int y = 0; y < chunkSize; y++)
							for (int z = 0; z < chunkSize; z++)
								if (Math.random() > 0.9995) {
									double angle = Math.random() * Math.PI * 2;
									double angleZ = Math.random() * Math.PI * 2;
									double size = Math.random() * 2 + .25;
									chunk[cx][cy][cz].add(x, y, z, new Cube(cx * chunkSize + x + 0.5, cy * chunkSize + y + 0.5, cz * chunkSize + z + 0.5, angle, angleZ, size));
								}
				}
	}
	
	private void fillWorldGround(int scale) {
		int border = scale * 10;
		for (int x = 0; x < border; x++)
			for (int y = 0; y < border; y++)
				addStaticCube(x, y, 0);
		for (int z = 1; z < 2; z++)
			for (int x = 0; x < border; x++) {
				addStaticCube(x, 0, z);
				addStaticCube(x, border - 1, z);
				addStaticCube(0, x, z);
				addStaticCube(border - 1, x, z);
			}
	}
	
	private void addStaticCube(int x, int y, int z) {
		addShape(x, y, z, new StaticCube(x + 0.5, y + 0.5, z + 0.5));
	}
	
	public Cell.LinkedShapes addShape(int x, int y, int z, Shape shape) {
		int cx = x / chunkSize;
		int cy = y / chunkSize;
		int cz = z / chunkSize;
		int sx = x - cx * chunkSize;
		int sy = y - cy * chunkSize;
		int sz = z - cz * chunkSize;
		chunk[cx][cy][cz].safeInit(chunkSize);
		return chunk[cx][cy][cz].add(sx, sy, sz, shape);
	}
	
	// DRAWING
	
	public void drawChunks(Painter painter, Camera c) {
		int boundaries[] = c.cullBoundaries();
		int volumeRaw = (boundaries[1] - boundaries[0]) * (boundaries[3] - boundaries[2]) * (boundaries[5] - boundaries[4]) / 100000;
		
		boundaries[0] = Math3D.max(boundaries[0], 0);
		boundaries[1] = Math3D.min(boundaries[1], width - 1);
		boundaries[2] = Math3D.max(boundaries[2], 0);
		boundaries[3] = Math3D.min(boundaries[3], length - 1);
		boundaries[4] = Math3D.max(boundaries[4], 0);
		boundaries[5] = Math3D.min(boundaries[5], height - 1);
		int volumeBound = (boundaries[1] - boundaries[0]) * (boundaries[3] - boundaries[2]) * (boundaries[5] - boundaries[4]) / 100000;
		
		
		int[] fromChunkCoord = getChunkCoord(boundaries[0], boundaries[2], boundaries[4]);
		int[] toChunkCoord = getChunkCoord(boundaries[1], boundaries[3], boundaries[5]);
		int[] cameraChunkCoord = getChunkCoord((int) c.x, (int) c.y, (int) c.z);
		int volumeChunk = (fromChunkCoord[0] - toChunkCoord[0]) * (fromChunkCoord[1] - toChunkCoord[1]) * (fromChunkCoord[2] - toChunkCoord[2]) / -1;
		
		Painter.debugString[1] = "(unit 100,000) volume raw " + volumeRaw + " ; (unit 100,000) volume bound " + volumeBound + " ; volume chunk " + volumeChunk;
		
		for (int x = fromChunkCoord[0]; x < cameraChunkCoord[0]; x++)
			drawChunksRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, Math3D.RIGHT);
		for (int x = toChunkCoord[0]; x > cameraChunkCoord[0]; x--)
			drawChunksRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, Math3D.LEFT);
		drawChunksRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cameraChunkCoord[0], Math3D.NONE);
	}
	
	private void drawChunksRow(Painter painter, Camera c, int[] fromChunkCoord, int[] toChunkCoord, int[] cameraChunkCoord, int x, int xSide) {
		for (int y = fromChunkCoord[1]; y < cameraChunkCoord[1]; y++)
			drawChunksColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, y, xSide, Math3D.BACK);
		for (int y = toChunkCoord[1]; y > cameraChunkCoord[1]; y--)
			drawChunksColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, y, xSide, Math3D.FRONT);
		drawChunksColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, cameraChunkCoord[1], xSide, Math3D.NONE);
	}
	
	private void drawChunksColumn(Painter painter, Camera c, int[] fromChunkCoord, int[] toChunkCoord, int[] cameraChunkCoord, int x, int y, int xSide, int ySide) {
		for (int z = fromChunkCoord[2]; z < cameraChunkCoord[2]; z++)
			drawChunk(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, y, z, xSide, ySide, Math3D.TOP);
		for (int z = toChunkCoord[2]; z > cameraChunkCoord[2]; z--)
			drawChunk(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, y, z, xSide, ySide, Math3D.BOTTOM);
		drawChunk(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, x, y, cameraChunkCoord[2], xSide, ySide, Math3D.NONE);
	}
	
	private void drawChunk(Painter painter, Camera c, int[] fromChunkCoord, int[] toChunkCoord, int[] cameraChunkCoord, int cx, int cy, int cz, int xSide, int ySide, int zSide) {
		if (chunk[cx][cy][cz].isEmpty())
			return;
		if (xSide == Math3D.RIGHT) {
			int startx = cx == fromChunkCoord[0] ? fromChunkCoord[3] : 0;
			for (int x = startx; x < chunkSize; x++)
				drawRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x);
		} else if (xSide == Math3D.LEFT) {
			int endx = cx == toChunkCoord[0] ? toChunkCoord[3] : chunkSize - 1;
			for (int x = endx; x >= 0; x--)
				drawRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x);
		} else {
			int startx = cx == fromChunkCoord[0] ? fromChunkCoord[3] : 0;
			int endx = cx == toChunkCoord[0] ? toChunkCoord[3] : chunkSize - 1;
			for (int x = startx; x < cameraChunkCoord[3]; x++)
				drawRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x);
			for (int x = endx; x > cameraChunkCoord[3]; x--)
				drawRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x);
			drawRow(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, cameraChunkCoord[3]);
		}
	}
	
	private void drawRow(Painter painter, Camera c, int[] fromChunkCoord, int[] toChunkCoord, int[] cameraChunkCoord, int cx, int cy, int cz, int xSide, int ySide, int zSide, int x) {
		if (ySide == Math3D.BACK) {
			int starty = cy == fromChunkCoord[1] ? fromChunkCoord[4] : 0;
			for (int y = starty; y < chunkSize; y++)
				drawColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x, y);
		} else if (ySide == Math3D.FRONT) {
			int endy = cy == toChunkCoord[1] ? toChunkCoord[4] : chunkSize - 1;
			for (int y = endy; y >= 0; y--)
				drawColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x, y);
		} else {
			int starty = cy == fromChunkCoord[1] ? fromChunkCoord[4] : 0;
			int endy = cy == toChunkCoord[1] ? toChunkCoord[4] : chunkSize - 1;
			for (int y = starty; y < cameraChunkCoord[4]; y++)
				drawColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x, y);
			for (int y = endy; y > cameraChunkCoord[4]; y--)
				drawColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x, y);
			drawColumn(painter, c, fromChunkCoord, toChunkCoord, cameraChunkCoord, cx, cy, cz, xSide, ySide, zSide, x, cameraChunkCoord[4]);
		}
	}
	
	private void drawColumn(Painter painter, Camera c, int[] fromChunkCoord, int[] toChunkCoord, int[] cameraChunkCoord, int cx, int cy, int cz, int xSide, int ySide, int zSide, int x, int y) {
		if (zSide == Math3D.TOP) {
			int startz = cz == fromChunkCoord[2] ? fromChunkCoord[5] : 0;
			for (int z = startz; z < chunkSize; z++)
				drawCell(painter, c, cx, cy, cz, xSide, ySide, zSide, x, y, z);
		} else if (zSide == Math3D.BOTTOM) {
			int endz = cz == toChunkCoord[2] ? toChunkCoord[5] : chunkSize - 1;
			for (int z = endz; z >= 0; z--)
				drawCell(painter, c, cx, cy, cz, xSide, ySide, zSide, x, y, z);
		} else {
			int startz = cz == fromChunkCoord[2] ? fromChunkCoord[5] : 0;
			int endz = cz == toChunkCoord[2] ? toChunkCoord[5] : chunkSize - 1;
			for (int z = startz; z < cameraChunkCoord[5]; z++)
				drawCell(painter, c, cx, cy, cz, xSide, ySide, zSide, x, y, z);
			for (int z = endz; z > cameraChunkCoord[5]; z--)
				drawCell(painter, c, cx, cy, cz, xSide, ySide, zSide, x, y, z);
			drawCell(painter, c, cx, cy, cz, xSide, ySide, zSide, x, y, cameraChunkCoord[5]);
		}
	}
	
	private void drawCell(Painter painter, Camera c, int cx, int cy, int cz, int xSide, int ySide, int zSide, int x, int y, int z) {
		chunk[cx][cy][cz].draw(x, y, z, painter, c, xSide, ySide, zSide);
	}
	
	// UPDATE
	
	public void update() {
		for (Ship s : ship)
			s.update(this);
	}
	
	// UITL
	
	private int[] getChunkCoord(int x, int y, int z) {
		int cx = x / chunkSize;
		int cy = y / chunkSize;
		int cz = z / chunkSize;
		x -= cx * chunkSize;
		y -= cy * chunkSize;
		z -= cz * chunkSize;
		return new int[] {cx, cy, cz, x, y, z};
	}
}
