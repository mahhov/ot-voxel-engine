package ships;

import engine.Math3D;
import module.ForwBlade;
import module.Hull;
import module.Module;
import module.Rotor;
import shapes.Shape;
import world.World;

public class SmallShip extends Ship {
	public SmallShip(double x, double y, double z, double angle, double angleZ, double angleTilt, World world) {
		super(x, y, z, angle, angleZ, angleTilt, world);
	}
	
	void generateParts() {
		// 5 hull
		// 4 blade
		// 3 hull
		// 2 rotor - back
		// 1 hull
		// 0 rotor - front
		
		part = new Module[2][6][1];
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					if (y == 0 || y == 2)
						part[x][y][z] = new Rotor();
					else if (y == 4)
						part[x][y][z] = new ForwBlade(this);
					else
						part[x][y][z] = new Hull();
		
	}
	
	void setParts() {
		int x = 0, y = 4, z = 0;
		part[x][y][z].set(Math3D.RIGHT, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.LEFT, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 3;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 2;
		part[x][y][z].set(Math3D.BACK, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BACK, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.BOTTOM, new double[] {x - massX, y - massY, z - massZ});
		x = 0;
		y = 0;
		part[x][y][z].set(Math3D.FRONT, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.FRONT, new double[] {x - massX, y - massY, z - massZ});
		
		x = 0;
		y = 5;
		part[x][y][z].set(Math3D.NONE, new double[] {x - massX, y - massY, z - massZ});
		x = 1;
		part[x][y][z].set(Math3D.NONE, new double[] {x - massX, y - massY, z - massZ});
	}
}