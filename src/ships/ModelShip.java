package ships;

import engine.Controller;
import engine.Math3D;
import engine.Painter;
import module.*;
import shapes.CubeFrame;
import shapes.Shape;
import shapes.StaticCube;
import world.World;

import java.awt.*;
import java.io.*;

public class ModelShip extends Ship implements Serializable {
	private final static String SAVE_PATH = "modelShipScratch.ot";
	private Blueprint blueprint;
	private String saveStatus;
	
	private final static int WORLD_EDGE = 5;
	
	private int[] selected, nextSelected;
	private int moduleSelected;
	private int directionSelected;
	
	private final static int MODULE_EMPTY_MODULE = 0, MODULE_HULL = 1, MODULE_ROTOR = 2, MODULE_HELIUM = 3, MODULE_FORW_BLADE = 4;
	private final static String[] MODULE_NAMES = new String[] {"REMOVE", "HULL", "ROTOR", "HELIUM", "FORWARD BLADE"};
	private final static String[] DIRECTION_NAMES = new String[] {"LEFT", "RIGHT", "FRONT", "BACK", "BOTTOM", "TOP"};
	
	public ModelShip(World world) {
		super(WORLD_EDGE, WORLD_EDGE, WORLD_EDGE, 0, 0, 0, world);
		addToWorld(world);
		initWorld(world);
	}
	
	void generateParts() {
		if (blueprint == null)
			blueprint = Blueprint.defaultBlueprint();
		
		part = new Module[blueprint.width][blueprint.length][blueprint.height];
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					updatePart(x, y, z);
	}
	
	void setParts() {
		for (int x = 0; x < part.length; x++)
			for (int y = 0; y < part[x].length; y++)
				for (int z = 0; z < part[x][y].length; z++)
					part[x][y][z].set(blueprint.blueprint[x][y][z][1], new double[3]);
	}
	
	private void updatePart(int x, int y, int z) {
		Module module;
		switch (blueprint.blueprint[x][y][z][0]) {
			case MODULE_EMPTY_MODULE:
				module = new EmptyModule();
				break;
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
		module.set(blueprint.blueprint[x][y][z][1], new double[3]);
		part[x][y][z] = module;
	}
	
	void initWorld(World world) {
		for (int xi = 0; xi < part.length + WORLD_EDGE * 2; xi++)
			for (int yi = 0; yi < part[0].length + WORLD_EDGE * 2; yi++)
				for (int zi = 0; zi < 1; zi++)
					if (xi < WORLD_EDGE || xi > part.length + WORLD_EDGE || yi < WORLD_EDGE || yi > part[0].length + WORLD_EDGE)
						world.addShape(xi, yi, zi, new StaticCube(xi + .5, yi + .5, zi + .5, Color.GRAY));
					else
						world.addShape(xi, yi, zi, new StaticCube(xi + .5, yi + .5, zi + .5, Color.LIGHT_GRAY));
	}
	
	void addToWorld(World world) {
		// selected frame
		drawCounter++;
		if (selected != null)
			world.addShape((int) x + selected[1], (int) y + selected[0], (int) z + selected[2], new CubeFrame(x + selected[1] + .5, y + selected[0] + .5, z + selected[2] + .5, new Math3D.Angle(0), new Math3D.Angle(0), new Math3D.Angle(0), .5, this));
		
		// ship
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
		selectDirection(controller);
		modify(controller);
		saveLoad(controller);
		addToWorld(world);
		textOutput();
	}
	
	private void selectCube(Controller controller) {
		double x = controller.viewOrig[1] - this.x, y = controller.viewOrig[0] - this.y, z = controller.viewOrig[2] - this.z;
		int curx = (int) x, cury = (int) y, curz = (int) z;
		int nextx = curx, nexty = cury, nextz = curz;
		double deltax, deltay, deltaz;
		double movex, movey, movez, move;
		double moved = 0, maxMoved = 75;
		
		//		System.out.println("begin");
		while (moved < maxMoved && inBounds(nextx, nexty, nextz) && isEmpty(nextx, nexty, nextz)) {
			//			System.out.println(x + " " + y + " " + z);
			if (controller.viewDir[1] > 0)
				deltax = Math3D.notZero(1 + nextx - x, 1);
			else
				deltax = Math3D.notZero(nextx - x, -1);
			if (controller.viewDir[0] > 0)
				deltay = Math3D.notZero(1 + nexty - y, 1);
			else
				deltay = Math3D.notZero(nexty - y, -1);
			if (controller.viewDir[2] > 0)
				deltaz = Math3D.notZero(1 + nextz - z, 1);
			else
				deltaz = Math3D.notZero(nextz - z, -1);
			
			if (Math3D.isZero(controller.viewDir[1]))
				movex = Math3D.sqrt3;
			else
				movex = deltax / controller.viewDir[1];
			if (Math3D.isZero(controller.viewDir[0]))
				movey = Math3D.sqrt3;
			else
				movey = deltay / controller.viewDir[0];
			if (Math3D.isZero(controller.viewDir[2]))
				movez = Math3D.sqrt3;
			else
				movez = deltaz / controller.viewDir[2];
			
			if (movex <= 0 || movey <= 0 || movez <= 0)
				System.out.println("move by <= 0");
			
			move = Math3D.min(movex, movey, movez) + Math3D.EPSILON;
			moved += move;
			
			x += controller.viewDir[1] * move;
			y += controller.viewDir[0] * move;
			z += controller.viewDir[2] * move;
			
			curx = nextx;
			cury = nexty;
			curz = nextz;
			
			nextx = (int) x;
			nexty = (int) y;
			nextz = (int) z;
			if (x < 0)
				nextx--;
			if (y < 0)
				nexty--;
			if (z < 0)
				nextz--;
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
	}
	
	private void selectDirection(Controller controller) {
		if (controller.isKeyPressed(Controller.KEY_I))
			directionSelected = Math3D.FRONT;
		else if (controller.isKeyPressed(Controller.KEY_J))
			directionSelected = Math3D.LEFT;
		else if (controller.isKeyPressed(Controller.KEY_K))
			directionSelected = Math3D.BACK;
		else if (controller.isKeyPressed(Controller.KEY_L))
			directionSelected = Math3D.RIGHT;
		else if (controller.isKeyPressed(Controller.KEY_U))
			directionSelected = Math3D.BOTTOM;
		else if (controller.isKeyPressed(Controller.KEY_O))
			directionSelected = Math3D.TOP;
	}
	
	private void modify(Controller controller) {
		if (!controller.isMousePressed())
			return;
		
		int[] xyz;
		if (moduleSelected == MODULE_EMPTY_MODULE)
			xyz = nextSelected;
		else
			xyz = selected;
		
		if (xyz != null) {
			blueprint.blueprint[xyz[0]][xyz[1]][xyz[2]][0] = (byte) moduleSelected;
			blueprint.blueprint[xyz[0]][xyz[1]][xyz[2]][1] = (byte) directionSelected;
			updatePart(xyz[0], xyz[1], xyz[2]);
			saveStatus = "unsaved changes - press '/' to save, press '\' to load";
		}
		
	}
	
	private void saveLoad(Controller controller) {
		if (controller.isKeyPressed(Controller.KEY_SLASH))  // save
			try {
				FileOutputStream fileOut = new FileOutputStream(SAVE_PATH);
				ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
				objectOut.writeObject(blueprint);
				objectOut.close();
				saveStatus = "saved model ship to " + SAVE_PATH;
			} catch (Exception ex) {
				saveStatus = "error saving model ship to " + SAVE_PATH;
				ex.printStackTrace();
			}
		
		else if (controller.isKeyPressed(Controller.KEY_BACK_SLASH)) // load
			try {
				FileInputStream fileIn = new FileInputStream(SAVE_PATH);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				Blueprint blueprint = (Blueprint) objectIn.readObject();
				this.blueprint = blueprint;
				generateParts();
				setParts();
				saveStatus = "loaded model ship from " + SAVE_PATH;
				objectIn.close();
			} catch (Exception ex) {
				saveStatus = "error loading model ship from " + SAVE_PATH;
				ex.printStackTrace();
			}
	}
	
	private void textOutput() {
		Painter.outputString[0] = "seleced module: " + MODULE_NAMES[moduleSelected];
		Painter.outputString[1] = "seleced direction: " + DIRECTION_NAMES[directionSelected];
		Painter.outputString[2] = saveStatus;
	}
	
	private boolean inBounds(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < part.length && y < part[0].length && z < part[0][0].length;
	}
	
	private boolean isEmpty(int x, int y, int z) {
		return part[x][y][z].mass == 0;
	}
	
	private static class Blueprint implements Serializable {
		private byte width, length, height;
		private byte[][][][] blueprint;
		
		private static Blueprint defaultBlueprint() {
			Blueprint bp = new Blueprint();
			bp.width = 21;
			bp.length = 21;
			bp.height = 21;
			bp.blueprint = new byte[bp.width][bp.length][bp.height][2];
			bp.blueprint[10][10][10][0] = MODULE_HULL;
			return bp;
		}
		
		private void writeObject(java.io.ObjectOutputStream out) throws IOException {
			out.writeByte(width);
			out.writeByte(length);
			out.writeByte(height);
			for (int x = 0; x < width; x++)
				for (int y = 0; y < length; y++)
					for (int z = 0; z < height; z++) {
						out.writeByte(blueprint[x][y][z][0]);
						out.writeByte(blueprint[x][y][z][1]);
					}
		}
		
		private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
			width = in.readByte();
			length = in.readByte();
			height = in.readByte();
			blueprint = new byte[width][length][height][2];
			for (int x = 0; x < width; x++)
				for (int y = 0; y < length; y++)
					for (int z = 0; z < height; z++) {
						blueprint[x][y][z][0] = in.readByte();
						blueprint[x][y][z][1] = in.readByte();
					}
		}
	}
}