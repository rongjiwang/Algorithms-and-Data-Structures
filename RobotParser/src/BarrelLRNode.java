
import java.util.Scanner;

public class BarrelLRNode implements RobotExpressionNode {
	RobotExpressionNode node = null;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (node != null) {
			int value = node.evaluate(robot);
			return robot.getBarrelLR(value);
		} else {
			return robot.getClosestBarrelLR();
		}
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.BARRELLR, s)) {
			Parser.fail("barrelLR fail", s);
		}
		if (s.hasNext(Parser.OPENPAREN)) {
			Parser.checkFor(Parser.OPENPAREN, s);
			node = new ExpressionNode();
			node.parse(s);
			if (s.hasNext(Parser.CLOSEPAREN)) {
				Parser.checkFor(Parser.CLOSEPAREN, s);
			} else {
				Parser.fail("closeparen fail", s);
			}
		}
		return this;
	}

	public String toString() {
		return Parser.BARRELLR.toString();
	}

}
