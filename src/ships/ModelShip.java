package ships;

import engine.Controller;
import engine.Math3D;
import engine.Painter;
import module.*;
import shapes.CubeFrame;
import shapes.Shape;
import world.World;

public class ModelShip extends Ship {
	private int[] selected, nextSelected;
	private int moduleSelected;
	
	private final static int MODULE_EMPTY_MODULE = 0, MODULE_HULL = 1, MODULE_ROTOR = 2, MODULE_HELIUM = 3, MODULE_FORW_BLADE = 4;
	private final static String[] MODULE_NAMES = new String[] {"EMPTY MODULE", "HULL", "ROTOR", "HELIUM", "FORWARD BLADE"};
	
	public ModelShip(World world) {
		super(0, 0, 0, 0, 0, 0, world);
		addToWorld(world);
	}
	
	void generateParts() {
		part = new Module[21][21][21];
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					part[x][y][z] = new EmptyModule();
		for (int x = 10; x < 11; x++)
			for (int y = 10; y < 11; y++)
				for (int z = 10; z < 11; z++)
					part[x][y][z] = new Hull();
	}
	
	void setParts() {
		
	}
	
	void addToWorld(World world) {
		drawCounter++;
		if (selected != null)
			world.addShape((int) x + selected[1], (int) y + selected[0], (int) z + selected[2], new CubeFrame(x + selected[1] + .5, y + selected[0] + .5, z + selected[2] + .5, new Math3D.Angle(0), new Math3D.Angle(0), new Math3D.Angle(0), .5, this));
		
		Shape shape;
		double xc, yc, zc;
		for (int xi = 0; xi < part.length; xi++)
			for (int yi = 0; yi < part[xi].length; yi++)
				for (int zi = 0; zi < part[xi][yi].length; zi++) {
					xc = x + yi + 0.5;
					yc = y + xi + 0.5;
					zc = z + zi + 0.5;
					
					int[] block = new int[] {0, 0, 0, 0, 0, 0};//getBlock(xi, yi, zi);
					
					shape = part[xi][yi][zi].getShape(xc, yc, zc, angle, angleZ, angleTilt, rightUp, block, this);
					if (shape != null)
						world.addShape((int) xc, (int) yc, (int) zc, shape);
				}
		
	}
	
	public void update(World world, Controller controller) {
		selectCube(controller);
		selectModule(controller);
		modify(controller);
		addToWorld(world);
	}
	
	private void selectCube(Controller controller) {
		int curx = (int) (controller.selectOrig[1] - this.x + .5), cury = (int) (controller.selectOrig[0] - this.y + .5), curz = (int) (controller.selectOrig[2] - this.z + .5);
		int nextx = curx, nexty = cury, nextz = curz;
		double x = curx, y = cury, z = curz;
		double deltax, deltay, deltaz;
		double movex, movey, movez, move;
		double moved = 0, maxMoved = 75;
		
		while (moved < maxMoved && inBounds(nextx, nexty, nextz) && isEmpty(nextx, nexty, nextz)) {
			if (controller.selectDir[1] > 0)
				deltax = Math3D.notZero(x - nextx);
			else
				deltax = Math3D.notZero(x - nextx - 1);
			if (controller.selectDir[0] > 0)
				deltay = Math3D.notZero(y - nexty);
			else
				deltay = Math3D.notZero(nexty - y - 1);
			if (controller.selectDir[2] > 0)
				deltaz = Math3D.notZero(z - nextz);
			else
				deltaz = Math3D.notZero(nextz - z - 1);
			
			movex = deltax / controller.selectDir[1];
			movey = deltay / controller.selectDir[0];
			movez = deltaz / controller.selectDir[2];
			
			move = Math3D.min(movex, movey, movez);
			moved += move;
			
			x += controller.selectDir[1] * move;
			y += controller.selectDir[0] * move;
			z += controller.selectDir[2] * move;
			
			curx = nextx;
			cury = nexty;
			curz = nextz;
			
			nextx = (int) x;
			nexty = (int) y;
			nextz = (int) z;
		}
		
		if (moved > maxMoved || !inBounds(curx, cury, curz))
			selected = null;
		else {
			selected = new int[] {curx, cury, curz};
			if (!inBounds(nextx, nexty, nextz))
				nextSelected = null;
			else
				nextSelected = new int[] {nextx, nexty, nextz};
		}
	}
	
	private void selectModule(Controller controller) {
		if (controller.isKeyPressed(Controller.KEY_0))
			moduleSelected = 0;
		else if (controller.isKeyPressed(Controller.KEY_1))
			moduleSelected = 1;
		else if (controller.isKeyPressed(Controller.KEY_2))
			moduleSelected = 2;
		else if (controller.isKeyPressed(Controller.KEY_3))
			moduleSelected = 3;
		else if (controller.isKeyPressed(Controller.KEY_4))
			moduleSelected = 4;
		
		Painter.outputString[0] = "seleced module: " + MODULE_NAMES[moduleSelected];
	}
	
	private void modify(Controller controller) {
		if (!controller.isMousePressed())
			return;
		
		if (moduleSelected == MODULE_EMPTY_MODULE) {
			if (nextSelected != null)
				part[nextSelected[0]][nextSelected[1]][nextSelected[2]] = new EmptyModule();
		} else if (selected != null) {
			Module module;
			switch (moduleSelected) {
				case MODULE_HULL:
					module = new Hull();
					break;
				case MODULE_ROTOR:
					module = new Rotor();
					break;
				case MODULE_HELIUM:
					module = new Helium(this);
					break;
				case MODULE_FORW_BLADE:
					module = new ForwBlade(this);
					break;
				default:
					module = new Hull();
			}
			module.set(Math3D.TOP, new double[] {selected[0], selected[1], selected[2]});
			part[selected[0]][selected[1]][selected[2]] = module;
		}
	}
	
	private boolean inBounds(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < part.length && y < part[0].length && z < part[0][0].length;
	}
	
	private boolean isEmpty(int x, int y, int z) {
		return part[x][y][z].mass == 0;
	}
}