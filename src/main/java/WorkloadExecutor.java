import java.util.Random;

public class WorkloadExecutor {

    public static void runW1DynamicArray(int n) {
        DynamicArray da = new DynamicArray();
        Random fillRand = new Random(42);
        for (int i = 0; i < n; i++) {
            da.add(fillRand.nextInt());
        }

        Metrics.reset();
        Random queryRand = new Random(42);
        for (int i = 0; i < 10000; i++) {
            da.get(queryRand.nextInt(n));
        }
    }

    public static void runW1LinkedList(int n) {
        MyLinkedList list = new MyLinkedList();
        Random fillRand = new Random(42);
        for (int i = 0; i < n; i++) {
            list.add(fillRand.nextInt());
        }

        Metrics.reset();
        Random queryRand = new Random(42);
        for (int i = 0; i < 10000; i++) {
            list.get(queryRand.nextInt(n));
        }
    }

    public static void runW2DynamicArray(int n) {
        DynamicArray da = new DynamicArray();
        Random fillRand = new Random(42);
        int[] present = new int[500];

        for (int i = 0; i < n; i++) {
            int val = fillRand.nextInt(1_000_000) * 2;
            da.add(val);
            if (i < 500) {
                present[i] = val;
            }
        }

        int[] queries = prepareSearchQueries(present);

        Metrics.reset();
        for (int q : queries) {
            da.contains(q);
        }
    }

    public static void runW2LinkedList(int n) {
        MyLinkedList list = new MyLinkedList();
        Random fillRand = new Random(42);
        int[] present = new int[500];

        for (int i = 0; i < n; i++) {
            int val = fillRand.nextInt(1_000_000) * 2;
            list.add(val);
            if (i < 500) {
                present[i] = val;
            }
        }

        int[] queries = prepareSearchQueries(present);

        Metrics.reset();
        for (int q : queries) {
            list.contains(q);
        }
    }

    public static void runW3DynamicArray(int n, String variant) {
        DynamicArray da = new DynamicArray();
        Random fillRand = new Random(42);
        for (int i = 0; i < n; i++) {
            da.add(fillRand.nextInt());
        }

        Metrics.reset();
        int targetIndex = variant.equals("head") ? 0 : da.size() / 2;

        for (int i = 0; i < 1000; i++) {
            da.add(targetIndex, i);
        }
        for (int i = 0; i < 1000; i++) {
            da.remove(targetIndex);
        }
    }

    public static void runW3LinkedList(int n, String variant) {
        MyLinkedList list = new MyLinkedList();
        Random fillRand = new Random(42);
        for (int i = 0; i < n; i++) {
            list.add(fillRand.nextInt());
        }

        Metrics.reset();
        int targetIndex = variant.equals("head") ? 0 : list.size() / 2;

        for (int i = 0; i < 1000; i++) {
            list.add(targetIndex, i);
        }
        for (int i = 0; i < 1000; i++) {
            list.remove(targetIndex);
        }
    }

    public static void runW4MinHeap(int n) {
        MinHeap heap = new MinHeap();
        Random fillRand = new Random(42);

        Metrics.reset();
        for (int i = 0; i < n; i++) {
            heap.insert(fillRand.nextInt());
        }

        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int minVal = heap.extractMin();
            if (minVal < prev) {
                throw new IllegalStateException("MinHeap invariant violated: " + minVal + " < " + prev);
            }
            prev = minVal;
        }
    }

    private static int[] prepareSearchQueries(int[] present) {
        int[] queries = new int[1000];
        System.arraycopy(present, 0, queries, 0, 500);

        Random queryRand = new Random(100);
        for (int i = 500; i < 1000; i++) {
            queries[i] = queryRand.nextInt(1_000_000) * 2 + 1;
        }

        Random shuffleRand = new Random(42);
        for (int i = queries.length - 1; i > 0; i--) {
            int j = shuffleRand.nextInt(i + 1);
            int temp = queries[i];
            queries[i] = queries[j];
            queries[j] = temp;
        }
        return queries;
    }
}