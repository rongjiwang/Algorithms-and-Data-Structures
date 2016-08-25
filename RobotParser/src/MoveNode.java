
import java.util.Scanner;

public class MoveNode implements RobotProgramNode {

	private RobotExpressionNode node = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		if (node != null) {
			int x = node.evaluate(robot);
			for (int i = 0; i < x; i++) {
				robot.move();
			}
		} else {
			robot.move();
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.MOVE, s)) {
			Parser.fail("Move fail", s);
		}

		if (s.hasNext(Parser.OPENPAREN)) {
			Parser.checkFor(Parser.OPENPAREN, s);
			node = new ExpressionNode();
			node.parse(s);

			if (s.hasNext(Parser.CLOSEPAREN)) {
				Parser.checkFor(Parser.CLOSEPAREN, s);
			} else {
				Parser.fail("Closeparen fail", s);
			}
		}
		return this;
	}

	public String toString() {

		String s = "move";
		if (node != null) {
			s += String.format(" %s", node.toString());
		}
		return s;
	}

}
