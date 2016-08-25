
import java.util.Scanner;

public class ActionNode implements RobotProgramNode {
	RobotProgramNode node = null;

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		node.execute(robot);

	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.MOVE)) {
			node = new MoveNode();
		} else if (s.hasNext(Parser.TAKEFUEL)) {
			node = new TakeFuelNode();
		} else if (s.hasNext(Parser.TURNLEFT)) {
			node = new TurnLeftNode();
		} else if (s.hasNext(Parser.TURNRIGHT)) {
			node = new TurnRightNode();
		} else if (s.hasNext(Parser.WAIT)) {
			node = new WaitNode();
		} else if (s.hasNext(Parser.TURNAROUND)) {
			node = new TurnAroundNode();
		} else if (s.hasNext(Parser.SHIELDON)) {
			node = new ShieldOnNode();
		} else if (s.hasNext(Parser.SHIELDOFF)) {
			node = new ShieldOffNode();
		} else {
			Parser.fail("nodeaction fail", s);
		}
		node.parse(s);

		if (!Parser.checkFor(Parser.SEMICOL, s)) {
			Parser.fail("Expecting ;", s);
		}

		return node;
	}

	public String toString() {
		return node.toString();
	}
}
