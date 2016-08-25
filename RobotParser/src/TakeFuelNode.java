
import java.util.Scanner;

public class TakeFuelNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.takeFuel();

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.TAKEFUEL, s)) {
			Parser.fail("takefuel fail", s);
		}
		return this;
	}

	public String toString() {
		return "takeFuel";
	}

}
