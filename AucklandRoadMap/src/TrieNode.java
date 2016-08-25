
import java.util.*;


public class TrieNode {

	private Map<Character,TrieNode> children;
	private List<Road> roads;

	
	public TrieNode(){
		children = new HashMap<Character, TrieNode>();
		roads = new ArrayList<Road>();

	}
	public void addRoad(Road road){
		TrieNode node = this;
		//Trie tree builder
		for (Character c : road.getName().toCharArray()){
			if (node.children.containsKey(c)){
				node = node.children.get(c);
			} else {
				node = node.addNode(c, node);
			}
		}
		node.roads.add(road);
	}
	
	public TrieNode addNode(Character character, TrieNode node){
		//add node to its children
		TrieNode newNode = new TrieNode();
		children.put(character, newNode);
		return newNode;
		}

	public List<Road> getRoads(){
		//return all the children roads
		List<Road> childRoads = this.roads;
		//System.out.println(subRoads.size()+"000");

		for (TrieNode child : children.values()){
			roads.addAll(child.getRoads());
		}
		//System.out.println(subRoads.size()+"999");
		return childRoads;
	}	
	
	public TrieNode searchNode(String name){
		TrieNode node = this;
		//find the children contain the char
		for (Character c : name.toCharArray()){
			if (node.children.containsKey(c)){
				node = node.children.get(c);
			} 
			else 
				return null;
			}
		return node;
	}

}