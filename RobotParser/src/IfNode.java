
import java.util.Scanner;

public class IfNode implements RobotProgramNode {
	ConditionNode condition = null;
	RobotProgramNode ifNode = null;
	RobotProgramNode elseNode = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		if (condition != null) {
			if (condition.evaluate(robot)) {
				ifNode.execute(robot);
			} else if (elseNode != null) {
				elseNode.execute(robot);

			}
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.IF, s)) {
			Parser.fail("if fail", s);
		}

		if (!Parser.checkFor(Parser.OPENPAREN, s)) {
			Parser.fail("openparen fail", s);
		}

		condition = new ConditionNode();
		condition.parse(s);

		if (!Parser.checkFor(Parser.CLOSEPAREN, s)) {
			Parser.fail("closeparen fail", s);
		}

		ifNode = new BlockNode();
		ifNode.parse(s);

		if (s.hasNext(Parser.ELSE)) {
			if (!Parser.checkFor(Parser.ELSE, s)) {
				Parser.fail("else fail", s);
			}

			elseNode = new BlockNode();
			elseNode.parse(s);
		}

		return this;
	}

	public String toString() {
		String s = "if(" + ifNode.toString() + ") " + ifNode.toString();
		if (elseNode != null) {
			s += "else " + elseNode.toString();
		}
		return s;
	}

}
