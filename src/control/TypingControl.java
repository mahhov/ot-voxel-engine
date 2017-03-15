//package control;
//
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseWheelEvent;
//
//public class TypingControl {
//	private static final int UP = 0, DOWN = 1, PRESSED = 2, RELEASED = 3;
//
//	private final int width, height;
//	private char key;
//	private boolean delete, backspace, enter;
//	private double mousex, mousey;
//	private int mouseState;
//
//	public TypingControl(int width, int height) {
//		this.width = width;
//		this.height = height;
//	}
//
//	public double getMouseX() {
//		return mousex;
//	}
//
//	public double getMouseY() {
//		return mousey;
//	}
//
//	public boolean getMousePress() {
//		if (mouseState == PRESSED) {
//			mouseState = DOWN;
//			return true;
//		}
//		return false;
//	}
//
//	public boolean getMouseRelease() {
//		if (mouseState == RELEASED) {
//			mouseState = UP;
//			return true;
//		}
//		return false;
//	}
//
//	public char getKey() {
//		char r = key;
//		key = 0;
//		return r;
//	}
//
//	public boolean getDelete() {
//		return delete;
//	}
//
//	public boolean getBackspace() {
//		return backspace;
//	}
//
//	public boolean getEnter() {
//		return enter;
//	}
//
//	//	public void resetToggleKeys() {
//	//		delete = false;
//	//		backspace = false;
//	//		enter = false;
//	//	}
//
//	public void keyReleased(KeyEvent e) {
//		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
//			if (e.isControlDown())
//				delete = true;
//			else
//				backspace = true;
//		} else if (e.getKeyCode() == KeyEvent.VK_DELETE)
//			delete = true;
//		else if (e.getKeyCode() == KeyEvent.VK_ENTER)
//			enter = true;
//		else {
//			char key = e.getKeyChar();
//			if (Character.isLetter(key) || Character.isDigit(key))
//				this.key = key;
//		}
//	}
//
//	public void mousePressed(MouseEvent e) {
//		mouseState = PRESSED;
//	}
//
//	public void mouseReleased(MouseEvent e) {
//		mouseState = RELEASED;
//	}
//
//	public void mouseDragged(MouseEvent e) {
//		mousex = e.getX() * 1f / width;
//		mousey = e.getY() * 1f / height;
//	}
//
//	public void mouseMoved(MouseEvent e) {
//		mousex = e.getX() * 1f / width;
//		mousey = e.getY() * 1f / height;
//	}
//
//	public void keyTyped(KeyEvent e) {
//	}
//
//	public void mouseClicked(MouseEvent e) {
//	}
//
//	public void mouseEntered(MouseEvent e) {
//	}
//
//	public void mouseExited(MouseEvent e) {
//	}
//
//	public void mouseWheelMoved(MouseWheelEvent e) {
//	}
//
//	public void keyPressed(KeyEvent e) {
//	}
//}
