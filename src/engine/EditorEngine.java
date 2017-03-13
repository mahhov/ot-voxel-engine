package engine;

import camera.Camera;
import camera.FreeCamera;
import control.Controller;
import list.LList;
import ships.ModelShip;
import ships.Ship;
import world.World;

class EditorEngine extends Engine {
	ModelShip modelShip;
	
	EditorEngine() {
		super();
		createWorld();
	}
	
	EditorEngine(Controller controller, Painter painter) {
		super(controller, painter);
		createWorld();
	}
	
	ModelShip createShip() {
		return new ModelShip(world);
	}
	
	Camera createCamera() {
		FreeCamera camera = new FreeCamera();
		return camera;
	}
	
	private void createWorld() {
		world = new World(1, 1, 1, 100);
		modelShip = createShip();
		world.setShip(new LList<>(null, null, modelShip));
	}
	
	void begin() {
		while (checkContinue()) {
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