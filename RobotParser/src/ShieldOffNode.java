
import java.util.Scanner;

public class ShieldOffNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.setShield(false);

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.SHIELDOFF, s)) {
			Parser.fail("shieldOff failed", s);
		}
		return this;
	}

	public String toString() {
		return "shieldOff";
	}

}
