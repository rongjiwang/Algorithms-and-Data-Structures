
import java.util.Scanner;

public class BarrelFBNode implements RobotExpressionNode {
	RobotExpressionNode node;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		if (node != null) {
			int value = node.evaluate(robot);
			return robot.getBarrelFB(value);
		} else {
			return robot.getClosestBarrelFB();
		}
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.BARRELFB, s)) {
			Parser.fail("barrelfb fail", s);
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
		return Parser.BARRELFB.toString();
	}

}
