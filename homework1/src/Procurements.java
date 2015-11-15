/**
 * Created by lionell on 11/1/15.
 *
 * @author Ruslan Sakevych
 */

import com.sun.istack.internal.NotNull;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Procurements {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        int k = in.nextInt();
        Offer[] offers = new Offer[n];
        for (int i = 0; i < n; i++) {
            int p = in.nextInt();
            int q = in.nextInt();
            offers[i] = new Offer(p, q, i + 1);
        }
        in.close();
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        int[] indexes = OffersOptimizer.optimize(offers, k);
        for (int index : indexes) {
            out.print(index);
            out.print(" ");
        }
        out.close();
    }

    private static class Offer {
        private int p;
        private int q;
        private int i;

        public Offer(int p, int q, int i) {
            this.p = p;
            this.q = q;
            this.i = i;
        }

        public int getP() {
            return p;
        }

        public int getQ() {
            return q;
        }

        public int getI() {
            return i;
        }
    }

    private static class OffersOptimizer {
        private static boolean check(Offer[] offers, int k, double alpha) {
            Arrays.sort(offers, new ParametrizedComparator(alpha));
            double sum = 0.0;
            for (int i = 0; i < k; i++) {
                sum += offers[i].getQ() - alpha * offers[i].getP();
            }
            return sum > 0.0;
        }

        @NotNull
        public static int[] optimize(Offer[] offers, int k) {
            final double EPS = 1e-10;
            final double INF = 1e10;
            double left = 0.0;
            double right = INF;
            while (right - left > EPS) {
                double middle = left + (right - left) / 2;
                if (check(offers, k, middle)) {
                    left = middle;
                } else {
                    right = middle;
                }
            }
            int[] optIndexes = new int[k];
            for (int i = 0; i < k; i++) {
                optIndexes[i] = offers[i].getI();
            }
            return optIndexes;
        }

        private static class ParametrizedComparator implements
                Comparator<Offer> {
            private final double alpha;

            public ParametrizedComparator(double alpha) {
                this.alpha = alpha;
            }

            // Descending order
            @Override
            public int compare(Offer o1, Offer o2) {
                return Double.compare(o2.getQ() - alpha * o2.getP(),
                        o1.getQ() - alpha * o1.getP());
            }
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

        private String nextToken() {
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
