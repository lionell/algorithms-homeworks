import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by lionell on 11/24/15.
 *
 * @author Ruslan Sakevych
 */
public class Boredom {
    private static int n;
    private static int m;
    private static int[] clock;
    private static int[][] segments;

    public static void main(String[] args) {
        Reader in = new Reader();
        n = in.nextInt();
        m = in.nextInt();
        String s = in.nextToken();
        clock = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            clock[i] = s.charAt(i) - '0';
        }
        segments = new int[2][n];
        for (int i = 0; i < m; i++) {
            segments[0][i] = in.nextInt();
            segments[1][i] = in.nextInt();
        }
        in.close();
        Solution solution = new Solution();
        int[] res = solution.run();
        for (int x : res) {
            System.out.println(x);
        }
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.close();
    }

    private static class Solution {
        private int k;
        private int size;
        private int[][] digits;
        private int[] extra;

        public Solution() {
            k = (int)Math.round(Math.sqrt(n));
            size = (n + k - 1) / k;
            digits = new int[10][size];
            for (int i = 0; i < n; i++) {
                digits[clock[i]][i / k]++;
            }
            extra = new int[size];
        }

        public int[] run() {
            int[] res = new int[m];
            for (int i = 0; i < m; i++) {
                res[i] = getSumOnSegment(segments[0][i] - 1, segments[1][i] - 1);
                increaseSegment(segments[0][i] - 1, segments[1][i] - 1);
            }
            return res;
        }

        private int getSumOnSegment(int l, int r) {
            int res = 0;
            int leftBound = (l + k - 1) / k;
            int rightBound = r / k - 1;
            if (leftBound <= rightBound) {
                for (int i = leftBound; i <= rightBound; i++) {
                    res += getSum(i);
                }
                for (int i = l; i < leftBound * k; i++) {
                    res += time(i);
                }
                for (int i = (rightBound + 1) * k; i <= r; i++) {
                    res += time(i);
                }
            } else {
                for (int i = l; i <= r; i++) {
                    res += time(i);
                }
            }
            return res;
        }

        private int getSum(int i) {
            int res = 0;
            for (int j = 0; j < 10; j++) {
                res += j * digits[j][i];
            }
            return res;
        }

        private void increaseSegment(int l, int r) {
            int leftBound = (l + k - 1) / k;
            int rightBound = r / k - 1;
            if (leftBound <= rightBound) {
                for (int i = leftBound; i <= rightBound; i++) {
                    increaseDigits(i);
                    extra[i]++;
                }
                for (int i = l; i < leftBound * k; i++) {
                    digits[time(i)][i / k]--;
                    clock[i] = (clock[i] + 1) % 10;
                    digits[time(i)][i / k]++;
                }
                for (int i = (rightBound + 1) * k; i <= r; i++) {
                    digits[time(i)][i / k]--;
                    clock[i] = (clock[i] + 1) % 10;
                    digits[time(i)][i / k]++;
                }
            } else {
                for (int i = l; i <= r; i++) {
                    digits[time(i)][i / k]--;
                    clock[i] = (clock[i] + 1) % 10;
                    digits[time(i)][i / k]++;
                }
            }
        }

        private void increaseDigits(int i) {
            int nines = digits[9][i];
            for (int j = 9; j >= 1; j--) {
                digits[j][i] = digits[j - 1][i];
            }
            digits[0][i] = nines;
        }

        private int time(int i) {
            return (clock[i] + extra[i / k]) % 10;
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