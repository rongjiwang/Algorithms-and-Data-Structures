
import java.util.Scanner;

public class ShieldOnNode implements RobotProgramNode {

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		robot.setShield(true);

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.SHIELDON, s)) {
			Parser.fail("shieldOn failed", s);
		}
		return this;
	}

	public String toString() {
		return "shieldOn";
	}

}
