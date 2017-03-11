package draw.elements;

import draw.manage.Interactable;
import draw.manage.Manageable;
import draw.painter.Painter;

import java.awt.*;

public class ActionList extends Draw implements Manageable, Interactable {

	private String[] text;
	private int fontSize;

	private double x0, y0, width, height;
	private int lineWidth;
	private Color color;
	private boolean fill;

	private int highlight; // -1 = none, -2 = scroll
	private int selected;
	private Color highlightColor;
	private Color selectedColor;

	private boolean scrolling;
	private double scroll;
	private double maxScroll;
	private double heightEach;
	private double scrollWidth, scrollHeight;

	public ActionList(String[] text, int fontSize, double heightEach,
			double scrollWidth, double x0, double y0, double width, double height,
			int lineWidth, Color color, Color highlightColor,
			Color selectedColor, boolean fill) {
		this.text = text;
		this.fontSize = fontSize;
		this.x0 = x0;
		this.y0 = y0;
		this.width = width;
		setHeight(height);
		this.lineWidth = lineWidth;
		this.color = color;
		this.highlightColor = highlightColor;
		this.selectedColor = selectedColor;
		this.fill = fill;
		highlight = -1;
		selected = -1;
		this.heightEach = heightEach;
		this.scrollWidth = scrollWidth;
	}

	public void setPosition(double x, double y) {
		x0 = x;
		y0 = y;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
		maxScroll = heightEach * text.length - height;
		if (maxScroll < 0)
			maxScroll = 0;
		scrollHeight = height * height / (heightEach * text.length);
		if (scrollHeight > height)
			scrollHeight = height;
	}

	public double getWidth() {
		return width;
	}

	public void mouseOver(double x, double y) {
		if (scrolling && height != scrollHeight) {
			scroll = maxScroll * (y - y0 - scrollHeight / 2)
					/ (height - scrollHeight);
			if (scroll < 0)
				scroll = 0;
			if (scroll > maxScroll)
				scroll = maxScroll;
		} else {
			double scrollTop = getScrollTop();
			if (x > x0 + width - scrollWidth && x < x0 + width && y > scrollTop
					&& y < scrollTop + scrollHeight)
				highlight = -2;
			else if (y > y0 && y < y0 + height) {
				highlight = (int) ((y - y0) / heightEach + scroll / heightEach);
				if (highlight < 0)
					highlight = -1;
				if (highlight > text.length - 1)
					highlight = -1;
			} else
				highlight = -1;
		}
	}

	public void mousePress() {
		if (highlight >= 0)
			selected = highlight;
		if (highlight == -2)
			scrolling = true;
	}

	public void mouseRelease() {
		scrolling = false;
	}

	public int getSelected() {
		if (selected >= 0) {
			return selected;
		}
		return -1;
	}

	public void resetText(String[] text, int selected) {
		this.text = text;
		this.selected = selected;
		setHeight(height);
		if (scroll > maxScroll)
			scroll = maxScroll;
	}

	private double getScrollTop() {
		if (maxScroll == 0)
			return y0;
		return y0 + scroll / maxScroll * (height - scrollHeight);
	}

	public void paint(Painter painter) {
		double mainWidth = width - scrollWidth;
		double scrollTop = getScrollTop();
		if (highlight == -2) {
			painter.drawRectangle(x0 + mainWidth, scrollTop, scrollWidth,
					scrollHeight, lineWidth, Color.GRAY, true);
		} else
			painter.drawRectangle(x0 + mainWidth, scrollTop, scrollWidth,
					scrollHeight, lineWidth, Color.LIGHT_GRAY, true);
		double y, shrunky, shrunkheight;
		int start = (int) (scroll / heightEach);
		int tlength = text.length;
		if (tlength > height / heightEach + 1)
			tlength = (int) (height / heightEach + 1);
		for (int i = start; i < start + tlength; i++) {
			y = y0 - scroll + i * heightEach;
			shrunky = y;
			shrunkheight = heightEach;
			if (shrunky < y0) {
				shrunkheight -= y0 - shrunky;
				shrunky = y0;
			}
			if (shrunky + shrunkheight > y0 + height) {
				shrunkheight = y0 + height - shrunky;
			}
			if (selected == i)
				painter.drawRectangle(x0, shrunky, mainWidth, shrunkheight,
						lineWidth, selectedColor, fill);
			else if (highlight == i)
				painter.drawRectangle(x0, shrunky, mainWidth, shrunkheight,
						lineWidth, highlightColor, fill);
			else
				painter.drawRectangle(x0, shrunky, mainWidth, shrunkheight,
						lineWidth, color, fill);
			painter.drawRectangle(x0, shrunky, mainWidth, shrunkheight,
					lineWidth, Color.GRAY, false);
			if (shrunkheight > heightEach * .66f)
				if (shrunkheight > heightEach * .66f)
					if (selected == i)
						painter.drawCenteredText(text[i], fontSize,
								Color.WHITE, x0 + .02f, y, mainWidth,
								heightEach, false);
					else
						painter.drawCenteredText(text[i], fontSize,
								Color.BLACK, x0 + .02f, y, mainWidth,
								heightEach, false);
		}
		painter.drawLine(x0 + mainWidth, y0, x0 + mainWidth, y0 + height,
				lineWidth, Color.BLACK);
		painter.drawRectangle(x0, y0, width, height, lineWidth, Color.BLACK,
				false);
	}
}
