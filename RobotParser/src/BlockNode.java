
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlockNode implements RobotProgramNode {

	List<RobotProgramNode> list = new ArrayList<>();

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		for (RobotProgramNode rob : list) {
			rob.execute(robot);
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		RobotProgramNode node = null;

		if (!Parser.checkFor(Parser.OPENBRACE, s)) {
			Parser.fail("OpenBrace failed", s);
		}
		while (!s.hasNext(Parser.CLOSEBRACE)) {
			if (s.hasNext()) {
				node = new StatementNode();
				node.parse(s);
				list.add(node);
			} else {
				Parser.fail("instru fail", s);
			}
		}
		if (!Parser.checkFor(Parser.CLOSEBRACE, s)) {
			Parser.fail("CloseBrace fail", s);
		}
		return this;
	}

	public String toString() {
		String s = "{";
		for (RobotProgramNode rob : list) {
			s += " " + rob.toString();
		}
		return s + " }\n";
	}
}
