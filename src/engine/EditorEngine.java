package engine;

import camera.Camera;
import camera.FreeCamera;
import ships.ModelShip;
import ships.Ship;

class EditorEngine extends Engine {
	ModelShip modelShip;
	
	void fillWorld(int chunkFill) {
		modelShip = new ModelShip(world);
		world.setShip(new Ship[] {modelShip});
	}
	
	Camera createCamera() {
		FreeCamera camera = new FreeCamera();
		return camera;
	}
	
	void begin() {
		while (true) {
			painter.clear();
			camera.move(controller);
			camera.update(modelShip.fullWidth, modelShip.fullLength, modelShip.fullHeight);
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