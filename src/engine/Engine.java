package engine;

import world.World;

class Engine {
	
	Camera camera;
	Controller controller;
	Painter painter;
	World world;
	
	Engine() {
		int frame = 600, image = 600;
		Math3D.loadTrig(1000);
		camera = new Camera();
		controller = new Controller(frame / 2, frame / 2);
		painter = new Painter(frame, image, controller);
		int eachChunkSize = 5;
		int numChunks = 500 / eachChunkSize;
		int chunkFill = 50 / eachChunkSize;
		world = new World(numChunks, numChunks, numChunks, eachChunkSize, chunkFill);
	}
	
	private void begin() {
		int frame = 0;
		long beginTime = 0, endTime;
		while (true) {
			painter.clear();
			camera.move(controller);
			camera.update(world.width, world.length, world.height);
			world.drawChunks(painter, camera);
			painter.repaint();
			wait(5);
			endTime = System.nanoTime() + 1;
			if (endTime - beginTime > 1000000000l) {
				painter.debugString[0] = "fps: " + frame + " ; paint surfaceCount: " + painter.surfaceCount + " ; paint drawCount: " + painter.drawCount;
				frame = 0;
				beginTime = endTime;
			}
			frame++;
		}
	}
	
	public static void wait(int howLong) {
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
// TODO : closer camera draw boundaries
// TODO : redo cube -> surface code and support angles