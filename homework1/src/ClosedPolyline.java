/**
 * Created by lionell on 11/1/15.
 *
 * @author Ruslan Sakevych
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class ClosedPolyline {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        Point[] m = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            m[i] = new Point(x, y, i + 1);
        }
        in.close();
        // Build desired polyline
        int[] order = PolylineBuilder.build(m);
        // Print polyline
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        if (order.length == 0) {
            out.println("No");
        } else {
            out.println("Yes");
            for (int index : order) {
                out.print(index);
                out.print(" ");
            }
        }
        out.close();
    }

    private static class Point implements Comparable<Point> {
        private int x;
        private int y;
        private int i;

        public Point(int x, int y, int i) {
            this.x = x;
            this.y = y;
            this.i = i;
        }

        public boolean lessThan(Point o) {
            return compareTo(o) < 0;
        }

        @Override
        public int compareTo(Point o) {
            if (equals(o)) {
                return 0;
            }
            return getX() < o.getX() ||
                    getX() == o.getX() && getY() < o.getY()
                    ? -1 : 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Point point = (Point) o;

            return getX() == point.getX() && getY() == point.getY();
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = getX();
            result = PRIME * result + getY();
            return result;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getI() {
            return i;
        }
    }

    private static class PolylineBuilder {
        private static final Comparator<Point> DESCENDING_ORDER =
                new Comparator<Point>() {
                    @Override
                    public int compare(Point o1, Point o2) {
                        return o2.compareTo(o1);
                    }
                };

        private static long direction(Point a, Point b, Point c) {
            return (long) (b.getX() - a.getX()) * (c.getY() - a.getY()) -
                    (long) (b.getY() - a.getY()) * (c.getX() - a.getX());
        }

        public static int[] build(Point[] points) {
            // Find points a and b
            // Size of points is greater than 1
            Point a = points[0];
            Point b = points[1];
            for (Point p : points) {
                if (p.lessThan(a)) {
                    a = p;
                }
                if (b.lessThan(p)) {
                    b = p;
                }
            }
            // Generate arrays mUp, mOn and mDown
            List<Point> mUp = new ArrayList<>();
            List<Point> mOn = new ArrayList<>();
            List<Point> mDown = new ArrayList<>();
            for (Point p : points) {
                long wedgeProduct = direction(a, b, p);
                if (wedgeProduct > 0) {
                    mUp.add(p);
                } else if (wedgeProduct < 0) {
                    mDown.add(p);
                } else if (!p.equals(a) && !p.equals(b)) {
                    mOn.add(p);
                }
            }
            // Check if we can build polyline
            if (mUp.isEmpty() && mDown.isEmpty()) {
                return new int[0];
            }
            // Extend arrays mUp and mDown
            if (mDown.size() == 0) {
                mDown.addAll(mOn);
            } else {
                mUp.addAll(mOn);
            }
            // Sort points in arrays mUp and mDown
            Collections.sort(mUp);
            Collections.sort(mDown, DESCENDING_ORDER);
            // Combine points to required polyline
            ArrayList<Point> polyline = new ArrayList<>();
            polyline.add(a);
            polyline.addAll(mUp);
            polyline.add(b);
            polyline.addAll(mDown);
            // Generate polyline vertexes order
            int[] order = new int[polyline.size()];
            for (int i = 0; i < polyline.size(); i++) {
                order[i] = polyline.get(i).getI();
            }
            return order;
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