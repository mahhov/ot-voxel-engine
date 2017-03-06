package engine;

import camera.Camera;
import camera.FreeCamera;
import ships.ModelShip;
import ships.Ship;

class EditorEngine extends Engine {
	
	void fillWorld(int chunkFill) {
		Ship[] ship = new Ship[1];
		ship[0] = new ModelShip( world);
		world.setShip(ship);
	}
	
	Camera createCamera() {
		FreeCamera camera = new FreeCamera();
		return camera;
	}
	
	void begin() {
		while (true) {
			painter.clear();
			camera.move(controller);
			camera.update(world.width, world.length, world.height);
			controller.setView(camera.orig(), camera.normal);
			world.update(controller);
			world.drawChunks(painter, camera);
			painter.repaint();
			Painter.debugString[0] = "paint surfaceCount: " + painter.surfaceCount + " ; paint drawCount: " + painter.drawCount;
			wait(30);
		}
	}
	
	public static void main(String args[]) {
		new EditorEngine().begin();
	}
}