import java.util.Scanner;

public interface RobotConditionNode {
	public boolean evaluate(Robot r);

	public RobotConditionNode parse(Scanner s);
}
