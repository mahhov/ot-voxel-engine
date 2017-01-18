package engine;

import java.awt.*;
import java.awt.event.*;

public class Controller implements KeyListener, MouseListener, MouseMotionListener {
	
	static final int UP = 0, DOWN = 1, PRESSED = 2, RELEASED = 3;
	public static final int KEY_W = 0, KEY_A = 1, KEY_S = 2, KEY_D = 3, KEY_Q = 4, KEY_E = 5, KEY_R = 6, KEY_F = 7, KEY_Z = 8, KEY_X = 9, KEY_ESC = 10;
	
	Key[] keys;
	
	private final int centerMouseX, centerMouseY;
	int mouseX, mouseY;
	int mouseState, rightMouseState;
	
	private Robot robot;
	
	Controller(int centerMouseX, int centerMouseY) {
		keys = new Key[11];
		keys[KEY_W] = new Key(87);
		keys[KEY_A] = new Key(65);
		keys[KEY_S] = new Key(83);
		keys[KEY_D] = new Key(68);
		keys[KEY_Q] = new Key(81);
		keys[KEY_E] = new Key(69);
		keys[KEY_R] = new Key(82);
		keys[KEY_F] = new Key(70);
		keys[KEY_Z] = new Key(90);
		keys[KEY_X] = new Key(88);
		keys[KEY_ESC] = new Key(27);
		
		this.centerMouseX = centerMouseX;
		this.centerMouseY = centerMouseY;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void keyTyped(KeyEvent e) {
	}
	
	public void keyPressed(KeyEvent e) {
		if (setKeyState(e.getKeyCode(), PRESSED) == -1)
			System.out.println("key press: " + e.getKeyCode());
	}
	
	public void keyReleased(KeyEvent e) {
		setKeyState(e.getKeyCode(), RELEASED);
	}
	
	public void mouseClicked(MouseEvent e) {
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseState = PRESSED;
		else
			rightMouseState = PRESSED;
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseState = RELEASED;
		else
			rightMouseState = RELEASED;
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void mouseDragged(MouseEvent e) {
	}
	
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		mouseX += x - centerMouseX;
		mouseY += y - centerMouseY;
		robot.mouseMove(centerMouseX - x + e.getXOnScreen(), centerMouseY - y + e.getYOnScreen());
	}
	
	private int setKeyState(int keyCode, int state) {
		for (Key k : keys)
			if (k.code == keyCode) {
				k.state = state;
				return 0;
			}
		return -1;
	}
	
	private int getKeyState(int key) {
		int r = keys[key].state;
		if (r == PRESSED)
			keys[key].state = DOWN;
		else if (r == RELEASED)
			keys[key].state = UP;
		return r;
	}
	
	public boolean isKeyDown(int key) {
		int state = getKeyState(key);
		return state == PRESSED || state == DOWN;
	}
	
	public int[] getMouseMovement() {
		int[] r = new int[] {mouseX, mouseY};
		mouseX = 0;
		mouseY = 0;
		return r;
	}
	
	private class Key {
		int code, state;
		
		private Key(int code) {
			this.code = code;
		}
	}
}
