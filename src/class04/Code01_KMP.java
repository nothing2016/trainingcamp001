package class04;

/**
 * KMP算法，用用来比较str中是否包含match,并返回开始的下标值
 * 时间复杂度可以到达O(N)
 */
public class Code01_KMP {
    // O(N)
	public static int getIndexOf(String s, String m) {
		if (s == null || m == null || m.length() < 1 || s.length() < m.length()) {
			return -1;
		}
		char[] str = s.toCharArray();
		char[] match = m.toCharArray();
		int x = 0; // str中当前比对到的位置
		int y = 0; // match中当前比对到的位置
		// M  M <= N   O(M)
		int[] next = getNextArray(match); // next[i]  match中i之前的字符串match[0..i-1]
		// O(N)
		while (x < str.length && y < match.length) {
			if (str[x] == match[y]) {
				x++;
				y++;
			} else if (next[y] == -1) { // y == 0
				// 如果已经匹配到match的0位置了，只能str的当前匹配下标后移动
				x++;
			} else {
				// match的匹配位置要变短
				y = next[y];
			}
		}
		return y == match.length ? x - y : -1;
	}

	// M   O(M)

	/**
	 *
	 * 求match的前缀和后缀相等的最大值, 如对于最后一个b ，前缀aa = 后缀aa ，所以为2，但跟自身的b是没有关系的
	 * match: [a  a  b  a  a  b]
	 * next:  [-1,0, 1, 0, 1, 2]
	 * 规定next0位置为-1,1位置为0
	 *  @param match
	 * @return
	 */
	public static int[] getNextArray(char[] match) {
		if (match.length == 1) {
			return new int[] { -1 };
		}
		int[] next = new int[match.length];
		next[0] = -1;
		next[1] = 0;
		int i = 2;
		// cn代表next[i - 1]的值，又代表回跳到match的下标
		int cn = 0;
		while (i < next.length) {
			if (match[i - 1] == match[cn]) { // 相等的时候，当前的next = i + 1, i 跑到下一个
				next[i++] = ++cn;
			} else if (cn > 0) {
				cn = next[cn];
			} else {
				// 如果没有匹配，当前的值就为0， i++
				next[i++] = 0;
			}
		}
		return next;
	}

	// for test
	public static String getRandomString(int possibilities, int size) {
		char[] ans = new char[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
		}
		return String.valueOf(ans);
	}

	public static void main(String[] args) {
		int possibilities = 5;
		int strSize = 20;
		int matchSize = 5;
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			String match = getRandomString(possibilities, matchSize);
			if (getIndexOf(str, match) != str.indexOf(match)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
