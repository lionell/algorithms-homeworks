import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by lionell on 3/29/16.
 * <p>
 * Main idea
 * Let's support a data structure that can find median
 * between vertices. Than we can just
 * go through all tests, and generate interesting vertices
 * numbers using generator from statement.
 * <p>
 * a_1 = median(a_0, 1)
 * a_2 = median(a_1, 2)
 * ...
 * a_q = median(a_(q-1), 1 + (q - 1) % n)
 * return a_1 + a_2 + ... + a_q
 * <p>
 * It's time figure out how to implement such data structure
 * that supports median(p, q) in O(log2(N)). Let's iterate
 * through tree with dfs and put time labels. So we can answer
 * if q is far child of p. Also let's store up[i][v] - 2^i parent of v.
 * So now we can find LCA(p, q) in O(log2(N)).
 * <p>
 * If we have query median(p, q) we can find their lca in O(log2(N)),
 * find distance as
 * height(p) + height(q) - 2 * height(lca)
 * then we can evaluate distance from highest of p and q to median.
 * Finally we can iterate to media in O(log2(N)) time using
 * up[i][j] array. Totally we need O(log2(N)) time on query.
 * <p>
 * We need also O(Nlog2(N) + M) time to build such data structure.
 * Also we need to store edges in adjacency list. But it's
 * only additional O(N) memory.
 * <p>
 * Time complexity:
 * Preprocessing: O(Nlog2(N))
 * Query: O(log2(N))
 * <p>
 * Memory: O(Nlog2(N))
 *
 * @author Ruslan Sakevych
 */
public class MediansInTree {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        int[] parents = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            parents[i] = in.nextInt();
        }
        int t = in.nextInt();
        TestSeries[] tests = new TestSeries[t];
        for (int i = 0; i < t; i++) {
            int a0 = in.nextInt();
            int q = in.nextInt();
            tests[i] = new TestSeries(a0, q, n);
        }
        in.close();

        Tree tree = new QuickTree(n, parents);
        long[] answers = new long[t];
        for (int i = 0; i < t; i++) {
            answers[i] = tests[i].runTest(tree);
        }

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        for (long answer : answers) {
            out.println(answer);
        }
        out.close();
    }

    private static class QuickTree implements Tree {
        private final int[] inTime;
        private final int[] outTime;
        private int timer = 0;

        private final int MAX_DEPTH;
        private final int[] heights;
        private final int[][] up;
        private final Map<Integer, List<Integer>> adjacency;

        private QuickTree(int n, int[] parents) {
            inTime = new int[n];
            outTime = new int[n];

            heights = new int[n];
            MAX_DEPTH = (int) (Math.log(n) / Math.log(2));
            up = new int[MAX_DEPTH + 1][n];

            adjacency = new HashMap<>();
            for (int i = 0; i < n; i++) {
                adjacency.put(i, new ArrayList<>());
            }
            for (int i = 0; i < parents.length; i++) {
                adjacency.get(parents[i] - 1).add(i + 1);
            }

            dfs(0, 0, 0);
        }

        private void dfs(int v, int parent, int height) {
            inTime[v] = timer++;

            heights[v] = height;
            up[0][v] = parent;
            for (int i = 1; i <= MAX_DEPTH; i++) {
                up[i][v] = up[i - 1][up[i - 1][v]];
            }
            for (int to : adjacency.get(v)) {
                dfs(to, v, height + 1);
            }

            outTime[v] = timer++;
        }

        private boolean isParent(int p, int q) {
            return inTime[p] <= inTime[q]
                    && outTime[q] <= outTime[p];
        }

        private int findLCA(int p, int q) {
            if (isParent(p, q)) {
                return p;
            }
            if (isParent(q, p)) {
                return q;
            }
            for (int jump = MAX_DEPTH; jump >= 0; jump--) {
                if (!isParent(up[jump][p], q)) {
                    p = up[jump][p];
                }
            }
            return up[0][p];
        }

        private int goUp(int p, int l) {
            int i = 0;
            while (l > 0) {
                if (l % 2 == 1) {
                    p = up[i][p];
                }
                l /= 2;
                i++;
            }
            return p;
        }

        @Override
        public int findMedian(int p, int q) {
            int lca = findLCA(p, q);
            int m = (heights[p] + heights[q] - 2 * heights[lca]);
            if (heights[p] >= heights[q]) {
                return goUp(p, m / 2);
            }
            // don't forget odd distance
            return goUp(q, m / 2 + m % 2);
        }
    }

    private static class TestSeries {
        private final int a0;
        private final int q;
        private final int n;

        private TestSeries(int a0, int q, int n) {
            this.a0 = a0;
            this.q = q;
            this.n = n;
        }

        private long runTest(Tree tree) {
            long sum = 0;
            int lastVertex = a0;
            for (int i = 0; i < q; i++) {
                int nextVertex = 1 + i % n;
                lastVertex =
                        tree.findMedian(lastVertex - 1, nextVertex - 1) + 1;
                sum += lastVertex;
            }
            return sum;
        }
    }

    private interface Tree {
        int findMedian(int p, int q);
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
