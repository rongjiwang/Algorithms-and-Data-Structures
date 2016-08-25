
import java.util.Scanner;

public class OrNode implements RobotConditionNode {

	ConditionNode nodeX = null;
	ConditionNode nodeY = null;

	@Override
	public boolean evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (nodeX.evaluate(robot) || nodeY.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public RobotConditionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.OR, s)) {
			Parser.fail("or fail", s);
		}

		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("openparen fail", s);
		}

		nodeX = new ConditionNode();
		nodeX.parse(s);

		if (!Parser.checkFor(Parser.COMMA, s)) {
			Parser.fail("comma fail", s);
		}

		nodeY = new ConditionNode();
		nodeY.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("closeparen fail", s);
		}
		return this;
	}

	public String toString() {
		return "or(" + nodeX.toString() + ", " + nodeY.toString() + ")";
	}

}
