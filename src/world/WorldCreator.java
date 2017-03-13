package world;

import engine.Math3D;
import shapes.Cube;

public class WorldCreator {
	public World world;
	
	public WorldCreator(int chunkWidth, int chunkLength, int chunkHeight, int chunkSize) {
		world = new World(chunkWidth, chunkLength, chunkHeight, chunkSize);
	}
	
	public void fillWorldRand(int chunkSize, int width, int length, int height, double density) {
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
	
	public void fillWorldGround(int width, int length, int height) {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < length; y++)
				for (int z = 0; z < height; z++)
					world.addStaticCube(x, y, z, null);
	}
}
