package world;

import engine.Math3D;
import shapes.Cube;
import shapes.StaticCube;

import java.util.Random;

public class WorldCreator {
	public World world;
	
	public WorldCreator(int chunkWidth, int chunkLength, int chunkHeight, int chunkSize) {
		world = new World(chunkWidth, chunkLength, chunkHeight, chunkSize);
	}
	
	public void randDebris(int chunkSize, int width, int length, int height, double density) {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < length; y++)
				for (int z = 0; z < height; z++)
					if (Math.random() < density) {
						Math3D.Angle angle = new Math3D.Angle(Math.random() * Math.PI * 2);
						Math3D.Angle angleZ = new Math3D.Angle(Math.random() * Math.PI * 2);
						Math3D.Angle angleTilt = new Math3D.Angle(Math.random() * Math.PI * 2);
						double size = Math.random() * 2 + .25;
						world.addShape(x, y, z, new Cube(x + 0.5, y + 0.5, z + 0.5, angle, angleZ, angleTilt, size, null));
					}
	}
	
	public void flatFloor(int width, int length, int height) {
		boolean[] topSide = new boolean[6];
		topSide[Math3D.TOP] = true;
		for (int x = 0; x < width; x += 5)
			for (int y = 0; y < length; y += 5)
				for (int z = 0; z < height; z += 5)
					world.addShape(x, y, z, new StaticCube(x + 0.5, y + 0.5, z + 0.5, null, topSide, 2));
	}
	
	// size should be 2^n+1, for any int n; e.g. 5, 65, 257
	public void heightMap(int size, int height) {
		// credit to M. Jessup http://stackoverflow.com/questions/2755750/diamond-square-algorithm
		
		double[][] heightMap = new double[size][size];
		heightMap[0][0] = heightMap[0][size - 1] = heightMap[size - 1][0] =
				heightMap[size - 1][size - 1] = .5;
		
		double h = .5;
		Random r = new Random();
		for (int sideLength = size - 1; sideLength >= 2; sideLength /= 2, h /= 2) {
			int halfSide = sideLength / 2;
			for (int x = 0; x < size - 1; x += sideLength)
				for (int y = 0; y < size - 1; y += sideLength) {
					double avg = heightMap[x][y] + heightMap[x + sideLength][y] + heightMap[x][y + sideLength] + heightMap[x + sideLength][y + sideLength];
					avg /= 4.0;
					heightMap[x + halfSide][y + halfSide] = avg + (r.nextDouble() * 2 * h) - h;
				}
			for (int x = 0; x < size - 1; x += halfSide)
				for (int y = (x + halfSide) % sideLength; y < size - 1; y += sideLength) {
					double avg = heightMap[(x - halfSide + size) % size][y] + heightMap[(x + halfSide) % size][y] + heightMap[x][(y + halfSide) % size] + heightMap[x][(y - halfSide + size) % size];
					avg /= 4.0;
					avg = avg + (r.nextDouble() * 2 * h) - h;
					heightMap[x][y] = avg;
					if (x == 0)
						heightMap[size - 1][y] = avg;
					if (y == 0)
						heightMap[x][size - 1] = avg;
				}
		}
		
		//		boolean[] topSide = new boolean[6];
		//		topSide[Math3D.TOP] = true;
		
		for (int x = 0; x < size; x++)
			for (int y = 0; y < size; y++)
				for (int z = 0; z < heightMap[x][y] * height; z++)
					world.addShape(x, y, z, new StaticCube(x + 0.5, y + 0.5, z + 0.5, null, null, .5));
	}
	
	
	public static int rand(int height) {
		return (int) (Math.random() * height);
	}
	
	public static int addOrSubtract() {
		return ((int) (Math.random() * 2)) * 2 - 1;
	}
}
