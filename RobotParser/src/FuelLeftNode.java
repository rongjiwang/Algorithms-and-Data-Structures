
import java.util.Scanner;

public class FuelLeftNode implements RobotExpressionNode {

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return robot.getFuel();
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.FUELLEFT, s)) {
			Parser.fail("fuel-left fail", s);
		}
		return this;
	}

	public String toString() {
		return Parser.FUELLEFT.toString();
	}
}
