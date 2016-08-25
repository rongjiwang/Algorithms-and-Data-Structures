
public class Fringe implements Comparable<Object> {
	public Node node;
	public Node from;
	public double costToHere;
	public double totalCostToGoal;

	public Fringe(Node node, Node from, double costToHere, double totalCostToGoal) {
		this.node = node;
		this.from = from;
		this.costToHere = costToHere;
		this.totalCostToGoal = totalCostToGoal;
	}

	public Node getNode() {
		return node;
	}

	public Node getFromNode() {
		return from;
	}

	public double getCostToHere() {
		return costToHere;
	}

	public double getTotalCostToGoal() {
		return totalCostToGoal;
	}

	public int compareTo(Object other) {
		Fringe f = (Fringe) other;
		if (this.totalCostToGoal < f.totalCostToGoal) {
			return -1;
		}
		if (this.totalCostToGoal > f.totalCostToGoal) {
			return 1;
		} else {
			return 0;
		}
	}

}
