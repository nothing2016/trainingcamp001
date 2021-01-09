package class05;

/**
 * manacher算法: 找到最大的回文子串的长度
 * pArr[]: 记录每个位置回文半径长度
 * R: 代表记录过的回文最大的右边界，R只会一直增大
 * C: 和R 配套使用，代表对应R的回文中心点
 *
 * 四种情况：
 *     1) i 不在R的范围内，那么只能向i的两边扩充
 *     2）i 在R的范围内，分为三种情况
 *        a).i 的对称点i` 的回文半径刚好在[L,R]的范围内，pArr[i] = pArr[i`]
 *        b).i 的对称点i` 的回文半径超过了[L,R]的范围内，pArr[i] = R - i
 *        c).i 的对称点i` 的回文半径刚好在[L,R]的边界上，需要两边扩充
 *
 *  注意：这里的中心点C一定是在i的左边的
 */
public class Code01_Manacher {

	public static int manacher(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		// "12132" -> "#1#2#1#3#2#"  补充#，让任意一个字符串都是奇数的回文
		char[] str = manacherString(s);
		// 回文半径的大小，不是半径的下标，如果一个当前一个数没有回文，只有自己，那么值就为1
		int[] pArr = new int[str.length];
		int C = -1;
		// 讲述中：R代表最右的扩成功的位置。coding：最右的扩成功位置的，再下一个位置
		int R = -1;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < str.length; i++) {
			// R第一个违规的位置，i>= R
			// i位置扩出来的答案，i位置扩的区域，至少是多大。
			// 先求当前点i的回文半径，这里就是在R范围内a.b的情况找最小值作为当前的回文半径
			// 1情况的话，就是1作为自己的回文半径
			pArr[i] = i < R ? Math.min(pArr[2 * C - i], R - i) : 1;

			// 只要扩充还在有效的范围内，继续
			while (i + pArr[i] < str.length && i - pArr[i] > -1) {
				// 如果相等，扩充（就是1，和c的情况）
				if (str[i + pArr[i]] == str[i - pArr[i]])
					pArr[i]++;
				else {
					// a,b 情况会直接break
					break;
				}
			}
			// 拿到了i的回文半径，查看是否需要更新
			if (i + pArr[i] > R) {
				R = i + pArr[i];
				C = i;
			}
			max = Math.max(max, pArr[i]);
		}
		return max - 1;
	}

	public static char[] manacherString(String str) {
		char[] charArr = str.toCharArray();
		char[] res = new char[str.length() * 2 + 1];
		int index = 0;
		for (int i = 0; i != res.length; i++) {
			res[i] = (i & 1) == 0 ? '#' : charArr[index++];
		}
		return res;
	}

	// for test
	public static int right(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = manacherString(s);
		int max = 0;
		for (int i = 0; i < str.length; i++) {
			int L = i - 1;
			int R = i + 1;
			while (L >= 0 && R < str.length && str[L] == str[R]) {
				L--;
				R++;
			}
			max = Math.max(max, R - L - 1);
		}
		return max / 2;
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
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			if (manacher(str) != right(str)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
