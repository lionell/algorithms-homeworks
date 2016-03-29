import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by lionell on 10.02.16.
 *
 * @author Ruslan Sakevych
 */
public class Tricicle {
    public static void main(String[] args) {
        Reader in = new Reader();
        double x1 = in.nextDouble(), y1 = in.nextDouble(), r1 = in.nextDouble();
        double x2 = in.nextDouble(), y2 = in.nextDouble(), r2 = in.nextDouble();
        double x3 = in.nextDouble(), y3 = in.nextDouble(), r3 = in.nextDouble();
        in.close();

        double k1 = 1. / r1, k2 = 1. / r2, k3 = 1. / r3;
        double k4 = k1 + k2 + k3 + 2. * Math.sqrt(k1 * k2 + k2 * k3 + k3 * k1);
        double r4 = 1. / k4;

        Complex z1 = new Complex(x1, y1);
        Complex z2 = new Complex(x2, y2);
        Complex z3 = new Complex(x3, y3);
        Complex z4 = z1.mul(k1).add(z2.mul(k2)).add(z3.mul(k3));
        z4.add(z1.mul(z2).mul(k1 * k2).add(z2.mul(z3).mul(k2 * k3)).add(z1.mul(z3).mul(k1 * k3)).sqrt().mul(2.)).mul(r4);

        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.print(z4.re() + " " + z4.im() + " " + r4);
        out.close();
    }

    private static class Complex {
        private double re;
        private double im;

        public Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        public double re() {
            return re;
        }

        public void setRe(double re) {
            this.re = re;
        }

        public double im() {
            return im;
        }

        public void setIm(double im) {
            this.im = im;
        }

        public double abs() {
            return Math.sqrt(re * re + im * im);
        }

        public double arg() {
            return Math.atan2(im, re);
        }

        public Complex add(Complex c) {
            return new Complex(re + c.re, im + c.im);
        }

        public Complex mul(double d) {
            return new Complex(re * d, im * d);
        }

        public Complex mul(Complex c) {
            return new Complex(re * c.re + im * c.im, re * c.im + im * c.re);
        }

        public Complex sqrt() {
            double r = Math.sqrt(abs());
            double theta = arg() / 2;
            return new Complex(r * Math.cos(theta), r * Math.sin(theta));
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

        public double nextDouble() {
            return Double.parseDouble(nextToken());
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
