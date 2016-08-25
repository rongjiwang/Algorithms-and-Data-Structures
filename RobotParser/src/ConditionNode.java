
import java.util.Scanner;

public class ConditionNode implements RobotConditionNode {
	RobotConditionNode node;

	@Override
	public boolean evaluate(Robot r) {
		// TODO Auto-generated method stub
		return node.evaluate(r);
	}

	@Override
	public RobotConditionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.GREATERTHEN)) {
			node = new GreaterThanNode();
		} else if (s.hasNext(Parser.LESSTHEN)) {
			node = new LessThanNode();
		} else if (s.hasNext(Parser.EQUAL)) {
			node = new EqualToNode();
		} else if (s.hasNext(Parser.AND)) {
			node = new AndNode();
		} else if (s.hasNext(Parser.OR)) {
			node = new OrNode();
		} else if (s.hasNext(Parser.NOT)) {
			node = new NotNode();
		}
		node.parse(s);
		return node;
	}

	public String toString() {
		return node.toString();
	}

}
