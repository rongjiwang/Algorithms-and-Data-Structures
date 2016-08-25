import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.*;
import javax.swing.JFileChooser;

/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {
	// Useful Patterns

	public static final String VAR = null;
	public static Pattern ADD = Pattern.compile("add");
	public static Pattern SUB = Pattern.compile("sub");
	public static Pattern MUL = Pattern.compile("mul");
	public static Pattern DIV = Pattern.compile("div");

	public static Pattern CONDITION = Pattern.compile("and|or|not|lt|gt|eq");

	public static Pattern FUELLEFT = Pattern.compile("fuelLeft");
	public static Pattern OPPLR = Pattern.compile("oppLR");
	public static Pattern OPPFB = Pattern.compile("oppFB");
	public static Pattern NUMBARRELS = Pattern.compile("numBarrels");
	public static Pattern BARRELLR = Pattern.compile("barrelLR");
	public static Pattern BARRELFB = Pattern.compile("barrelFB");
	public static Pattern WALLDIST = Pattern.compile("wallDist");
	public static Pattern SENSOR = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	public static Pattern NUM = Pattern.compile("-?[0-9]+");
	public static Pattern OPERATION = Pattern.compile("add|sub|mul|div");
	public static Pattern COMMA = Pattern.compile(",");
	public static Pattern LESSTHEN = Pattern.compile("lt");
	public static Pattern GREATERTHEN = Pattern.compile("gt");
	public static Pattern EQUAL = Pattern.compile("eq");
	public static Pattern AND = Pattern.compile("and");
	public static Pattern OR = Pattern.compile("or");

	public static Pattern NOT = Pattern.compile("not");

	public static Pattern ACTION = Pattern.compile("move|takeFuel|turnL|turnR|turnAround|wait|shieldOn|shieldOff");

	public static Pattern IF = Pattern.compile("if");
	public static Pattern ELSE = Pattern.compile("else");
	public static Pattern WHILE = Pattern.compile("while");
	public static Pattern LOOP = Pattern.compile("loop");

	public static Pattern MOVE = Pattern.compile("move");
	public static Pattern TAKEFUEL = Pattern.compile("takeFuel");
	public static Pattern TURNLEFT = Pattern.compile("turnL");
	public static Pattern TURNRIGHT = Pattern.compile("turnR");
	public static Pattern WAIT = Pattern.compile("wait");
	public static Pattern TURNAROUND = Pattern.compile("turnAround");
	public static Pattern SHIELDON = Pattern.compile("shieldOn");
	public static Pattern SHIELDOFF = Pattern.compile("shieldOff");

	public static Pattern SEMICOL = Pattern.compile(";");
	public static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	public static Pattern OPENPAREN = Pattern.compile("\\(");
	public static Pattern CLOSEPAREN = Pattern.compile("\\)");
	public static Pattern OPENBRACE = Pattern.compile("\\{");
	public static Pattern CLOSEBRACE = Pattern.compile("\\}");

	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement
														// this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		RobotProgramNode node = null;
		if (s != null) {
			node = new ProgramNode().parse(s);
		}
		return node;
	}

	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public
// (or private)
