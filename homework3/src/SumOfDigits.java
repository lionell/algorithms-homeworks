import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by lionell on 11/29/15.
 *
 * @author Ruslan Sakevych
 */
public class SumOfDigits {

    public static void main(String[] args) {
        Reader in = new Reader();
        long a = in.nextLong();
        long b = in.nextLong();
        int s = in.nextInt();
        in.close();
        Solution solution = new Solution();
        long count = solution.findCount(a, b, s);
        long minimum = solution.findMinimum(a, b, s);
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.println(count);
        out.println(minimum);
        out.close();
    }

    private static class Solution {
        private long[][] lenSumCount = new long[16][136];

        public Solution() {
            initLenSumCount();
        }

        private void initLenSumCount() {
            lenSumCount[0][0] = 1;
            for (int i = 1; i < 16; i++) {
                for (int j = 0; j < 136; j++) {
                    for (int k = 0; k < 10; k++) {
                        if (j - k >= 0) {
                            lenSumCount[i][j] += lenSumCount[i - 1][j - k];
                        }
                    }
                }
            }
        }

        public long findCount(long l, long r, int s) {
            return findNotMoreCount(r, s) - findNotMoreCount(l - 1, s);
        }

        private long findNotMoreCount(long u, int s) {
            u += 1;
            int digitCount = ((int) Math.log10(u)) + 1;
            int need = s;
            long t = u;
            while (t > 0) {
                need -= t % 10;
                t /= 10;
            }
            long res = 0;
            for (int k = 1; k <= digitCount; k++) {
                need += u % 10;
                for (int i = 0; i < u % 10; i++) {
                    if (need - i >= 0) {
                        res += lenSumCount[k - 1][need - i];
                    }
                }
                u /= 10;
            }
            return res;
        }

        public long findMinimum(long l, long r, int s) {
            long desired = findNotMoreCount(l, s) + 1;
            while (r > l + 1) {
                long mid = l + (r - l) / 2;
                long midCount = findNotMoreCount(mid, s);
                if (midCount >= desired) {
                    r = mid;
                } else if (midCount < desired) {
                    l = mid;
                }
            }
            return r;
        }
    }

    private static class Reader {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public Reader() {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        public Reader(String s) {
            try {
                reader = new BufferedReader(new FileReader(s));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String nextToken() {
            while (tokenizer == null || !tokenizer.hasMoreElements()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(nextToken());
        }

        public long nextLong() {
            return Long.parseLong(nextToken());
        }

        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
