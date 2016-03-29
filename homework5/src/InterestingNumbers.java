import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by lionell on 10.02.16.
 *
 * @author Ruslan Sakevych
 */
public class InterestingNumbers {
    public static void main(String[] args) {
        Reader in = new Reader();
        long n = in.nextInt();
        in.close();

        long[] divs = factor(n);
        Arrays.sort(divs);
        long k = 1;
        ArrayList<Long> ans = new ArrayList<>();
        while (divs[1] * k < n) {
            long x = divs[1] * k;
            if ((x - 1) % divs[0] == 0) {
                ans.add(x);
            }
            if ((x + 1) % divs[0] == 0) {
                ans.add(x + 1);
            }
            k++;
        }

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.print("0 1 ");
        for (long x : ans) {
            out.print(x + " ");
        }
        out.close();
    }

    public static long[] factor(long n) {
        long root = (long) Math.ceil(Math.sqrt(n));
        for (long d = 2; d <= root; d++) {
            if (n % d == 0) {
                return new long[]{d, n / d};
            }
        }
        return new long[]{};
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

        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
