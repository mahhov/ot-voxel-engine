package engine;

import camera.TrailingCamera;
import world.World;

class Engine {
	
	private TrailingCamera camera;
	private Controller controller;
	private Painter painter;
	private World world;
	private boolean pause;
	
	private Engine() {
		int frame = 200, image = 200;
		Math3D.loadTrig(1000);
		camera = new TrailingCamera();
		controller = new Controller(frame / 2, frame / 2);
		painter = new Painter(frame, image, controller);
		int eachChunkSize = 5;
		int numChunks = 500 / eachChunkSize;
		int chunkFill = 50 / eachChunkSize;
		world = new World(numChunks, numChunks, numChunks, eachChunkSize, chunkFill);
		camera.setFollowShip(world.cameraShip);
	}
	
	private void begin() {
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
	
	private static void wait(int howLong) {
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