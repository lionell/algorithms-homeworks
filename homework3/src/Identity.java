import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * Created by lionell on 04.12.2015.
 *
 * @author Ruslan Sakevych
 */
public class Identity {
    public static void main(String[] args) {
        Reader in = new Reader();
        String identity = in.nextToken();
        in.close();
        String[] temp = identity.split("=");
        String s = temp[0];
        int sum = Integer.parseInt(temp[1]);
        String ans = new Solution().run(s, sum);
        PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
        out.println(ans);
        out.close();
    }

    private static class Solution {
        private int[][] minCount;
        private int[][] from;
        private int[] zeroes;

        public Solution() {
        }

        private void init(String s, int desiredSum) {
            int len = s.length();
            minCount = new int[len + 1][desiredSum + 1];
            from = new int[len + 1][desiredSum + 1];
            for (int i = 0; i < len + 1; i++) {
                for (int j = 0; j < desiredSum + 1; j++) {
                    minCount[i][j] = -1;
                    from[i][j] = -1;
                }
            }
        }

        public String run(String s, int desiredSum) {
            String comp = compress(s);
            init(comp, desiredSum);
            eval(comp, desiredSum);
            return decompress(recover(comp, desiredSum), desiredSum);
        }

        private String compress(String s) {
            int len = s.length();
            zeroes = new int[len + 1];
            StringBuilder builder = new StringBuilder();
            int count = 0;
            int index = -1;
            for (char c : s.toCharArray()) {
                if (c == '0') {
                    count++;
                } else {
                    if (count > 4) {
                        zeroes[index] = count - 4;
                    }
                    count = 0;
                }
                if (count > 4) {
                    continue;
                }
                index++;
                builder.append(c);
            }
            if (count > 4) {
                zeroes[index] = count - 4;
            }
            return builder.toString();
        }

        private String decompress(ArrayList<String> list, int desiredSum) {
            StringBuilder builder = new StringBuilder();
            int index = 0;
            for (String el : list) {
                if (index > 0) {
                    builder.append('+');
                }
                for (char c : el.toCharArray()) {
                    builder.append(c);
                    if (zeroes[index] > 0) {
                        for (int i = 0; i < zeroes[index]; i++) {
                            builder.append('0');
                        }
                    }
                    index++;
                }
            }
            builder.append('=');
            builder.append(desiredSum);
            return builder.toString();
        }

        private void eval(String s, int desiredSum) {
            int len = s.length();
            minCount[0][0] = 0;
            for (int r = 1; r <= len; r++) {
                for (int sum = 0; sum <= desiredSum; sum++) {
                    int l = r - 1;
                    int cur = 0;
                    int power = 1;
                    do {
                        cur = cur + power * (s.charAt(l) - '0');
                        int add = (l > 0) ? 1 : 0;
                        if (sum - cur >= 0
                                && minCount[l][sum - cur] != -1
                                && (minCount[r][sum] == -1 || minCount[r][sum] > minCount[l][sum - cur] + add)) {
                            minCount[r][sum] = minCount[l][sum - cur] + add;
                            from[r][sum] = l;
                        }
                        power *= 10;
                        l--;
                    } while (l >= 0 && cur <= sum);
                }
            }
        }

        private ArrayList<String> recover(String s, int desiredSum) {
            int len = s.length();
            ArrayList<String> arr = new ArrayList<>();
            int sum = desiredSum;
            int r = len;
            int l;
            do {
                l = from[r][sum];
                String x = s.substring(l, r);
                arr.add(x);
                sum -= Integer.parseInt(x);
                r = l;
            } while (l > 0);
            Collections.reverse(arr);
            return arr;
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

        public long nextLong() {
            return Long.parseLong(nextToken());
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
