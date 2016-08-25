import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;


public class Road {
	private int ID;
	private int type;
	private String RoadName;
	private String city;
	private boolean oneway = false;
	private int speed;
	private int roadClass;
	private boolean NoCar = false;
	private boolean NoPed = false;
	private boolean NoBic = false;
	private Set<Segment> segmentSet = new HashSet<Segment>();
	
public Road(String line){
	//store data into array
	String[] elements = line.split("\t");
	this.ID = Integer.parseInt(elements[0]);
	this.type = Integer.parseInt(elements[1]);
	this.RoadName = elements[2];
	this.city = elements[3];
	this.oneway = (elements[4].equals("1")) ? true : false;
	this.speed = Integer.parseInt(elements[5]);
	this.roadClass  = Integer.parseInt(elements[6]);
	this.NoCar = (elements[7].equals("1")) ? true: false;
	this.NoPed = (elements[8].equals("1")) ? true: false;
	this.NoBic = (elements[9].equals("1")) ? true: false;
	//System.out.println(ID+" "+type+" "+RoadName+" "+city+" "+speed+" "+roadClass);
}
   public int getID(){
		return this.ID;
	
}
   public String getName(){
	   return RoadName;
   }
   public String getCity(){
	   return this.city;
   }
   
 public void SegmentSet(Segment segment){
	 segmentSet.add(segment);
 }
 
 public void drawRoad(Graphics g, Location origin, double scale){
	 for(Segment s: segmentSet){
		 s.draw(g, origin, scale);
	 }
 }
 public String toString(){
	 return "RoadID: "+ID+" RoadName: "+RoadName+" city: "+city+"\n";
 }

}
