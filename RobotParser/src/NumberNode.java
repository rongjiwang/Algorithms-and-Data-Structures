
import java.util.Scanner;


public class NumberNode implements RobotExpressionNode {

	public int number;

	public NumberNode(int n) {
		this.number = n;
	}

	public NumberNode() {
		this.number = 0;
	}

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return number;
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.NUM)) {
			String value = s.next(Parser.NUM);
			number = Integer.parseInt(value);
		} else {
			Parser.fail("number fail", s);
		}
		return this;
	}

	public String toString() {
		return Integer.toString(number);
	}

}
