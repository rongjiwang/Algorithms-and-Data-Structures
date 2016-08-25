
import java.util.Scanner;

public class StatementNode implements RobotProgramNode {

	RobotProgramNode node = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		node.execute(robot);

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.ACTION)) {
			node = new ActionNode();
		} else if (s.hasNext(Parser.LOOP)) {
			node = new LoopNode();
		} else if (s.hasNext(Parser.IF)) {
			node = new IfNode();
		} else if (s.hasNext(Parser.WHILE)) {
			node = new WhileNode();
		} 
//		else if (s.hasNext(Parser.VAR)) {
//			node = new AssgnNode();
//		} 
		else {
			Parser.fail("nodestatement fail.", s);
		}
		node.parse(s);
		return this;
	}

	public String toString() {
		return node.toString();
	}
}
