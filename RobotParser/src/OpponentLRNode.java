
import java.util.Scanner;

public class OpponentLRNode implements RobotExpressionNode {

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return robot.getOpponentLR();
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.OPPLR, s)) {
			Parser.fail("opp left right- fail", s);
		}

		return this;
	}

	public String toString() {
		return Parser.OPPLR.toString();
	}

}
