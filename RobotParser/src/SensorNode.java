
import java.util.Scanner;

public class SensorNode implements RobotExpressionNode {
	RobotExpressionNode node = null;

	@Override
	public int evaluate(Robot robot) {
		// TODO Auto-generated method stub
		return node.evaluate(robot);
	}

	@Override
	public RobotExpressionNode parse(Scanner s) {
		// TODO Auto-generated method stub
		if (s.hasNext(Parser.FUELLEFT)) {
			node = new FuelLeftNode();
		} else if (s.hasNext(Parser.OPPLR)) {
			node = new OpponentLRNode();
		} else if (s.hasNext(Parser.OPPFB)) {
			node = new OpponentFBNode();
		} else if (s.hasNext(Parser.NUMBARRELS)) {
			node = new NumBarrelsNode();
		} else if (s.hasNext(Parser.BARRELLR)) {
			node = new BarrelLRNode();
		} else if (s.hasNext(Parser.BARRELFB)) {
			node = new BarrelFBNode();
		} else if (s.hasNext(Parser.WALLDIST)) {
			node = new WallDistNode();
		}
		node.parse(s);
		return node;
	}

	public String toString() {
		return node.toString();
	}

}
