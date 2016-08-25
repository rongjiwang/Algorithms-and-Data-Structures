
import java.util.Scanner;

public class ExpressionNode implements RobotExpressionNode {
	RobotExpressionNode node = null;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return node.evaluate(robot);
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.SENSOR)) {
			node = new SensorNode();
		} else if (s.hasNext(Parser.NUM)) {
			node = new NumberNode();
		} else if (s.hasNext(Parser.OPERATION)) {
			node = new OperationNode();
		} else {
			Parser.fail("Node Exp Failed.", s);
		}
		node.parse(s);
		return node;
	}

	public String toString() {
		return node.toString();
	}

}
