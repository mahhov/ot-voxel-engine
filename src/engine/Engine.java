package engine;

import camera.Camera;
import camera.TrailingCamera;
import control.Controller;
import list.LList;
import ships.FileShip;
import ships.Ship;
import world.World;

class Engine {
	Camera camera;
	Controller controller;
	Painter painter;
	World world;
	Ship ship;
	private boolean pause;
	
	Engine() {
		int frame = 800, image = frame;
		Math3D.loadTrig(1000);
		controller = new Controller(frame, frame);
		painter = new Painter(frame, image, controller);
		camera = createCamera();
	}
	
	Engine(Controller controller, Painter painter) {
		this.controller = controller;
		this.painter = painter;
		camera = createCamera();
	}
	
	Ship createShip() {
		return new FileShip(50, 50, 25, 0, 0, 0, world);
	}
	
	Camera createCamera() {
		TrailingCamera camera = new TrailingCamera();
		return camera;
	}
	
	private void createWorld() {
		int eachChunkSize = 5;
		int numChunks = 500 / eachChunkSize;
		int chunkFill = 50 / eachChunkSize;
		world = new World(numChunks, numChunks, numChunks, eachChunkSize);
		world.fillWorldRand(chunkFill);
		ship = createShip();
		((TrailingCamera) camera).setFollowShip(ship);
		world.setShip(new LList<>(null, null, ship));
	}
	
	void begin() {
		createWorld();
		int frame = 0;
		long beginTime = 0, endTime;
		while (checkContinue()) {
			while (pause) {
				checkPause();
				wait(30);
			}
			painter.clear();
			camera.move(controller);
			camera.update(world.width, world.length, world.height);
			world.update(controller);
			world.drawChunks(painter, camera);
			painter.repaint();
			checkPause();
			wait(30);
			endTime = System.nanoTime() + 1;
			if (endTime - beginTime > 1000000000L) {
				Painter.debugString[0] = "fps: " + frame + " ; paint surfaceCount: " + painter.surfaceCount + " ; paint drawCount: " + painter.drawCount;
				frame = 0;
				beginTime = endTime;
			}
			frame++;
		}
	}
	
	private void checkPause() {
		if (controller.isKeyPressed(Controller.KEY_P))
			pause = !pause;
	}
	
	boolean checkContinue() {
		return !controller.isKeyPressed(Controller.KEY_M);
	}
	
	static void wait(int howLong) {
		try {
			Thread.sleep(howLong);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		new Engine().begin();
	}
}

// TODO : don't keep throwing away old buffered image and graphics2d