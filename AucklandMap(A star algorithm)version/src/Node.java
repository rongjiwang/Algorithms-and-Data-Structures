import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Node represents an intersection in the road graph. It stores its ID and its
 * location, as well as all the segments that it connects to. It knows how to
 * draw itself, and has an informative toString method.
 *
 * @author tony
 */
public class Node {

	public final int nodeID;
	public final Location location;
	public Collection<Segment> segments;
	private double pathLength;
	public Node pathFrom;
	private double depth;
	private int NumSubtrees;
	private boolean artShape = false;
	public List<Node> list;

	public Node(int nodeID, double lat, double lon) {
		this.nodeID = nodeID;
		this.location = Location.newFromLatLon(lat, lon);
		this.segments = new HashSet<Segment>();
		list = new ArrayList<Node>();

	}

	public int getNodeID() {
		return nodeID;
	}

	public Node getPathFrom() {
		return pathFrom;
	}

	public Segment timeSeg() {
		for (Segment s : segments) {
			if (s.getEnd() == this || s.getStart() == this) {
				return s;
			}
		}
		return null;
	}

	public void setPathFrom(Node pathFrom) {
		this.pathFrom = pathFrom;
	}

	public void addSegment(Segment seg) {
		segments.add(seg);
	}

	public void draw(Graphics g, Dimension area, Location origin, double scale) {
		Point p = location.asPoint(origin, scale);

		// for efficiency, don't render nodes that are off-screen.
		if (p.x < 0 || p.x > area.width || p.y < 0 || p.y > area.height)
			return;
		if (artShape == false) {
			int size = (int) (Mapper.NODE_GRADIENT * Math.log(scale) + Mapper.NODE_INTERCEPT);
			g.fillRect(p.x - size / 2, p.y - size / 2, size, size);
		} else {
			int size = 4;
			g.drawOval(p.x - size / 2, p.y - size / 2, size, size);
		}
	}

	public String toString() {
		Set<String> edges = new HashSet<String>();
		for (Segment s : segments) {
			if (!edges.contains(s.road.name))
				edges.add(s.road.name);
		}

		String str = "ID: " + nodeID + "  loc: " + location + "\nroads: ";
		for (String e : edges) {
			str += e + ", ";
		}
		return str.substring(0, str.length() - 2);
	}

	public void setPathLength(double pathLength) {
		this.pathLength = pathLength;

	}

	public double getPathLength() {
		return pathLength;
	}

	public void setNeighbours() {
		for (Segment s : segments) {
			if (s.start == this) {
				list.add(s.end);
			}
			if (s.end == this) {
				list.add(s.start);
			}
		}
	}

	public List<Node> getTempNeighbours() {
		return this.list;
	}

	public List<Node> getNeighbours() {
		// System.out.println("+++");
		List<Node> n = new ArrayList<Node>();
		for (Segment s : segments) {
			if (s.start == this) {
				n.add(s.end);
			}
			if (s.end == this) {
				n.add(s.start);
			}
		}
		return n;
	}

	public Segment getSegWith(Node from) {
		for (Segment s : segments) {
			if (s.start == this && s.end == from) {
				return s;
			}
			if (s.end == this && s.start == from) {
				return s;
			}
		}
		return null;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	public double getDepth() {
		return this.depth;
	}

	public void setNumSubtrees(int i) {
		this.NumSubtrees = i;
	}

	public int getNumSubtrees() {
		return NumSubtrees;
	}

	public void setArtShape(boolean artShape) {
		this.artShape = artShape;
	}
}

// code for COMP261 assignments