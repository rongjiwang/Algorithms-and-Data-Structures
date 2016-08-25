
import java.util.Scanner;


public class OperationNode implements RobotExpressionNode {
	RobotExpressionNode node = null;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return node.evaluate(robot);
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub

		if (s.hasNext(Parser.ADD)){
			node = new AddNode();
		} else if (s.hasNext(Parser.SUB)){
			node = new SubNode();
		} else if (s.hasNext(Parser.MUL)){
			node = new MulNode();
		} else if (s.hasNext(Parser.DIV)){
			node = new DivNode();
		} 
		node.parse(s);
		return node;
	}

	@Override
	public String toString() {
		return node.toString();
	}

}
