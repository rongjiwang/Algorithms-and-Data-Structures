
import java.util.Scanner;

public class TurnRightNode implements RobotProgramNode {

	RobotProgramNode rpNode = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.turnRight();

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.TURNRIGHT, s)) {
			Parser.fail("turnRight fail", s);
		}
		return this;
	}

	public String toString() {
		return "turnR";
	}

}
