import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {

	PriorityQueue<Node> que;
	HashMap<Character, String> charToString = new HashMap<>();
	HashMap<String, Character> stringToChar = new HashMap<>();

	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		// TODO fill this in.
		HashMap<Character, Integer> freq = new HashMap<>();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (freq.containsKey(c)) // create frequency map
				freq.put(c, freq.get(c) + 1);
			else
				freq.put(c, 1);
		}

		que = new PriorityQueue<Node>();

		for (Character c : freq.keySet()) { // store collected data into
			// priority_queue
			que.add(new Node(c, freq.get(c), null, null));
		}

		while (que.size() > 1) {
			// System.out.println(que.size() + "#");
			Node left = que.poll();
			Node right = que.poll(); // build up the tree
			// System.out.println(left.letter + " " + left.freq + " " +
			// right.letter + " " + right.freq);
			que.add(new Node('\0', left.freq + right.freq, left, right));
		}
		Node root = que.poll(); // set up root
		postOrder(root, new String());

		for (char c : this.charToString.keySet()) {
			System.out.println("< " + c + " " + this.charToString.get(c) + " >");
		}

	}

	/**
	 * every left node with 0, and right with 1(not include root node)
	 *
	 * @param root
	 * @param string
	 */
	private void postOrder(Node root, String string) {
		if (root.letter == '\0') {
			this.postOrder(root.left, string + '0');
			this.postOrder(root.right, string + '1');
		} else {
			charToString.put(root.letter, string);
			stringToChar.put(string, root.letter);
		}
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		StringBuilder ss = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			ss.append(this.charToString.get(text.charAt(i)));
		}
		return ss.toString();
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		StringBuilder temp = new StringBuilder();
		StringBuilder ans = new StringBuilder();

		for (int i = 0; i < encoded.length(); i++) {
			temp.append(encoded.charAt(i));
			if (this.stringToChar.containsKey(temp.toString())) { // while
																	// match,
																	// then add
																	// to the
																	// output
																	// string
				ans.append(this.stringToChar.get(temp.toString()));
				temp = new StringBuilder();
			}
		}
		return ans.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
	}

	@SuppressWarnings("rawtypes")
	static class Node implements Comparable {
		public char letter;
		public int freq;
		public Node left;
		public Node right;

		Node(char c, int f, Node lft, Node rt) {
			letter = c;
			freq = f;
			left = lft;
			right = rt;
		}

		@Override
		public int compareTo(Object o) {
			return freq - ((Node) o).freq;
		}

		public String toString() {
			return "< " + letter + " " + freq + " >";
		}

	}

	public static void main(String[] args) {
		HuffmanCoding h = new HuffmanCoding("HELLO WORLD");
		String encoded = h.encode("HELLO WORLD");
		System.out.println(encoded);
		String decoded = h.decode(encoded);
		System.out.println(decoded);
	}
}
