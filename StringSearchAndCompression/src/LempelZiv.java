import java.util.ArrayList;
import java.util.Iterator;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	ArrayList<Tuple> encoded = new ArrayList<>();

	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String s) {
		int cursor = 0;
		int windowSize = 100;
		int prevMatch = 0;
		int lookahead = 0;
		int matchLen = 0;
		int matchLocation = 0;
		String ans = "";
		while (cursor < s.length()) {
			prevMatch = (cursor - windowSize >= 0) ? cursor - windowSize : 0;
			lookahead = (cursor + lookahead < s.length()) ? cursor + lookahead : s.length();

			if (cursor == 0) { // start with empty string
				ans = "";
			} else {
				ans = s.substring(prevMatch, cursor);
			}
			matchLen = 1;
			// search the lookahead window for next match character
			String target = s.substring(cursor, cursor + matchLen);

			if (ans.indexOf(target) != -1) { // found a char match
				matchLen++;
				while (matchLen <= lookahead) { // try to extend the matching
												// string
					target = s.substring(cursor, cursor + matchLen);
					matchLocation = ans.indexOf(target);
					// make sure cursor not out of bound
					if (matchLocation != -1 && (cursor + matchLen) < s.length()) {
						matchLen++;
					} else {
						// matching failed
						break;
					}
				}
				matchLen--;
				// matching location for the last matching string
				matchLocation = ans.indexOf(s.substring(cursor, cursor + matchLen));
				// point to the next char after matching happened
				cursor += matchLen;
				// distance back to the matching location from search window
				int offset = (cursor < (windowSize + matchLen)) ? cursor - matchLocation - matchLen
						: windowSize - matchLocation;
				// next new char from window
				String nextChar = s.substring(cursor, cursor + 1);
				// store into our collection for decoding and so on
				encoded.add(new Tuple(offset, matchLen, nextChar));
			} else {
				// 0 matching then create new tuple with new char
				String nextChar = s.substring(cursor, cursor + 1);
				encoded.add(new Tuple(0, 0, nextChar));
			}
			cursor++;
		}
		StringBuilder out = new StringBuilder();
		for (Tuple t : encoded) {
			if (t != null) {
				out.append(t.toString() + "\n");
			}
		}
		System.out.println(out.toString());
		return out.toString();
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String tags) {

		StringBuilder out = new StringBuilder();
		Iterator<Tuple> iterator = this.encoded.iterator();
		while (iterator.hasNext()) {
			Tuple next = iterator.next();
			if (next.length == 0) { // new char then added to output string
				out.append(next.nextChar);
			} else { // matching chars
				for (int i = 0; i < next.length; i++) {
					out.append(out.charAt(out.length() - next.offset));
				}
				out.append(next.nextChar);
			}
		}
		return out.toString();
	}

	class Tuple {
		private int offset;
		private int length;
		private String nextChar;

		public Tuple(int offset, int length, String nextChar) {
			this.offset = offset;
			this.length = length;
			this.nextChar = nextChar;
		}

		@Override
		public String toString() {
			return "[" + offset + "," + length + "," + nextChar + "]";
		}

	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}

	public static void main(String[] args) {
		LempelZiv lz = new LempelZiv();
		String ss = lz.compress("HELLO WORLD");
		System.out.println(ss);
		System.out.println(lz.decompress(ss));
	}
}
