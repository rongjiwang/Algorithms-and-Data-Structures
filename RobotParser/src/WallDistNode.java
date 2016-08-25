
import java.util.Scanner;

public class WallDistNode implements RobotExpressionNode {

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return robot.getDistanceToWall();
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		if (!Parser.checkFor(Parser.WALLDIST, s)) {
			Parser.fail("walldist fail", s);
		}
		return this;
	}

	public String toString() {
		return Parser.WALLDIST.toString();
	}

}
