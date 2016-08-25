
import java.util.Scanner;

public class SubNode implements RobotExpressionNode {

	ExpressionNode nodeX = null;
	ExpressionNode nodeY = null;
	int front = -1;
	int behind = -1;

	public int evaluate(Robot robot) {
		front = nodeX.evaluate(robot);
		behind = nodeY.evaluate(robot);
		return (front - behind);
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {

		if (!Parser.checkFor(Parser.SUB, s)) {
			Parser.fail("sub fail", s);
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

		return String.format("sub ( %s, %s )", nodeX.toString(), nodeY.toString());
	}

}
