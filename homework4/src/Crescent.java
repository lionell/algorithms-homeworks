import java.io.BufferedOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by lionell on 11.02.16.
 *
 * @author Ruslan Sakevych
 */
public class Crescent {
    private static int r;
    private static int c;
    private static char[][] world;
    private static int[][] waterWorld;
    private static int[][] heroWorld;
    private static Queue<Point> waterQueue = new LinkedList<>();
    private static Queue<Point> heroQueue = new LinkedList<>();
    private static Point[] directions = {new Point(-1, 0), new Point(1, 0), new Point(0, -1), new Point(0, 1)};
    private static Point finish;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        r = in.nextInt();
        c = in.nextInt();
        in.nextLine();
        world = new char[r][c];
        waterWorld = new int[r][c];
        heroWorld = new int[r][c];
        for (int i = 0; i < r; i++) {
            String t = in.nextLine();
            for (int j = 0; j < c; j++) {
                waterWorld[i][j] = -1;
                heroWorld[i][j] = -1;
                world[i][j] = t.charAt(j);
                if (world[i][j] == '*') {
                    waterQueue.add(new Point(i, j));
                    waterWorld[i][j] = 0;
                }
                if (world[i][j] == '@') {
                    heroQueue.add(new Point(i, j));
                    heroWorld[i][j] = 0;
                }
                if (world[i][j] == 'D') {
                    finish = new Point(i, j);
                }
            }
        }
        in.close();
        // It's time to spread water
        while (!waterQueue.isEmpty()) {
            Point w = waterQueue.remove();
            for (Point d : directions) {
                Point n = w.add(d);
                if (checkBounds(n.i, n.j)
                        && (world[n.i][n.j] == '.' || world[n.i][n.j] == '@')
                        && waterWorld[n.i][n.j] == -1) {
                    waterQueue.add(n);
                    waterWorld[n.i][n.j] = waterWorld[w.i][w.j] + 1;
                }
            }
        }
        // Now let's move hero
        while (!heroQueue.isEmpty() && heroWorld[finish.i][finish.j] == -1) {
            Point h = heroQueue.remove();
            for (Point d : directions) {
                Point n = h.add(d);
                if (checkBounds(n.i, n.j) && (world[n.i][n.j] == '.' || world[n.i][n.j] == 'D')
                        && (waterWorld[n.i][n.j] > heroWorld[h.i][h.j] + 1 || waterWorld[n.i][n.j] == -1)
                        && heroWorld[n.i][n.j] == -1) {
                    heroQueue.add(n);
                    heroWorld[n.i][n.j] = heroWorld[h.i][h.j] + 1;
                }
            }
        }
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        if (heroWorld[finish.i][finish.j] == -1) {
            out.print("Impossible");
        } else {
            out.print(heroWorld[finish.i][finish.j]);
        }
        out.close();
    }

    public static boolean checkBounds(int i, int j) {
        return i >= 0 && i < r && j >= 0 && j < c;
    }

    public static class Point {
        public int i;
        public int j;

        public Point(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Point add(Point p) {
            return new Point(i + p.i, j + p.j);
        }
    }
}
