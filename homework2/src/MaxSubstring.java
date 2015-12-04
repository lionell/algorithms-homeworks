import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by lionell on 11/24/15.
 *
 * @author Ruslan Sakevych
 */
public class MaxSubstring {
    private static int n;
    private static String s;

    public static void main(String[] args) throws IOException {
        Reader in = new Reader();
        n = in.nextInt();
        s = in.nextToken();
        in.close();
        Solution solution = new Solution();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.println(solution.run());
        out.close();
    }
    private static class Solution {
        private static final long MOD = 1152921504606846976L; // 2^62
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
            long maxPower = pow(PRIME, len - 1);
            long[] hashes = new long[count];
            for (int i = 0; i < len; i++) {
                hashes[0] = (hashes[0] * PRIME + (s.charAt(i) - 'a')) % MOD;
            }
            for (int i = 1; i < count; i++) {
                hashes[i] = hashes[i - 1];
                hashes[i] -= (s.charAt(i - 1) - 'a') * maxPower;
                hashes[i] = (hashes[i] + MOD) % MOD;
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

    private static class Reader {
        private BufferedReader br;
        private StringTokenizer st;

        public Reader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public Reader(String s) {
            try {
                br = new BufferedReader(new FileReader(s));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String nextToken() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(nextToken());
        }

        public void close() {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
