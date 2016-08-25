
import java.util.Scanner;

public class NotNode implements RobotConditionNode {
	ConditionNode nodeX = null;

	@Override
	public boolean evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (nodeX.evaluate(robot)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public RobotConditionNode parse(Scanner s) {
		// TODO Auto-generated method stub

		if (!Parser.checkFor(Parser.NOT, s)) {
			Parser.fail("not fail", s);
		}

		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("openparen fail", s);
		}

		nodeX = new ConditionNode();
		nodeX.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("closeparen fail", s);
		}
		return this;
	}

	public String toString() {
		return "not(" + nodeX.toString() + ")";
	}
}
