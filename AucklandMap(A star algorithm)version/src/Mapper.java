import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * This is the main class for the mapping program. It extends the GUI abstract
 * class and implements all the methods necessary, as well as having a main
 * function.
 *
 * @author tony
 */
public class Mapper extends GUI {
	public static final Color NODE_COLOUR = new Color(77, 113, 255);
	public static final Color SEGMENT_COLOUR = new Color(130, 130, 130);
	public static final Color HIGHLIGHT_COLOUR = new Color(255, 219, 77);

	// these two constants define the size of the node squares at different zoom
	// levels; the equation used is node size = NODE_INTERCEPT + NODE_GRADIENT *
	// log(scale)
	public static final int NODE_INTERCEPT = 1;
	public static final double NODE_GRADIENT = 0.8;

	// defines how much you move per button press, and is dependent on scale.
	public static final double MOVE_AMOUNT = 100;
	// defines how much you zoom in/out per button press, and the maximum and
	// minimum zoom levels.
	public static final double ZOOM_FACTOR = 1.3;
	public static final double MIN_ZOOM = 1, MAX_ZOOM = 200;

	// how far away from a node you can click before it isn't counted.
	public static final double MAX_CLICKED_DISTANCE = 0.15;

	// these two define the 'view' of the program, ie. where you're looking and
	// how zoomed in you are.
	private Location origin;
	private double scale;

	// our data structures.
	private Graph graph;
	private Trie trie;

	private boolean clickedNodeAStar = false;
	private double bestToGoal;
	private ArrayList<Segment> list;
	private Set<Node> articulationPoints = new HashSet<Node>();
	private Stack<APnode> apStack = new Stack<>();
	private boolean resetDraw = false;
	private String currentRoadName;
	private int currentRoadID;
	private String currentRoadCity;
	private boolean restrictionDetail = false;
	private int fromNodeID;
	private Integer turnNodeID;
	private int toNodeID;
	private Set<Node> unvisitedNodes;
	static boolean timeMode;
	
	public boolean isTimeMode() {
		return timeMode;
	}

	private static final boolean IntVersion = true;

	@Override
	protected void redraw(Graphics g) {
		if (graph != null)
			graph.draw(g, getDrawingAreaDimension(), origin, scale);
	}

	protected void timeModeOn() {
		if(this.isTimeMode()){this.setTimeMode(false);}

		else{this.setTimeMode(true);
		}

	}

	@Override
	protected void onClick(MouseEvent e) {
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		// find the closest node.
		double bestDist = Double.MAX_VALUE;
		Node closest = null;

		for (Node node : graph.nodes.values()) {
			double distance = clicked.distance(node.location);
			if (distance < bestDist) {
				bestDist = distance;
				closest = node;
			}
		}

		// if it's close enough, highlight it and show some information.
		if (clicked.distance(closest.location) < MAX_CLICKED_DISTANCE) {
			if (!clickedNodeAStar) {

				graph.setHighlight((Node) null);
				graph.setHighlight2((Node) null);

				graph.setHighlight(closest);
				getTextOutputArea().setText(closest.toString());
				// System.out.println("false");
				clickedNodeAStar = true;
			} else {

				graph.setHighlight2(closest);
				getTextOutputArea().setText(closest.toString());
				clickedNodeAStar = false;
				// System.out.println("true");

				// call pathfinder mathod
				Fringe f = null;
				if (isTimeMode() == false) {
					f = pathfinder(graph.getHighlightedNode(), graph.getHighlightedNodeEnd());
				} else {
					f = timePathFinder(graph.getHighlightedNode(), graph.getHighlightedNodeEnd());
				}
				List<Segment> displaySegs = new ArrayList<Segment>();
				displaySegs = getSegPath(f);
				graph.setPathSegs(displaySegs);
				String display = "";
				double totalDistance = 0;
				Map<String, Double> noDuplicateNames = new HashMap<String, Double>();

				for (Segment s : displaySegs) {
					if (s != null) {
						if (!noDuplicateNames.containsKey(s.road.name)) {
							noDuplicateNames.put(s.road.name, s.length);
						} else {
							double prevLength = noDuplicateNames.get(s.road.name);
							double newLength = prevLength + s.length;
							noDuplicateNames.put(s.road.name, newLength);
						}
						totalDistance += s.length;
					}
				}
				// make sure roads are showing in order
				List<String> inOrder = new ArrayList<>();
				for (int i = 0; i < displaySegs.size(); i++) {
					if (!inOrder.contains(displaySegs.get(i).road.name)) {
						inOrder.add(displaySegs.get(i).road.name);
					}
				}
				// display roads with lengths in order
				for (int i = inOrder.size() - 1; i >= 0; i--) {
					for (String s : noDuplicateNames.keySet()) {
						if (inOrder.get(i).equals(s)) {
							display += s + " : " + String.format("%.3f km", noDuplicateNames.get(s)) + "\n";
						}
					}
				}

				getTextOutputArea()
						.setText(display + "\n" + "TotalDistance = " + String.format("%.3f km", totalDistance) + "\n");
			}
		}
	}

	public void setTimeMode(boolean timeMode) {
		this.timeMode = timeMode;
	}

	private List<Segment> getSegPath(Fringe f) {
		list = new ArrayList<Segment>();
		Node goal = f.getNode();
		Node from = f.getFromNode();
		int i = 0;
		while (from != null) {
			// System.out.println(i++);
			// find path from goal node back to the start node
			Segment seg = goal.getSegWith(from);
			list.add(seg);
			Node temp = from;
			from = from.getPathFrom();
			goal = temp;
		}
		return list;
	}

	public int getSpeed(int index) {
		switch (index) {
		case 0:
			return 5;
		case 1:
			return 20;
		case 2:
			return 40;
		case 3:
			return 60;
		case 4:
			return 80;
		case 5:
			return 100;
		case 6:
			return 110;
		default:
			return 110;
		}
	}

	public String getRoadClassStr(int index) {
		switch (index) {
		case 0:
			return "Residential";
		case 1:
			return "Collector";
		case 2:
			return "Arterial";
		case 3:
			return "Principal HW";
		default:
			return "Major HW";
		}
	}

	private Fringe timePathFinder(Node nodeStart, Node nodeGoal) {
		System.out.println("Search by best time");
		double maxSpeed = 110;
		bestToGoal = Double.POSITIVE_INFINITY;
		// set every node length to the max value
		for (Node n : graph.nodes.values()) {
			n.setPathLength(Double.POSITIVE_INFINITY);
			n.setNeighbours();
		}

		Fringe f = new Fringe(nodeStart, null, 0, nodeStart.location.distance(nodeGoal.location) / maxSpeed);
		PriorityQueue<Fringe> fQueue = new PriorityQueue<Fringe>();
		fQueue.add(f);
		while (!fQueue.isEmpty()) {

			Fringe fOut = fQueue.poll();
			Node current = fOut.getNode();
			Node prev = fOut.getFromNode();
			double costToHere = fOut.getCostToHere();
			double totalCostToGoal = fOut.getTotalCostToGoal();
			Segment s = current.timeSeg();
			double speed = getSpeed(s.road.getSpeed());
			if (costToHere < (current.getPathLength() / speed)) {
				current.setPathFrom(prev);
				current.setPathLength(costToHere);
				if (current == nodeGoal) {
					// bestToGoal = costToHere;
					break;
				}
				Set<Node> neig = new HashSet<Node>();
				List<Node> list = current.getNeighbours();
				for (Node n : list) {
					neig.add(n);
				}
				for (Node n : neig) {
					Segment s1 = n.timeSeg();
					double speed1 = getSpeed(s1.road.getSpeed());
					// make sure we are not at the wrong direction of a one way
					// street
					if (rightDirOfOneWay(current, n) && restrictionRule(prev, current, n)) {

						double costToNeig = costToHere + (current.location.distance(n.location) / maxSpeed);
						if (costToNeig < (n.getPathLength() / speed1)) {
							double estTotal = costToNeig + (n.location.distance(nodeGoal.location) / maxSpeed);
							if (estTotal < bestToGoal) {
								f = new Fringe(n, current, costToNeig, estTotal);
								fQueue.add(f);

							}
							if (n == nodeGoal) {
								bestToGoal = costToNeig;
							}
						}
					} else {
						if (!restrictionDetail) {
							String detail = currentRoadID + "-" + currentRoadName + "-" + currentRoadCity + "-";
							//System.out.println(detail + " from " + current.nodeID + " to " + n.nodeID
								//	+ " is  the wrong direction of one way Street");

						} else {
						//	System.out.println("Can't turn from this street");
							restrictionDetail = false;

						}
					}
				}
			}

		}
		return f;
	}

	private Fringe pathfinder(Node nodeStart, Node nodeGoal) {
		System.out.println("Search by shortest distance");
		//System.out.println("distance");

		bestToGoal = Double.POSITIVE_INFINITY;
		// set every node length to the max value
		for (Node n : graph.nodes.values()) {
			n.setPathLength(Double.POSITIVE_INFINITY);
			n.setNeighbours();
		}

		Fringe f = new Fringe(nodeStart, null, 0, nodeStart.location.distance(nodeGoal.location));
		PriorityQueue<Fringe> fQueue = new PriorityQueue<Fringe>();
		fQueue.add(f);
		while (!fQueue.isEmpty()) {

			Fringe fOut = fQueue.poll();
			Node current = fOut.getNode();
			Node prev = fOut.getFromNode();
			double costToHere = fOut.getCostToHere();
			double totalCostToGoal = fOut.getTotalCostToGoal();

			if (costToHere < current.getPathLength()) {
				current.setPathFrom(prev);
				current.setPathLength(costToHere);
				if (current == nodeGoal) {
					// bestToGoal = costToHere;
					break;
				}
				Set<Node> neig = new HashSet<Node>();
				List<Node> list = current.getNeighbours();
				for (Node n : list) {
					neig.add(n);
				}
				for (Node n : neig) {
					// make sure we are not at the wrong direction of a one way
					// street
					if (rightDirOfOneWay(current, n) && restrictionRule(prev, current, n)) {

						double costToNeig = costToHere + current.location.distance(n.location);
						if (costToNeig < n.getPathLength()) {
							double estTotal = costToNeig + n.location.distance(nodeGoal.location);
							if (estTotal < bestToGoal) {
								f = new Fringe(n, current, costToNeig, estTotal);
								fQueue.add(f);

							}
							if (n == nodeGoal) {
								bestToGoal = costToNeig;
							}
						}
					} else {
						if (!restrictionDetail) {
							String detail = currentRoadID + "-" + currentRoadName + "-" + currentRoadCity + "-";
							//System.out.println(detail + " from " + current.nodeID + " to " + n.nodeID
							//		+ " is  the wrong direction of one way Street");

						} else {
							//System.out.println("Can't turn from this street");
							restrictionDetail = false;

						}
					}
				}
			}

		}
		return f;
	}

	private boolean restrictionRule(Node prev, Node current, Node to) {

		for (Integer i : graph.restri.keySet()) {
			if (prev != null && (graph.restri.get(i).getFromNode() == prev.getNodeID())
					&& (to.getNodeID() == graph.restri.get(i).getToNode()) && (i == current.getNodeID())) {
				//System.out.println("555555555555555");
				fromNodeID = prev.getNodeID();
				turnNodeID = i;
				toNodeID = to.getNodeID();
				restrictionDetail = true;
				//.out.println(fromNodeID + "-" + turnNodeID + "-" + toNodeID);
				return false;
			} else if (prev != null
					&& (graph.restri.get(i).getFromNode() == graph.restri.get(i).getToNode()
							&& prev.getNodeID() == to.getNodeID() && graph.restri.get(i).getToNode() == to.getNodeID())
					&& (i == current.getNodeID())) {
				//System.out.println("666666666666666");
				fromNodeID = prev.getNodeID();
				turnNodeID = i;
				toNodeID = to.getNodeID();
				restrictionDetail = true;
				//System.out.println(fromNodeID + "-" + turnNodeID + "-" + toNodeID);
				return false;
			}
		}
		return true;
	}

	private boolean rightDirOfOneWay(Node current, Node to) {
		for (Segment s : graph.segments) {

			if (s.getStart() == to && s.getEnd() == current && s.road.getOneway() == 1) {
				this.currentRoadName = s.road.name;
				this.currentRoadID = s.road.roadID;
				this.currentRoadCity = s.road.city;
				return false;
			}
		}

		return true;
	}

	protected void ArtPointAll() {
		if (!resetDraw) {
			unvisitedNodes = new HashSet<Node>();
			for (Node n : graph.nodes.values()) {
				unvisitedNodes.add(n);
			}
			while (!unvisitedNodes.isEmpty()) {
				ArtPoint();
			}
			getTextOutputArea().setText("The total articulation points is :" + articulationPoints.size());
		}

		else {
			// System.out.println(resetDraw);
			articulationPoints.clear();
			resetDraw = false;
			getTextOutputArea().setText("");
			redraw();
		}
	}

	protected void ArtPoint() {
		boolean pickStartNode = true;
		Node startNode = null;
		int numSubtrees = 0;

		for (Node n : unvisitedNodes) {
			n.setDepth(Double.POSITIVE_INFINITY);
			if (pickStartNode == true) {
				startNode = n;
				pickStartNode = false;
			}
		}
		startNode.setDepth(0);
		if (unvisitedNodes.contains(startNode)) {
			unvisitedNodes.remove(startNode);
		}
		for (Node n : startNode.getNeighbours()) {
			if (n.getDepth() == Double.POSITIVE_INFINITY) {
				if (unvisitedNodes.contains(n)) {
					unvisitedNodes.remove(n);
				}
				if (!IntVersion) {
					recArtPts(n, 1, startNode);
					System.out.println("Rec");
				} else {
					iterArtPts(n, startNode);
					System.out.println("Int");

				}
				numSubtrees++;
			}
		}
		if (numSubtrees > 1) {
			articulationPoints.add(startNode);
		}
		graph.setArtPoints(articulationPoints);
		// System.out.println(resetDraw);

		resetDraw = true;

	};

	private void iterArtPts(Node firstNode, Node root) {
		APnode ap = new APnode(firstNode, 1, new APnode(root, 0, null));
		apStack.push(ap);
		while (!apStack.isEmpty()) {
			APnode element = apStack.peek();
			Node node = element.getNode();
			if (element.getChildren() == null) {
				node.setDepth(element.getDepth());
				element.setReach(element.getDepth());
				Queue<Node> children = new ArrayDeque<Node>();
				element.setChildren(children);
				for (Node n : node.getNeighbours()) {
					if (unvisitedNodes.contains(n)) {
						unvisitedNodes.remove(n);
					}
					if (n != element.getParent().getNode()) {
						element.getChildren().offer(n);
					}
				}
			} else if (!element.getChildren().isEmpty()) {
				Node child = element.getChildren().poll();
				if (child.getDepth() < Double.POSITIVE_INFINITY) {
					element.setReach(Math.min(element.getReach(), child.getDepth()));
				} else {
					apStack.push(new APnode(child, node.getDepth() + 1, element));
				}
			} else {
				if (node != firstNode) {
					if (element.getReach() >= element.getParent().getDepth()) {
						articulationPoints.add(element.getParent().getNode());
					}
					element.getParent().setReach(Math.min(element.getParent().getReach(), element.getReach()));
				}
				apStack.pop();
			}
		}
	}

	private int recArtPts(Node current, int depth, Node from) {
		current.setDepth(depth);
		int reachBack = depth;
		for (Node n : current.getNeighbours()) {
			if (unvisitedNodes.contains(n)) {
				unvisitedNodes.remove(n);
			}
			if (n != from) {
				if (n.getDepth() < Double.POSITIVE_INFINITY) {
					reachBack = (int) Math.min(n.getDepth(), reachBack);
				} else {
					int childReach = recArtPts(n, depth + 1, current);
					if (childReach >= depth) {
						articulationPoints.add(current);
					}
					reachBack = Math.min(childReach, reachBack);
				}
			}
		}
		return reachBack;
	}

	@Override
	protected void onSearch() {
		if (trie == null)
			return;

		// get the search query and run it through the trie.
		String query = getSearchBox().getText();
		Collection<Road> selected = trie.get(query);

		// figure out if any of our selected roads exactly matches the search
		// query. if so, as per the specification, we should only highlight
		// exact matches. there may be (and are) many exact matches, however, so
		// we have to do this carefully.
		boolean exactMatch = false;
		for (Road road : selected)
			if (road.name.equals(query))
				exactMatch = true;

		// make a set of all the roads that match exactly, and make this our new
		// selected set.
		if (exactMatch) {
			Collection<Road> exactMatches = new HashSet<>();
			for (Road road : selected)
				if (road.name.equals(query))
					exactMatches.add(road);
			selected = exactMatches;
		}

		// set the highlighted roads.
		graph.setHighlight(selected);

		// now build the string for display. we filter out duplicates by putting
		// it through a set first, and then combine it.
		Collection<String> names = new HashSet<>();
		for (Road road : selected)
			names.add(road.name);
		String str = "";
		for (String name : names)
			str += name + "; ";

		if (str.length() != 0)
			str = str.substring(0, str.length() - 2);
		getTextOutputArea().setText(str);
	}

	@Override
	protected void onMove(Move m) {
		if (m == GUI.Move.NORTH) {
			origin = origin.moveBy(0, MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.SOUTH) {
			origin = origin.moveBy(0, -MOVE_AMOUNT / scale);
		} else if (m == GUI.Move.EAST) {
			origin = origin.moveBy(MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.WEST) {
			origin = origin.moveBy(-MOVE_AMOUNT / scale, 0);
		} else if (m == GUI.Move.ZOOM_IN) {
			if (scale < MAX_ZOOM) {
				// yes, this does allow you to go slightly over/under the
				// max/min scale, but it means that we always zoom exactly to
				// the centre.
				scaleOrigin(true);
				scale *= ZOOM_FACTOR;
			}
		} else if (m == GUI.Move.ZOOM_OUT) {
			if (scale > MIN_ZOOM) {
				scaleOrigin(false);
				scale /= ZOOM_FACTOR;
			}
		}
	}

	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons, File restri) {
		graph = new Graph(nodes, roads, segments, polygons, restri);
		trie = new Trie(graph.roads.values());
		origin = new Location(-250, 250); // close enough
		scale = 1;
	}

	/**
	 * This method does the nasty logic of making sure we always zoom into/out
	 * of the centre of the screen. It assumes that scale has just been updated
	 * to be either scale * ZOOM_FACTOR (zooming in) or scale / ZOOM_FACTOR
	 * (zooming out). The passed boolean should correspond to this, ie. be true
	 * if the scale was just increased.
	 */
	private void scaleOrigin(boolean zoomIn) {
		Dimension area = getDrawingAreaDimension();
		double zoom = zoomIn ? 1 / ZOOM_FACTOR : ZOOM_FACTOR;

		int dx = (int) ((area.width - (area.width * zoom)) / 2);
		int dy = (int) ((area.height - (area.height * zoom)) / 2);

		origin = Location.newFromPoint(new Point(dx, dy), origin, scale);
	}

	public static void main(String[] args) {
		new Mapper();
	}
}

// code for COMP261 assignments