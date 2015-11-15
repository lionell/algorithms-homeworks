/**
 * Created by lionell on 11/10/15.
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
import java.util.Arrays;
import java.util.StringTokenizer;

public class Triangles {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            points[i] = new Point(x, y);
        }
        in.close();

        long triangleCount = countTriangles(points);

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.print(triangleCount);
        out.close();
    }

    private static long countTriangles(Point[] points) {
        int n = points.length;
        if (n < 3) {
            return 0;
        }
        long count = 0;
        for (int i = 0; i < n; i++) {
            Point[] newPoints = buildPoints(points, i);
            Arrays.sort(newPoints);
            int firstLower = 0;
            int firstUpper = 0;
            int secondLower = 1;
            int secondUpper = 2;
            do {
                firstLower = firstUpper;
                firstUpper = updateUpperBound(newPoints, firstUpper);
                Point first = newPoints[firstLower];
                while (getExteriorProduct(first, newPoints[secondLower]) >= 0 &&
                        getDotProduct(first, newPoints[secondLower]) > 0) {
                    secondLower++;
                    if (secondLower == n - 1) {
                        secondLower = 0;
                    }
                    if (secondLower == firstLower) {
                        break;
                    }
                }
                secondUpper = secondLower;
                secondUpper = updateUpperBound(newPoints, secondUpper);
                Point second = newPoints[secondLower];
                if (getExteriorProduct(first, second) >= 0 &&
                        getDotProduct(first, second) == 0) {
                    count += (long)(firstUpper - firstLower) *
                            (long)(secondUpper - secondLower);
                }
            } while (firstUpper < n - 1);
        }
        return count;
    }

    private static Point[] buildPoints(Point[] points, int originIndex) {
        Point origin = points[originIndex];
        Point[] newPoints = new Point[points.length - 1];
        for (int j = 0; j < originIndex; j++) {
            newPoints[j] = getVector(origin, points[j]);
        }
        for (int j = originIndex + 1; j < points.length; j++) {
            newPoints[j - 1] = getVector(origin, points[j]);
        }
        return newPoints;
    }

    private static int updateUpperBound(Point[] points, int upperBound) {
        do {
            upperBound++;
        } while(upperBound < points.length &&
                points[upperBound] == points[upperBound - 1]);
        return upperBound;
    }

    private static Point getVector(Point origin, Point p) {
        return new Point(p.getX() - origin.getX(), p.getY() - origin.getY());
    }

    private static long getDotProduct(Point v1, Point v2) {
        return (long)v1.getX() * v2.getX() + (long)v1.getY() * v2.getY();
    }

    private static long getExteriorProduct(Point v1, Point v2) {
        return (long)v1.getX() * v2.getY() - (long)v2.getX() * v1.getY();
    }

    private static class Point implements Comparable {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Object o) {
            Point p = (Point) o;
            double angle = p.getAngle() - getAngle();
            if (angle == 0) {
                return Long.compare(getLength(), p.getLength());
            }
            return angle > 0 ? -1 : 1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public long getLength() {
            return (long)x * x + (long)y * y;
        }

        public double getAngle() {
            double angle = Math.atan2(y, x);
            if (angle < 0) {
                angle += 2 * Math.PI;
            }
            return angle;
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

            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = x;
            result = PRIME * result + y;
            return result;
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
