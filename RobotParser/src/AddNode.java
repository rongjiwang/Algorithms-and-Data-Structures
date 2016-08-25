
import java.util.Scanner;

public class AddNode implements RobotExpressionNode {
	ExpressionNode nodeX = null;
	ExpressionNode nodeY = null;
	int front = -1;
	int behind = -1;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		front = nodeX.evaluate(robot);
		behind = nodeY.evaluate(robot);
		return (front + behind);
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.ADD, s)) {
			Parser.fail("add fail", s);
		}

		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("openparen fail", s);
		}

		nodeX = new ExpressionNode();
		nodeX.parse(s);

		if (!Parser.checkFor(Parser.COMMA, s)) {
			Parser.fail("comma fail", s);
		}

		nodeY = new ExpressionNode();
		nodeY.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("closeparen fail", s);
		}
		return this;
	}

	public String toString() {

		return String.format("add ( %s, %s )", nodeX.toString(), nodeY.toString());
	}

}
