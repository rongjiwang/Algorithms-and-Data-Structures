import java.util.ArrayDeque;
import java.util.Queue;

public class APnode {

	private Node node;
	private double reach;
	private APnode parent;
	private double depth;
	private Queue<Node> children;

	public APnode(Node node, double reach, APnode parent) {
		this.node = node;
		this.reach = reach;
		this.parent = parent;
		this.depth = reach;

	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public double getReach() {
		return reach;
	}

	public void setReach(double d) {
		this.reach = d;
	}

	public APnode getParent() {
		return parent;
	}

	public void setParent(APnode parent) {
		this.parent = parent;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Queue<Node> getChildren() {
		return children;
	}

	public void setChildren(Queue<Node> children) {
		this.children = children;
	}

}
