package protolabs.com.github;

/*
 * Copyright 2018-now Marcus Pinnecke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    static Random rand = new Random();

    public static void main(String[] args) {

        String set = args[0];
        String model = args[1];
        Integer n = Integer.valueOf(args[2]);
        Integer m = Integer.valueOf(args[3]);

        if (set.compareTo("structured") == 0) {
            if (model.compareTo("coc") == 0) {
                createStructuredCoc(n, m, Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else {
                createStructuredKvc(n, m, Integer.MIN_VALUE, Integer.MAX_VALUE);
            }
        } else if (set.compareTo("bound-structured") == 0) {
            Integer l = Integer.valueOf(args[4]);
            Integer u = Integer.valueOf(args[5]);
            if (model.compareTo("coc") == 0) {
                createStructuredCoc(n, m, l, u);
            } else {
                createStructuredKvc(n, m, l, u);
            }
        } else if (set.compareTo("unstructured") == 0) {
            Integer nKeys = Integer.valueOf(args[4]);
            if (model.compareTo("coc") == 0) {
                createUnstructuredCoc(n, m, nKeys);
            } else {
                createUnstructuredKvc(n, m, nKeys);
            }
        } else if (set.compareTo("document") == 0) {
            Integer k = Integer.valueOf(args[4]);
            if (model.compareTo("coc") == 0) {
                createDocumentsCoc(n, m, k);
            } else {
                createDocumentsKvc(n, m, k);
            }
        }
    }

    private static void createDocumentsKvc(Integer n, Integer m, Integer k) {
        m = m < n ? n : m;
        List<String> strings = randomStrings(m);
        System.out.print("{");
        System.out.print("\"x\": [");
        for (int i = 0; i < n; i++) {
            System.out.print("\"" + makeSentence(strings, k) + "\"" + (i + 1 < n ? ", " : ""));
        }
        System.out.print("], ");

        int N = Math.max(1, rand.nextInt(n));
        List<Boolean> vals = new ArrayList<>();
        for (int j = 0; j < n; j++) {
            vals.add((j < N));
        }
        Collections.shuffle(vals);

        for (int i = 0; i < m; i++) {
            System.out.print("\"a" + i + "\": [");
            for (int j = 0; j < n; j++) {
                if (vals.get(j)) {
                    System.out.print("\"" + makeSentence(strings, k) + "\"" + (j + 1 < n ? ", " : ""));
                } else {
                    System.out.print("null" + (j + 1 < n ? ", " : ""));
                }
            }
            System.out.print("]" + (i + 1 < m ? ", " : ""));
        }

        if (m > 0) {
            System.out.print(", ");
        }

        System.out.print("\"y\": [");
        for (int i = 0; i < n; i++) {
            System.out.print("\"" + strings.get(i) + "\"" + (i + 1 < n ? ", " : ""));
        }
        System.out.print("]");
        System.out.print("}");
    }

    private static void createDocumentsCoc(Integer n, Integer m, Integer k) {
        m = m < n ? n : m;
        List<String> strings = randomStrings(m + 1);
        for (int i = 0; i < n; i++) {
            System.out.print("{");
            List<String> workingStrings = new ArrayList<>(strings);
            Collections.shuffle(workingStrings);
            String yValue = workingStrings.get(0);
            workingStrings.remove(0);
            String xValue = makeSentence(workingStrings, m);
            System.out.print("\"x\": \"" + xValue + "\", ");
            int K = rand.nextInt(k);
            for (int j = 0; j < K; j++) {
                System.out.print("\"a" + j + "\": \"" + makeSentence(workingStrings, m) + "\", ");
            }
            System.out.print("\"y\": \"" + yValue + "\"");
            System.out.print("}\n");
        }
    }

    private static String makeSentence(List<String> workingStrings, int m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int idx = rand.nextInt(workingStrings.size());
            sb.append(workingStrings.get(idx));
            if (i + 1 < m) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private static void createUnstructuredKvc(Integer n, Integer m, Integer nKeys) {
        System.out.print("{");
        List<String> keys = randomStrings(nKeys + 1);
        for (int i = 0; i < nKeys; i++) {
            String key = keys.get(i);
            System.out.print("\"" + key + "\": [");
            int M = Math.max(1, rand.nextInt(m));
            List<Boolean> vals = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                vals.add((j < M));
            }
            Collections.shuffle(vals);

            for (int j = 0; j < n; j++) {
                if (vals.get(j)) {
                    System.out.print(rand.nextInt() + (j + 1 < n ? ", " : ""));
                } else {
                    System.out.print("null" + (j + 1 < n ? ", " : ""));
                }
            }
            System.out.print("]" + (i + 1 < nKeys ? ", " : ""));
        }
        System.out.print("}");
    }

    static private List<String> randomStrings(Integer nKeys) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < nKeys; i++) {
            result.add(RandomStringUtils.randomAlphanumeric(5 + rand.nextInt(10)));
        }
        return result;
    }

    private static void createUnstructuredCoc(Integer n, Integer m, Integer nKeys) {
        List<String> keys = randomStrings(nKeys + 1);
        for (int i = 0; i < n; i++) {
            int M = Math.max(1, rand.nextInt(m));
            List<String> thisKeys = new ArrayList<>(keys);
            System.out.print("{ ");
            for (int j = 0; j < M; j++) {
                int idx = rand.nextInt(thisKeys.size());
                String key = thisKeys.get(idx);
                thisKeys.remove(idx);
                System.out.print("\"" + key + "\": " + rand.nextInt() + ((j + 1 < M ? ", " : "")));
            }
            System.out.print("}\n");
        }
    }

    private static void createStructuredKvc(Integer n, Integer m, Integer lower, Integer upper) {
        System.out.print("{");

        System.out.print("\"x\": [");
        for (int j = 0; j < n; j++) {
            System.out.print(ThreadLocalRandom.current().nextInt(lower, upper) + (j + 1 < n ? ", " : ""));
        }
        System.out.print("]" + ((m > 0) ? ", " : ""));

        for (int i = 0; i < m; i++) {
            System.out.print("\"a" + i + "\": [");
            for (int j = 0; j < n; j++) {
                System.out.print(ThreadLocalRandom.current().nextInt(lower, upper) + (j + 1 < n ? ", " : ""));
            }
            System.out.print("]" + ((i + 1 < m) ? ", " : ""));
        }
        System.out.print("}");
    }

    private static void createStructuredCoc(Integer n, Integer m, Integer lower, Integer upper) {
        for (int i = 0; i < n; i++) {
            System.out.print("{\"x\": " + ThreadLocalRandom.current().nextInt(lower, upper) + ((m > 0) ? ", " : ""));
            for (int j = 0; j < m; j++) {
                System.out.print("\"a" + j + "\": " + ThreadLocalRandom.current().nextInt(lower, upper) + ((j + 1 < m ? ", " : "")));
            }
            System.out.print("}\n");
        }
    }
}
