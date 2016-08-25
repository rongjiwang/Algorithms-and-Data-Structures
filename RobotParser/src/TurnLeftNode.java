
import java.util.Scanner;

public class TurnLeftNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.turnLeft();

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.TURNLEFT, s)) {
			Parser.fail("turnLeft fail", s);
		}
		return this;
	}

	public String toString() {
		return "turnL";
	}

}
