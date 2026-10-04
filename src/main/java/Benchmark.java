import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Benchmark {

    private static final String CSV_FILE = "results/results.csv";

    public static void main(String[] args) {
        ensureResultsDirectoryExists();
        initCsvFile();

        int[] sizes = {100, 1000, 10000, 100000};

        for (int n : sizes) {
            System.out.println("Running benchmarks for n = " + n + "...");

            // W1 - Random Access
            runBenchmark("W1", "-", "DynamicArray", n, () -> WorkloadExecutor.runW1DynamicArray(n));
            runBenchmark("W1", "-", "MyLinkedList", n, () -> WorkloadExecutor.runW1LinkedList(n));

            // W2 - Search
            runBenchmark("W2", "-", "DynamicArray", n, () -> WorkloadExecutor.runW2DynamicArray(n));
            runBenchmark("W2", "-", "MyLinkedList", n, () -> WorkloadExecutor.runW2LinkedList(n));

            // W3 - Insert & Remove (head)
            runBenchmark("W3", "head", "DynamicArray", n, () -> WorkloadExecutor.runW3DynamicArray(n, "head"));
            runBenchmark("W3", "head", "MyLinkedList", n, () -> WorkloadExecutor.runW3LinkedList(n, "head"));

            // W3 - Insert & Remove (middle)
            runBenchmark("W3", "middle", "DynamicArray", n, () -> WorkloadExecutor.runW3DynamicArray(n, "middle"));
            runBenchmark("W3", "middle", "MyLinkedList", n, () -> WorkloadExecutor.runW3LinkedList(n, "middle"));

            // W4 - Priority Processing
            runBenchmark("W4", "-", "MinHeap", n, () -> WorkloadExecutor.runW4MinHeap(n));
        }

        System.out.println("Benchmark completed! Results saved to " + CSV_FILE);
    }

    private static void runBenchmark(String workload, String variant, String structure, int n, Runnable task) {
        Metrics.reset();
        task.run();

        double[] timesMs = new double[5];
        long finalSteps = 0;
        long finalMoves = 0;
        long finalComparisons = 0;

        for (int i = 0; i < 5; i++) {
            Metrics.reset();

            long start = System.nanoTime();
            task.run();
            long end = System.nanoTime();

            timesMs[i] = (end - start) / 1_000_000.0;

            finalSteps = Metrics.steps;
            finalMoves = Metrics.moves;
            finalComparisons = Metrics.comparisons;
        }

        Arrays.sort(timesMs);
        double medianTimeMs = timesMs[2];

        writeToCsv(workload, variant, structure, n, medianTimeMs, finalSteps, finalMoves, finalComparisons);
    }

    private static void ensureResultsDirectoryExists() {
        File dir = new File("results");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static void initCsvFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToCsv(String workload, String variant, String structure, int n,
                                   double timeMs, long steps, long moves, long comparisons) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            writer.printf("%s,%s,%s,%d,%.3f,%d,%d,%d%n",
                    workload, variant, structure, n, timeMs, steps, moves, comparisons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}