
import java.util.Scanner;

public class AndNode implements RobotConditionNode {
	ConditionNode nodeX = null;
	ConditionNode nodeY = null;

	@Override
	public boolean evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (nodeX.evaluate(robot) && nodeY.evaluate(robot)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public RobotConditionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.AND, s)) {
			Parser.fail("and fail. expecting: " + Parser.AND.toString(), s);
		}

		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("openparen fail. expecting : " + Parser.OPENPAREN.toString(), s);
		}

		nodeX = new ConditionNode();
		nodeX.parse(s);

		if (!Parser.checkFor(Parser.COMMA, s)) {
			Parser.fail("comma fail. expecting ,", s);
		}

		nodeY = new ConditionNode();
		nodeY.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("closeparen fail. expecting: " + Parser.CLOSEPAREN.toString(), s);
		}
		return this;
	}

	public String toString() {
		return "and(" + nodeX.toString() + ", " + nodeY.toString() + ")";
	}

}
