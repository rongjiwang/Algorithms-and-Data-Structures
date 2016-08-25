
import java.util.Scanner;

public class LoopNode implements RobotProgramNode {

	RobotProgramNode node = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		while (true) {
			node.execute(robot);
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.LOOP, s)) {
			Parser.fail("Loop failed", s);
		}

		node = new BlockNode();
		node.parse(s);
		return node;
	}

	public String toString() {
		String s = node.toString();
		return String.format("loop %s", s);
	}
}
