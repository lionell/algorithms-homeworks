import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by lionell on 12.02.16.
 * <p>
 * Idea
 * Let's represent every rect as two vertical sides,
 * e.g. (x1=1, y1=1, x2=3, y2=4) -> [(x=1, y1=1, y2=4), (x=3, y1=1, y2=4)]
 * Now let's sort this array by x-axis.
 * Finally we can iterate through sorted array and calculate area as
 * area = sum of section lengths multiplied by distances between x-axis points.
 * So we need a structure that can easily handle segments and evaluate their union(see @class Section).
 * We can have a sorted set of segment points and iterate through it every time we need length.
 * I used TreeSet - Java implementation of sorted set.
 * <p>
 * Time complexity:
 * - Section.update: O(log(n))
 * - Section.getLength: O(n)
 * - RectUnion.main: O(n^2)
 * <p>
 * Space complexity: O(n)
 * </p>
 *
 * @author Ruslan Sakevych
 */
public class RectUnion {
    public static void main(String[] args) {
        Reader in = new Reader();
        int n = in.nextInt();
        ArrayList<Side> sides = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int x1 = in.nextInt();
            int y1 = in.nextInt();
            int x2 = in.nextInt();
            int y2 = in.nextInt();
            if (y1 > y2) {
                int t = y1;
                y1 = y2;
                y2 = t;
            }
            Segment bound = new Segment(y1, y2, i);
            sides.add(new Side(x1, bound));
            sides.add(new Side(x2, bound));
        }
        in.close();

        Collections.sort(sides);
        Section section = new Section();
        int last = 0;
        long area = 0;
        for (Side side : sides) {
            area += (side.getX() - last) * section.getLength();
            section.update(side.getSegment());
            last = side.getX();
        }

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.print(area);
        out.close();
    }

    private static class Side implements Comparable<Side> {
        private int x;
        private Segment segment;

        public Side(int x, Segment segment) {
            this.x = x;
            this.segment = segment;
        }

        public int getX() {
            return x;
        }

        public Segment getSegment() {
            return segment;
        }

        @Override
        public int compareTo(Side s) {
            if (equals(s)) {
                return 0;
            }
            return (getX() < s.getX()
                    || getX() == s.getX() && getSegment().compareTo(s.getSegment()) < 0)
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

            Side side = (Side) o;

            return getX() == side.getX()
                    && getSegment().equals(side.getSegment());

        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = getX();
            result = PRIME * result + getSegment().hashCode();
            return result;
        }
    }

    private static class Segment implements Comparable<Segment> {
        private int l;
        private int r;
        private int id;

        public Segment(int l, int r, int id) {
            this.l = l;
            this.r = r;
            this.id = id;
        }

        public int getL() {

            return l;
        }

        public int getR() {
            return r;
        }

        public int getId() {
            return id;
        }

        @Override
        public int compareTo(Segment s) {
            if (equals(s)) {
                return 0;
            }
            return (getL() < s.getL()
                    || getL() == s.getL() && getR() < s.getR()
                    || getL() == s.getL() && getR() == s.getR() && s.getId() < s.getId())
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

            Segment segment = (Segment) o;

            return getL() != segment.getL()
                    && getR() != segment.getR()
                    && getId() == segment.getId();

        }

        @Override
        public int hashCode() {
            final int PRIME = 31;
            int result = getL();
            result = PRIME * result + getR();
            result = PRIME * result + getId();
            return result;
        }
    }

    private static class Section {
        private Set<Bound> bounds = new TreeSet<>();

        public void update(Segment s) {
            Bound l = new Bound(s.getL(), s.getId());
            Bound r = new Bound(s.getR(), s.getId());
            if (bounds.contains(l) && bounds.contains(r)) {
                bounds.remove(l);
                bounds.remove(r);
            } else {
                bounds.add(l);
                bounds.add(r);
            }
        }

        public long getLength() {
            Set<Integer> ids = new HashSet<>();
            int last = Integer.MIN_VALUE;
            long length = 0;
            for (Bound b : bounds) {
                if (!ids.isEmpty()) {
                    length += b.getX() - last;
                }
                if (ids.contains(b.getId())) {
                    ids.remove(b.getId());
                } else {
                    ids.add(b.getId());
                }
                last = b.getX();
            }
            return length;
        }

        private static class Bound implements Comparable<Bound> {
            private int x;
            private int id;

            public Bound(int x, int id) {
                this.x = x;
                this.id = id;
            }

            public int getX() {
                return x;
            }

            public int getId() {
                return id;
            }

            @Override
            public int compareTo(Bound b) {
                if (equals(b)) {
                    return 0;
                }
                return (getX() < b.getX() || getX() == b.getX() && getId() < b.getId()) ? -1 : 1;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }

                Bound bound = (Bound) o;

                return getX() == bound.getX() && getId() == bound.getId();

            }

            @Override
            public int hashCode() {
                final int PRIME = 31;
                int result = getX();
                result = PRIME * result + getId();
                return result;
            }
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

        public void close() {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
