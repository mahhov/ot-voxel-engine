package engine;

public class Client {
	Engine engine;
	EditorEngine eengine;
	Painter painter;
	Controller controller;
	
	Client() {
		int frame = 200, image = frame;
		Math3D.loadTrig(1000);
		controller = new Controller(frame / 2, frame / 2);
		painter = new Painter(frame, image, controller);
		engine = new Engine(controller, painter);
		eengine = new EditorEngine(controller, painter);
	}
	
	void begin() {
		while (true) {
			eengine.begin();
			engine.begin();
		}
	}
	
	public static void main(String[] args) {
		new Client().begin();
	}
}
