
import java.util.Scanner;

public class WaitNode implements RobotProgramNode {

	RobotExpressionNode RENode;
	int time = -1;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		if (RENode == null) {
			robot.idleWait();
		} else {
			int y = RENode.evaluate(robot);
			for (int i = 0; i < y; i++) {
				robot.idleWait();
			}
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.WAIT, s)) {
			Parser.fail("wait fail", s);
		}
		if (s.hasNext(Parser.OPENBRACE)) {
			Parser.checkFor(Parser.OPENBRACE, s);
			RENode = new ExpressionNode();
			RENode.parse(s);
			if (s.hasNext(Parser.CLOSEBRACE)) {
				Parser.checkFor(Parser.CLOSEBRACE, s);
			} else {
				Parser.fail("closebrace fail", s);
			}
		}
		return this;
	}

	public String toString() {
		String string = "wait";
		if (RENode != null)
			string += "(" + time + ")";
		return string;
	}

}
