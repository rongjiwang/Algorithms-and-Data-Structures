
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProgramNode implements RobotProgramNode {

	private List<RobotProgramNode> list = new ArrayList<RobotProgramNode>();

	@Override
	public void execute(Robot robot) {
		// TODO Auto-generated method stub
		for (RobotProgramNode r : list) {
			r.execute(robot);
		}
	}

	@Override
	public RobotProgramNode parse(Scanner s) {
		// TODO Auto-generated method stub
		StatementNode state;
		while (s.hasNext()) {
			state = new StatementNode();
			list.add(state.parse(s));
		}
		return this;
	}

	public String toString() {
		String s = "";
		for (RobotProgramNode r : list) {
			s += r.toString() + ";\n";
		}
		return s;
	}

}
