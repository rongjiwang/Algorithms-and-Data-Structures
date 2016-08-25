
import java.util.Scanner;

public class GreaterThanNode implements RobotConditionNode {

	RobotExpressionNode nodeX = null;
	RobotExpressionNode nodeY = null;

	@Override
	public boolean evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (nodeX.evaluate(robot) > nodeY.evaluate(robot)) {
			return true;
		}
		return false;
	}

	@Override
	public RobotConditionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.GREATERTHEN, s)) {
			Parser.fail("Greater Than fail", s);
		}
		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("Openparen fail", s);
		}
		nodeX = new ExpressionNode();
		nodeX.parse(s);

		if (!Parser.checkFor(Parser.COMMA, s)) {
			Parser.fail("Comma fail", s);
		}

		nodeY = new ExpressionNode();
		nodeY.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("Closeparen fail", s);
		}
		return this;

	}

	public String toString() {
		return "gt(" + nodeX.toString() + ", " + nodeY.toString() + ")";

	}

}
