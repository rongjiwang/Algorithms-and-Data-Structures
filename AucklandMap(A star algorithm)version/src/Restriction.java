
public class Restriction {
	private int fromNode;
	private int fromRoad;
	private int turnNode;
	private int toRoad;
	private int toNode;

	public Restriction(int fromNode, int fromRoad, int turnNode, int toRoad, int toNode) {
		this.fromNode = fromNode;
		this.fromRoad = fromRoad;
		this.turnNode = turnNode;
		this.toRoad = toRoad;
		this.toNode = toNode;

	}

	public int getFromNode() {
		return fromNode;
	}

	public void setFromNode(int fromNode) {
		this.fromNode = fromNode;
	}

	public int getFromRoad() {
		return fromRoad;
	}

	public void setFromRoad(int fromRoad) {
		this.fromRoad = fromRoad;
	}

	public int getTurnNode() {
		return turnNode;
	}

	public void setTurnNode(int turnNode) {
		this.turnNode = turnNode;
	}

	public int getToRoad() {
		return toRoad;
	}

	public void setToRoad(int toRoad) {
		this.toRoad = toRoad;
	}

	public int getToNode() {
		return toNode;
	}

	public void setToNode(int toNode) {
		this.toNode = toNode;
	}
}
