package draw.manage;

import list.ListItem;

public interface Manageable extends ListItem {

	abstract void setPosition(double x, double y);

	abstract void setWidth(double width);

	abstract void setHeight(double height);

	abstract double getWidth();
}
