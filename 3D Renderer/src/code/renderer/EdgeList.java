package code.renderer;

import java.util.HashMap;
import java.util.Map;

/**
 * EdgeList should store the data for the edge list of a single polygon in your
 * scene. A few method stubs have been provided so that it can be tested, but
 * you'll need to fill in all the details.
 *
 * You'll probably want to add some setters as well as getters or, for example,
 * an addRow(y, xLeft, xRight, zLeft, zRight) method.
 */
public class EdgeList {
	private int startY;
	private int endY;
	private Map<Integer, EdgeData> map = new HashMap<>();

	public EdgeList(int startY, int endY) {
		this.startY = startY;
		this.endY = endY;
	}

	public void addRow(int y, float xLeft, float zLeft, float xRight, float zRight) {
		map.put(y, new EdgeData(xLeft, zLeft, xRight, zRight));
	}

	public int getStartY() {
		return this.startY;
	}

	public int getEndY() {
		return this.endY;
	}

	public float getLeftX(int y) {
		for (Integer i : map.keySet()) { // get Y level value from HashMap
			if (i == y) {
				return map.get(i).getxLeft();
			}
		}
		return 0;
	}

	public float getRightX(int y) {
		for (Integer i : map.keySet()) {
			if (i == y) {
				return map.get(i).getxRight();
			}
		}
		return 0;
	}

	public float getLeftZ(int y) {
		for (Integer i : map.keySet()) {
			if (i == y) {
				return map.get(i).getzLeft();
			}
		}
		return 0;
	}

	public float getRightZ(int y) {
		for (Integer i : map.keySet()) {
			if (i == y) {
				return map.get(i).getzRight();
			}
		}
		return 0;
	}
}

// code for comp261 assignments
