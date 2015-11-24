import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by lionell on 11/24/15.
 *
 * @author Ruslan Sakevych
 */
public class MaxSubstring {
    private static int n;
    private static String s;

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        s = in.next();
        in.close();
        Solution solution = new Solution();
        System.out.println(solution.run());
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.close();
    }
    private static class Solution {
        private static final long MOD = 288230376151711744L; // 2^58
        private static final long PRIME = 37L;

        public int run() {
            int l = 0;
            int r = n;
            while (r - l > 1) {
                int m = l + (r - l) / 2;
                if (check(m)) {
                    l = m;
                } else {
                    r = m;
                }
            }
            return l;
        }

        private boolean check(int len) {
            int count = n - len + 1;
            long maxPower = pow(PRIME, len - 1) % MOD;
            long[] hashes = new long[count];
            for (int i = 0; i < len; i++) {
                hashes[0] = (hashes[0] * PRIME + (s.charAt(i) - 'a')) % MOD;
            }
            for (int i = 1; i < count; i++) {
                hashes[i] = hashes[i - 1];
                hashes[i] -= (s.charAt(i - 1) - 'a') * maxPower;
                hashes[i] *= PRIME;
                hashes[i] += s.charAt(i + len - 1) - 'a';
                hashes[i] %= MOD;
            }
            Arrays.sort(hashes);
            for (int i = 1; i < count; i++) {
                if (hashes[i] == hashes[i - 1]) {
                    return true;
                }
            }
            return false;
        }

        private static long pow(long x, int y) {
            long res = 1;
            while (y > 0) {
                if (y % 2 == 1) {
                    res *= x;
                    res %= MOD;
                }
                x *= x;
                x %= MOD;
                y /= 2;
            }
            return res;
        }
    }
}
