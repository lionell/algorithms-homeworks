/**
 * Created by lionell on 11/3/15.
 *
 * @author Ruslan Sakevych
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
import java.util.StringTokenizer;

public class Hallway {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        int w = in.nextInt();
        Column[] columns = new Column[n];
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            int r = in.nextInt();
            columns[i] = new Column(x, y, r);
        }
        in.close();

        double tableRadius = findTableRadius(columns, w);

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.println(2 * tableRadius);
        out.close();
    }

    private static double findTableRadius(Column[] columns, int w) {
        final double INF = 1e12;
        final double EPS = 1e-4;
        double left = 0;
        double right = INF;
        while (right - left >= EPS) {
            double middle = left + (right - left) / 2;
            if (check(columns, w, middle)) {
                left = middle;
            } else {
                right = middle;
            }
        }
        return left;
    }

    private static boolean check(Column[] columns, int w, double delta) {
        Emulator emulator = new Emulator(columns, w, delta);
        return emulator.check();
    }

    private static class Emulator {
        private final Column[] columns;
        private final int n;
        private final boolean[] visitedColumns;
        private final int w;
        private final double delta;

        public Emulator(Column[] columns, int w, double delta) {
            this.columns = columns;
            this.n = columns.length;
            this.w = w;
            this.delta = delta;
            visitedColumns = new boolean[n];
        }

        public boolean check() {
            if (2 * delta > w) {
                return false;
            }
            // Start search from the left side
            for (int i = 0; i < n; i++) {
                if (columns[i].getX() - columns[i].getR() <= 2 * delta) {
                    dfs(i);
                }
            }
            // Check columns on the right side
            for (int i = 0; i < n; i++) {
                if (columns[i].getX() + columns[i].getR() >= w - 2 * delta
                        && visitedColumns[i]) {
                    return false;
                }
            }
            return true;
        }

        private void dfs(int index) {
            if (visitedColumns[index]) {
                return;
            }
            visitedColumns[index] = true;
            for (int i = 0; i < n; i++) {
                if (intersects(index, i)) {
                    dfs(i);
                }
            }
        }

        private boolean intersects(int index1, int index2) {
            Column a = columns[index1];
            Column b = columns[index2];
            double actualSquaredDistance =
                    Math.pow(a.getX() - b.getX(), 2) +
                            Math.pow(a.getY() - b.getY(), 2);
            double maxSquaredDistance =
                    Math.pow(a.getR() + b.getR() + 2 * delta, 2);
            return actualSquaredDistance <= maxSquaredDistance;
        }
    }

    private static class Column {
        private int x;
        private int y;
        private int r;

        public Column(int x, int y, int r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getR() {
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

        private String nextToken() {
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
