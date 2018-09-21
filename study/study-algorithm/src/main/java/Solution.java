import java.util.Arrays;
import java.util.Comparator;

/**
 * 输入一个正整数数组，把数组里所有数字拼接起来排成一个数，打印能拼接出的所有数字中最小的一个。例如输入数组{3，32，321}，则打印出这三个数字能排成的最小数字为321323
 * 
 * https://www.nowcoder.com/practice/8fecd3f8ba334add803bf2a06af1b993?tpId=13&tqId=11185&tPage=2&rp=2&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking
 * 
 * @author bage
 *
 */
public class Solution {
	public String PrintMinNumber(int [] numbers) {
		String[] strs = new String[numbers.length];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = String.valueOf(numbers[i]);
		}
		Arrays.sort(strs,new Comparator<String>() {

			public int compare(String Str1, String Str2) {
				int max = Str1.length() > Str2.length() ? Str1.length() : Str2.length();
				char charAtStr1 = '0';
				char charAtStr2 = '0';
				for (int i = 0; i < max; i++) {
					charAtStr1 = i < Str1.length() ? Str1.charAt(i) : '0';
					charAtStr2 = i < Str2.length() ? Str2.charAt(i) : '0';
					if(charAtStr1 > charAtStr2){
						return 1;
					} else if(charAtStr1 < charAtStr2){
						return -1;
					} else {
						// o1.charAt(i) == o2.charAt(i)
					}
				}
				return 0;
			}
		});
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
		}
		return sb.toString();
    }


	public static void main(String[] args) {

		String i = new Solution().PrintMinNumber(new int[] {3,32,321});
		System.out.println(i);
		
		i = new Solution().PrintMinNumber(new int[] {3,5,1,4,2});
		System.out.println(i);
		
	}
}