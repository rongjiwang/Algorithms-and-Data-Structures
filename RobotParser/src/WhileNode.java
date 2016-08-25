
import java.util.Scanner;

public class WhileNode implements RobotProgramNode {
	ConditionNode nodeCondition;
	BlockNode nodeBlock;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		while (true) {
			if (nodeCondition.evaluate(robot)) {
				nodeBlock.execute(robot);
			} else {
				return;
			}
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (!Parser.checkFor(Parser.WHILE, s)) {
			Parser.fail("while fail", s);
		}

		if (s.hasNext(Parser.OPENPAREN)) {
			Parser.checkFor(Parser.OPENPAREN, s);

			// "COND"
			if (s.hasNext(Parser.CONDITION)) {
				nodeCondition = new ConditionNode();
				nodeCondition.parse(s);
			} else {
				Parser.fail("condition fail", s);
			}

			// ")"
			if (s.hasNext(Parser.CLOSEPAREN)) {
				Parser.checkFor(Parser.CLOSEPAREN, s);
			}

			nodeBlock = new BlockNode();
			nodeBlock.parse(s);
		}
		return this;

	}

	public String toString() {
		return "while(" + nodeCondition.toString() + ") " + nodeBlock.toString();
	}

}
