package engine;

import camera.Camera;
import camera.TrailingCamera;
import ships.Ship;
import ships.SmallShip;
import world.World;

class Engine {
	Camera camera;
	Controller controller;
	Painter painter;
	World world;
	private boolean pause;
	
	Engine() {
		int frame = 800, image = frame;
		Math3D.loadTrig(1000);
		controller = new Controller(frame / 2, frame / 2);
		painter = new Painter(frame, image, controller);
		int eachChunkSize = 5;
		int numChunks = 500 / eachChunkSize;
		int chunkFill = 50 / eachChunkSize;
		world = new World(numChunks, numChunks, numChunks, eachChunkSize);
		fillWorld(chunkFill);
		camera = createCamera();
	}
	
	void fillWorld(int chunkFill) {
		world.fillWorldRand(chunkFill);
		
		Ship[] ship = new Ship[1];
		ship[0] = new SmallShip(50, 50, 25, 0, 0, 0, world);
		world.setShip(ship);
	}
	
	Camera createCamera() {
		TrailingCamera camera = new TrailingCamera();
		camera.setFollowShip(world.cameraShip);
		return camera;
	}
	
	void begin() {
		int frame = 0;
		long beginTime = 0, endTime;
		while (true) {
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