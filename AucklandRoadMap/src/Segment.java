import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;


public class Segment {
	
	private Location location;
	private Integer ID;
	private double length;
	private Integer nodeStart;
	private Integer nodeEnd;
	
	private List<Location> drawLocation;
	private Queue<String> segData;
	
	
	public Segment(String line){
		drawLocation = new ArrayList<Location>();
		segData = new ArrayDeque<String>();
		//store data into array
		String[] elements = line.split("\t");
		//store array into queue
		for(int i=0; i<elements.length; i++){
			segData.offer(elements[i]);
		}
		ID = Integer.parseInt(segData.poll());
		length = Double.parseDouble(segData.poll());
		nodeStart = Integer.parseInt(segData.poll());
		nodeEnd= Integer.parseInt(segData.poll());
		//store all the coords into another queue data structure
		while(!segData.isEmpty()){
			location = Location.newFromLatLon(Double.parseDouble(segData.poll()), Double.parseDouble(segData.poll()));
			//System.out.println(location.x+" "+location.y+" upload");
			drawLocation.add(location);
		}
		}

	public int nodeStartID(){
		return nodeStart;
	}
	public int nodeEndID(){
		return nodeEnd;
	}
	public int getRoadID(){
		return this.ID;
	}

		
	public void draw(Graphics g, Location origin, double scale){
			//take the start drawing x & y
			Point drawPointStart = drawLocation.get(0).asPoint(origin, scale);
			//System.out.println(drawPointStart.getX()+" "+ drawPointStart.getX()+ " Point XY");
			//draw line between neighbour coords
			for(int i=1; i<drawLocation.size();i++){
				Point drawPointEnd = drawLocation.get(i).asPoint(origin, scale);
				g.drawLine(drawPointStart.x, drawPointStart.y, drawPointEnd.x,drawPointEnd.y);
				//System.out.println(drawPointStart.getX()+" "+drawPointStart.getY()+" "+drawPointEnd.getX()+" "+drawPointEnd.getY());
				drawPointStart = drawPointEnd;
			}
		
	}
}
