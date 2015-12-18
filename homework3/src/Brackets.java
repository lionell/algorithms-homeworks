import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by lionell on 11/29/15.
 * <p>
 * Idea
 * Let'sequence divide sequence into simple and another sequence(not always simple).
 * Sequence = Simple + Sequence
 * All sequences can be divided in such way.
 * So let'sequence iterate with m(stands for mid), and try to divide sequence [l, r]
 * into simple sequence [l, m] and another sequence [m + 1, r].
 * SequenceCount = sum m from l + 1 to r :
 * SimpleSequenceCount[l, m] * SequenceCount[m + 1, r]
 * Time complexity: O(N^3)
 * NOTE! We need O(N^2) additional memory to store already evaluated values.
 * </p>
 *
 * @author Ruslan Sakevych
 */
public class Brackets {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        String s = in.nextToken();
        in.close();
        CorrectBracketSequenceCounter counter =
                new CorrectBracketSequenceCounter(n, s);
        int count = counter.count();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.print(format(count, counter.isOverflow()));
        out.close();
    }

    private static String format(int count, boolean isOverflow) {
        if (isOverflow) {
            return String.format("%05d", count);
        }
        return String.format("%d", count);
    }

    private static class CorrectBracketSequenceCounter {
        private static final int MOD = 100000;
        private boolean overflow = false;
        private int n;
        private String sequence;
        private int[][] sequenceCache;
        private int[][] simpleSequenceCache;

        public CorrectBracketSequenceCounter(int n, String sequence) {
            this.n = n;
            this.sequence = sequence;
            initCache();
        }

        private void initCache() {
            final int MAX_SEQUENCE_LENGTH = n + 1;
            sequenceCache =
                    new int[MAX_SEQUENCE_LENGTH][MAX_SEQUENCE_LENGTH];
            simpleSequenceCache =
                    new int[MAX_SEQUENCE_LENGTH][MAX_SEQUENCE_LENGTH];
            for (int i = 0; i < MAX_SEQUENCE_LENGTH; i++) {
                for (int j = 0; j < MAX_SEQUENCE_LENGTH; j++) {
                    sequenceCache[i][j] = -1;
                    simpleSequenceCache[i][j] = -1;
                }
            }
        }

        public int count() {
            return getSequenceCount(0, n - 1);
        }

        private int getSequenceCount(int l, int r) {
            if (sequenceCache[l][r] == -1) {
                sequenceCache[l][r] = evaluateSequence(l, r);
            }
            return sequenceCache[l][r];
        }

        private int evaluateSequence(int l, int r) {
            if (l > r) {
                return 1;
            }
            if (l == r) {
                return 0;
            }
            long sum = 0L;
            for (int m = l + 1; m <= r; m++) {
                sum += (long) getSimpleSequenceCount(l, m) *
                        getSequenceCount(m + 1, r);
                sum = getModulo(sum);
            }
            return (int) sum;
        }

        private int getSimpleSequenceCount(int l, int r) {
            if (simpleSequenceCache[l][r] == -1) {
                simpleSequenceCache[l][r] = evaluateSimpleSequence(l, r);
            }
            return simpleSequenceCache[l][r];
        }

        private int evaluateSimpleSequence(int l, int r) {
            long sum = 0L;
            String brackets = "" + sequence.charAt(l) + sequence.charAt(r);
            if (brackets.equals("()") ||
                    brackets.equals("[]") ||
                    brackets.equals("{}") ||
                    brackets.equals("?)") ||
                    brackets.equals("?]") ||
                    brackets.equals("?}") ||
                    brackets.equals("(?") ||
                    brackets.equals("[?") ||
                    brackets.equals("{?")) {
                sum = 1L;
            }
            if (brackets.equals("??")) {
                sum = 3L;
            }
            sum *= getSequenceCount(l + 1, r - 1);
            sum = getModulo(sum);
            return (int) sum;
        }

        public boolean isOverflow() {
            return overflow;
        }

        private long getModulo(long x) {
            if (x > MOD) {
                overflow = true;
                x %= MOD;
            }
            return x;
        }
    }

    private static class Reader {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public Reader() {
            reader = new BufferedReader(new InputStreamReader(System.in));
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
