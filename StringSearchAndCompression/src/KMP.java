/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {

	@SuppressWarnings("unused")
	private String pattern;
	@SuppressWarnings("unused")
	private String text;

	public KMP(String pattern, String text) {
		this.pattern = pattern;
		this.text = text;
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		if (pattern.length() == 0 || text.length() == 0)
			return -1;
		// if(pattern.length() == 1)
		// return 0;
		char[] pat = pattern.toCharArray(); // create char array for both
											// pattern and text
		char[] tex = text.toCharArray();

		int currentChar = 0;
		int currentMatch = 0;
		for (char c : pat) {
			System.out.println(c);
		}
		int[] table = matchTable(pat);

		while ((currentMatch + currentChar) < tex.length) {
			if (pat[currentChar] == tex[currentMatch + currentChar]) { // match
				currentChar++;
				if (currentChar == pat.length) {
					return currentMatch; // found pattern
				}
			} else if (table[currentChar] == -1) { // mismatch, no self overlap
				currentChar = 0;
				currentMatch = currentMatch + currentChar + 1;
			} else { // mismatch,with self overlap, jump forward
				currentMatch = currentMatch + currentChar - table[currentChar];
				currentChar = table[currentChar];
			}
		}
		return -1; // failed to find pattern
	}

	/**
	 * 
	 * @param input
	 *            pattern as char array version
	 * @return int array match table for search method
	 */
	private int[] matchTable(char[] s) {
		System.out.println(s.length);
		int[] matchTable = new int[s.length + 1]; // create match table
		matchTable[0] = -1;
		matchTable[1] = 0;

		int lastMatch = 0;
		int p = 2;

		while (p < s.length) {
			if (s[p - 1] == s[lastMatch]) { // substrings
				matchTable[p] = lastMatch + 1;
				p++;
				lastMatch++;
			} else if (lastMatch > 0) { // mismatch, restart prefix
				lastMatch = matchTable[lastMatch];
			} else { // run out of candidate prefixes
				matchTable[p] = 0;
				p++;
			}
		}
		return matchTable;
	}

	public static void main(String[] args) {
		KMP k = new KMP("s", "stuvwxyz");
		System.out.println(k.search("s", "stuvwxyz"));
	}
}
