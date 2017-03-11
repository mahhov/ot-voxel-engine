//package engine;
//
//import java.awt.*;
//
//class SplitInterface {
//	private final static Color BORDER_COLOR = Color.DARK_GRAY, BACK_COLOR = Color.LIGHT_GRAY, HOVER_COLOR = Color.WHITE, SELECT_COLOR = Color.GRAY, NORMAL_COLOR = Color.RED;
//	private static final double ROW_PADDING = .01, COLUMN_PADDING = .01;
//	private double rowHeight;
//	private Row[] row;
//	private LinkedElements lelements;
//
//	private SplitInterface(int numRow) {
//		setNumRow(numRow);
//	}
//
//	void setNumRow(int numRow) {
//		rowHeight = (1 - ROW_PADDING) / numRow;
//		row = new Row[numRow];
//	}
//
//	void setNumColumn(int row, int numColumn) {
//		this.row[row] = new Row(numColumn);
//	}
//
//	void setNumColumn(int row, int externalPadding, int internalPadding, int numColumn) {
//		this.row[row] = new Row(externalPadding, internalPadding, numColumn);
//	}
//
//	void addElement(Element el) {
//		if (lelements == null) {
//			lelements = new LinkedElements(null, null, el);
//			return;
//		}
//		lelements = new LinkedElements(lelements, null, el);
//		lelements.next.prev = lelements;
//	}
//
//	void update(Controller controller, Painter painter) {
//		LinkedElements linkedElementsIter = lelements;
//		while (linkedElementsIter != null) {
//			linkedElementsIter.element.react(controller);
//			linkedElementsIter.element.paint(painter);
//			linkedElementsIter = linkedElementsIter.next;
//		}
//	}
//
//	private double getRowY(int row) {
//		return ROW_PADDING + rowHeight * row;
//	}
//
//	private double getRowHeight(int nrow) {
//		return rowHeight * nrow - ROW_PADDING;
//	}
//
//	private class Row {
//		private double externalPadding, internalPadding;
//		private double columnWidth;
//
//		private Row(int numColumn) {
//			this(0, 0, numColumn);
//		}
//
//		private Row(int externalPadding, int internalPadding, int numColumn) {
//			this.externalPadding = COLUMN_PADDING + externalPadding;
//			this.internalPadding = internalPadding;
//			setNumColumn(numColumn);
//		}
//
//		private void setNumColumn(int numColumn) {
//			columnWidth = (1 - externalPadding) / numColumn;
//		}
//
//		private double getColumnX(int column) {
//			return externalPadding + columnWidth * column + internalPadding;
//		}
//
//		private double getColumnWidth(int ncolumn) {
//			return columnWidth * ncolumn - internalPadding * 2;
//		}
//	}
//
//	class Element {
//		private int rowy, columnx, rowHeight, columnWidth;
//		private double x, y, width, height, rightx, bottomy;
//		private final static int STATE_UP = 0, STATE_DOWN = 1, STATE_HOVER = 2;
//		private int state;
//		private String text;
//
//		Element(String text, int rowy, int columnx, int rowHeight, int columnWidth) {
//			this.text = text;
//			setCoordinates(rowy, columnx, rowHeight, columnWidth);
//		}
//
//		private void setCoordinates(int rowy, int columnx, int rowHeight, int columnWidth) {
//			this.rowy = rowy;
//			this.columnx = columnx;
//			this.rowHeight = rowHeight;
//			this.columnWidth = columnWidth;
//			x = row[rowy].getColumnX(columnx);
//			y = getRowY(rowy);
//			width = row[rowy].getColumnWidth(columnWidth);
//			height = getRowHeight(rowHeight);
//			rightx = x + width;
//			bottomy = y + height;
//		}
//
//		private void react(Controller controller) {
//			double[] mousexy = controller.getMouseLocation();
//			if (mousexy[0] > x && mousexy[0] < rightx && mousexy[1] > y && mousexy[1] < bottomy)
//				if (controller.isMouseDown())
//					state = STATE_DOWN;
//				else
//					state = STATE_HOVER;
//			else
//				state = STATE_UP;
//		}
//
//		private void paint(Painter painter) {
//			double[] xs = new double[] {x - .5, x - .5, rightx - .5, rightx - .5};
//			double[] ys = new double[] {y - .5, bottomy - .5, bottomy - .5, y - .5};
//			Color color;
//			switch (state) {
//				case STATE_DOWN:
//					color = SELECT_COLOR;
//					break;
//				case STATE_HOVER:
//					color = HOVER_COLOR;
//					break;
//				default:
//					color = NORMAL_COLOR;
//			}
//			painter.polygon(new double[][] {xs, ys}, 1, color, false);
//			painter.polygon(new double[][] {xs, ys}, 1, BORDER_COLOR, true);
//		}
//	}
//
//	private class LinkedElements {
//		private LinkedElements next, prev;
//		private Element element;
//
//		private LinkedElements(LinkedElements next, LinkedElements prev, Element element) {
//			this.next = next;
//			this.prev = prev;
//			this.element = element;
//		}
//	}
//
//	public static void main(String[] arg) {
//		int x = 250;
//		Controller controller = new Controller(x, x);
//		Painter painter = new Painter(x, x, controller);
//		SplitInterface si = new SplitInterface(8);
//		si.setNumColumn(0, 8);
//		Element el = si.new Element("woah dude", 0, 0, 1, 1);
//		si.addElement(el);
//		while (true) {
//			si.update(controller, painter);
//			painter.repaint();
//			Engine.wait(30);
//		}
//	}
//}
