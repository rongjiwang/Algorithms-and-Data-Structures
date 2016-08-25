import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JTextArea;


public class Node {
	private Location location;
	private int ID;
	private int nodeSize = 2;
	private Set<Segment> linkSegs;
	private Set<String> roadNames;
	
	public Node(int ID, Location location){
			this.location = location;
			this.ID = ID;
			linkSegs = new HashSet<Segment>();
			roadNames = new HashSet<String>();

			}
	
	public int getID(){
		return this.ID;
		
	}
	public Location getLocation(){
		return this.location;
		
	}
	void addSegment(Segment seg){
		linkSegs.add(seg);
	}
	void printDetails(Location origin, double scale, Map<Integer, Road> StoreRoad, JTextArea jta){
		String outText = "Intersection (ID: " + ID+"): ";
		for(Segment seg : linkSegs){
			Road road = StoreRoad.get(seg.getRoadID());
			if(!roadNames.contains(road.getName())){
			roadNames.add(road.getName());
			outText += road.getName()+"  *|*  ";
			}
		}
		jta.setText(null);
		jta.setText(outText);
	}

	//draw the node
	void drawNode(Graphics g, Location origin, double scale){
		Point point = location.asPoint(origin,scale);
		// draw from the left top spot
		g.fillRect(point.x - nodeSize/2, point.y - nodeSize/2, nodeSize, nodeSize);
		
	}
	
}
