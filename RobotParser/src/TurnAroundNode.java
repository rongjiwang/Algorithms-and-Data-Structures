
import java.util.Scanner;

public class TurnAroundNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.turnAround();

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.TURNAROUND, s)) {
			Parser.fail("turnAround fail", s);
		}
		return this;
	}

	public String toString() {
		return "turnAround";
	}
}
