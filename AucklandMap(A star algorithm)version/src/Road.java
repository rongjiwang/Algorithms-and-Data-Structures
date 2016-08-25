import java.util.Collection;
import java.util.HashSet;

/**
 * Road represents ... a road ... in our graph, which is some metadata and a
 * collection of Segments. We have lots of information about Roads, but don't
 * use much of it.
 * 
 * @author tony
 */
public class Road {
	public final int roadID;
	public final String name, city;
	public final Collection<Segment> components;
	private int oneway;
	private int speed;

	public Road(int roadID, int type, String label, String city, int oneway, int speed, int roadclass, int notforcar,
			int notforpede, int notforbicy) {
		this.speed = speed;
		this.roadID = roadID;
		this.city = city;
		this.name = label;
		this.components = new HashSet<Segment>();
		this.oneway = oneway;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public void addSegment(Segment seg) {
		components.add(seg);
	}

	public int getOneway() {
		return oneway;
	}

}

// code for COMP261 assignments