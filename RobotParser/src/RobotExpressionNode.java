import java.util.Scanner;

public interface RobotExpressionNode {
	
	public int evaluate(Robot robot);
	
	public RobotExpressionNode parse (Scanner s);
}
