import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class AucklandMap extends GUI {
	
	
	private Map<Integer, Node> StoreNode;
	private Map<Integer, Road> StoreRoad;
	private Map<Integer,Segment> StoreSeg;
	private Set<Road> highLightRoad;
	private Node highLight = null;
	private TrieNode tNode = new TrieNode();
	Set<String> roadNames;



	private Location location;
	private double CENTRE_LAT = -36.847622;
	private double CENTRE_LON = 174.763444;
	private double scale = 100;
	private Location origin = Location.newFromLatLon(CENTRE_LAT, CENTRE_LON);
	private int pressX;
	private int pressY;
	private List<Road> trieRoads;


	public AucklandMap(){
		StoreNode = new HashMap<Integer,Node>();
		StoreRoad = new HashMap<Integer,Road>();
		StoreSeg = new HashMap<Integer,Segment>();
		highLightRoad = new HashSet<Road>();
		trieRoads = new ArrayList<Road>();

	}
	
	private void draw(Graphics g){   //nodes draws
		g.setColor(Color.BLUE);
		for(Node n: StoreNode.values()){
			n.drawNode(g, origin, scale);
			}
		g.setColor(Color.green);      // roads draws
		for(Road r: StoreRoad.values()){
			r.drawRoad(g, origin, scale);
		}
		g.setColor(Color.red);         // intersection highlight draw
		if(highLight!=null)
			highLight.drawNode(g, origin, scale);
		
		g.setColor(Color.DARK_GRAY);   // core part search draws
		if(!highLightRoad.isEmpty()){
			for(Road r: highLightRoad)
			r.drawRoad(g, origin, scale);
					}
		g.setColor(Color.RED);   //Trie road search draws
		 for(Road r: trieRoads)
			 r.drawRoad(g, origin, scale);
				
			
		
	}	
	//***read node file class***
	private void ReadNode(File nodes){
		try{
			//Create a reader, read the File comes in
		BufferedReader buff = new BufferedReader(new FileReader(nodes));
		String line = null;
			//keep reading until none data(line) left
		while((line = buff.readLine()) != null){
			//split them into a array
			String[] elements = line.split("\t");
			//create node location and store in HashMap
			int ID = Integer.parseInt(elements[0]);
			location = Location.newFromLatLon(Double.parseDouble(elements[1]), Double.parseDouble(elements[2]));
			StoreNode.put(ID,new Node(ID, location));

		}
		buff.close();
		}
		catch(IOException e){System.out.println("NodeFile reading failed"+e);}
	}
	//***read road file class***
	private void ReadRoad(File roads){
		
		try{
			BufferedReader buff = new BufferedReader(new FileReader(roads));
			// skip the first line
			String line = buff.readLine();
			while((line = buff.readLine()) !=null){
					Road road = new Road(line);
					//store road into map
					StoreRoad.put(road.getID(), road);
					tNode.addRoad(road);
			}
			buff.close();
			
		}
		catch(IOException e){System.out.println("RoadFile reading failed"+e);}
	}
	//***read segment file class***
	private void ReadSegment(File segments){
			try{
				BufferedReader buff = new BufferedReader(new FileReader(segments));
				//skip the first line
				String line = buff.readLine();
				while((line = buff.readLine()) != null){
					Segment segment = new Segment(line);
					StoreSeg.put(segment.getRoadID(), segment);
					//if segment has a mother road
					if(StoreRoad.containsKey(segment.getRoadID())){
						//store the segment into Road class(segments build road)
						StoreRoad.get(segment.getRoadID()).SegmentSet(segment);
					}
					if( StoreNode.containsKey(segment.nodeStartID()) && StoreNode.containsKey(segment.nodeEndID())){
						StoreNode.get(segment.nodeStartID()).addSegment(segment);
						StoreNode.get(segment.nodeEndID()).addSegment(segment);
					}
					
				}
				
				buff.close();
			}
			catch(IOException e){System.out.println("Segments File Reading Failed");}
		
	}

	@Override
	protected void redraw(Graphics g) {
		draw(g);
	}

	@Override
	protected void onClick(MouseEvent e) {   //display a closest intersection
			Node nearByNode = null;
		double closestDistance = Double.MAX_VALUE;
		double diff;
		Location clicked = Location.newFromPoint(e.getPoint(), origin, scale);
		//shortest distance to user clicking point
		for(Node node: StoreNode.values()){
			diff = clicked.distance(node.getLocation());
			if(diff < closestDistance){
				closestDistance = diff;
				nearByNode = node;
			}
		}
		nearByNode.printDetails(origin, scale, StoreRoad, getTextOutputArea());
		highLight = nearByNode;
		
	}
	@Override
	protected void onSearch() {
		//check name,and highlight the road with details
//		for(Road road: StoreRoad.values()){
//			if(road.getName().equals(name)){
//				for(Segment s: StoreSeg.values()){
//					if(s.getRoadID() == road.getID()){              *Core Part*
//						highLightRoad.add(road);
//						data+= road.toString();
//					}
//				}
//			}
//		}
//		getTextOutputArea().setText(data);
		
		String name = getSearchBox().getText();
		String data = "";
		roadNames = new HashSet<String>();

		if (name.length() > 0) {
			TrieNode node = tNode.searchNode(name);                  /*Completion part*/
			if (node != null) {
				trieRoads = node.getRoads();
				for (Road road : trieRoads) {
					if (!roadNames.contains(road.getName())) {
						data+=" *|* " + road.getName();
						//Store road names 
						roadNames.add(road.getName());
					}
				}
				//print all road names per every entered char
				getTextOutputArea().setText(data);
			} else
				trieRoads.clear();
		}
	}
	@Override
	protected void onPress(MouseEvent e){
		pressX = e.getX();
		pressY = e.getY();
	}
	
	@Override
	protected void onDrag(MouseEvent e){
		origin = origin.moveBy((pressX-e.getX())/scale, (e.getY()-pressY)/scale);
		pressX = e.getX();
		pressY = e.getY();
	}
	
	@Override
	protected void onScroll(MouseWheelEvent e){
		double zoomRate = 1.1;
		int scrollCount = e.getWheelRotation();
		if(scrollCount > 0){
			for(int i=0;i<scrollCount;i++){
				this.scale = scale/=zoomRate;
			}
		}
		else if(scrollCount < 0){
			for(int i=0;i>scrollCount;i--){
				this.scale = scale*=zoomRate;
						}
		}
	}

	@Override
	protected void onMove(Move m) {       //button movements
		int moveRate = 3;
		double zoomRate = 1.1;
			if(m.equals(Move.NORTH)) this.origin = new Location(origin.x, origin.y+moveRate);
			if(m.equals(Move.SOUTH)) this.origin = new Location(origin.x, origin.y-moveRate);
			if(m.equals(Move.EAST)) this.origin = new Location(origin.x+moveRate, origin.y);
			if(m.equals(Move.WEST)) this.origin = new Location(origin.x-moveRate, origin.y);
			if(m.equals(Move.ZOOM_IN)) this.scale = scale*=zoomRate;
			if(m.equals(Move.ZOOM_OUT)) this.scale = scale/=zoomRate;

		}
	
	@Override
	protected void onLoad(File nodes, File roads, File segments, File polygons) {
		ReadNode(nodes);
		ReadRoad(roads);
		ReadSegment(segments);
		
	}
	public static void main(String[] args){
			new AucklandMap();
		
	}
}
