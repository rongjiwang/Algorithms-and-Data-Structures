import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This represents the data structure storing all the roads, nodes, and
 * segments, as well as some information on which nodes and segments should be
 * highlighted.
 *
 * @author tony
 */
public class Graph {
	// map node IDs to Nodes.
	Map<Integer, Node> nodes = new HashMap<>();
	// map road IDs to Roads.
	Map<Integer, Road> roads;
	// just some collection of Segments.
	Collection<Segment> segments;
	Map<Integer, Restriction> restri;

	Node highlightedNode;
	Node highlightedNodeEnd;

	Collection<Road> highlightedRoads = new HashSet<>();
	private List<Segment> segPaths = new ArrayList<>();
	private Set<Node> artSet = new HashSet<>();;

	public Graph(File nodes, File roads, File segments, File polygons, File restri) {
		this.nodes = Parser.parseNodes(nodes, this);
		this.roads = Parser.parseRoads(roads, this);
		this.segments = Parser.parseSegments(segments, this);
		this.restri = Parser.parseRestriction(restri, this);
	}

	public void draw(Graphics g, Dimension screen, Location origin, double scale) {
		// a compatibility wart on swing is that it has to give out Graphics
		// objects, but Graphics2D objects are nicer to work with. Luckily
		// they're a subclass, and swing always gives them out anyway, so we can
		// just do this.
		Graphics2D g2 = (Graphics2D) g;

		// draw all the segments.
		g2.setColor(Mapper.SEGMENT_COLOUR);
		for (Segment s : segments)
			s.draw(g2, origin, scale);

		// draw the segments of all highlighted roads.
		g2.setColor(Mapper.HIGHLIGHT_COLOUR);
		g2.setStroke(new BasicStroke(3));
		for (Road road : highlightedRoads) {
			for (Segment seg : road.components) {
				seg.draw(g2, origin, scale);
			}
		}

		// draw all the nodes.
		g2.setColor(Mapper.NODE_COLOUR);
		for (Node n : nodes.values())
			n.draw(g2, screen, origin, scale);

		// draw the highlighted node, if it exists.
		if (highlightedNode != null) {
			g2.setColor(Mapper.HIGHLIGHT_COLOUR);
			highlightedNode.draw(g2, screen, origin, scale);
		}

		if (highlightedNodeEnd != null) {
			g2.setColor(Mapper.HIGHLIGHT_COLOUR);
			highlightedNodeEnd.draw(g2, screen, origin, scale);
		}

		g2.setColor(Color.BLACK);
		for (Segment s : segPaths) {
			// System.out.println("draw here?");
			if (s != null) {
				s.draw(g2, origin, scale);
			}
		}

		Random random = new Random();
		for (Node n : artSet) {
			float red = random.nextFloat();
			float green = random.nextFloat();
			float blue = random.nextFloat();
			Color randomColor = new Color(red, green, blue);
			if (n != null) {
				n.setArtShape(true);
				// System.out.println("drawing records");
				g2.setColor(randomColor);
				n.draw(g2, screen, origin, scale);
				n.setArtShape(false);

			}

		}
	}

	public void setArtPoints(Set<Node> artSet) {
		this.artSet = artSet;
	}

	public void setPathSegs(List<Segment> list) {
		this.segPaths = list;
	}

	public void setHighlight(Node node) {
		this.highlightedNode = node;
	}

	public void setHighlight2(Node node) {
		this.highlightedNodeEnd = node;
	}

	public Node getHighlightedNode() {
		return highlightedNode;
	}

	public Node getHighlightedNodeEnd() {
		return highlightedNodeEnd;
	}

	public void setHighlight(Collection<Road> roads) {
		this.highlightedRoads = roads;
	}
}

// code for COMP261 assignments