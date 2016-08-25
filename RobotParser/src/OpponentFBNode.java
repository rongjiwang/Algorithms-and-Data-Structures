
import java.util.Scanner;

public class OpponentFBNode implements RobotExpressionNode {

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return robot.getOpponentFB();
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.OPPFB, s)) {
			Parser.fail("opp front back fail", s);
		}
		return this;
	}

	public String toString() {
		return Parser.OPPFB.toString();
	}
}
