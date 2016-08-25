
import java.util.Scanner;

public class NumBarrelsNode implements RobotExpressionNode {

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return robot.numBarrels();
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.NUMBARRELS, s)) {
			Parser.fail("numbarrel fail", s);
		}
		return this;
	}

	public String toString() {
		return Parser.NUMBARRELS.toString();
	}
}
