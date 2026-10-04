import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    private MinHeap heap;

    @BeforeEach
    public void setUp() {
        heap = new MinHeap();
    }

    @Test
    public void testEmptyHeapExceptions() {
        assertEquals(0, heap.size());
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    public void testSingleElementAndDuplicates() {
        heap.insert(42);
        assertEquals(1, heap.size());
        assertEquals(42, heap.peekMin());

        heap.insert(42);
        heap.insert(42);
        assertEquals(3, heap.size());

        assertEquals(42, heap.extractMin());
        assertEquals(42, heap.extractMin());
        assertEquals(42, heap.extractMin());
        assertEquals(0, heap.size());
    }

    @Test
    public void testHeapPropertyAfterEveryOperation() throws Exception {
        Random rand = new Random(42);

        for (int i = 0; i < 100; i++) {
            heap.insert(rand.nextInt(1000));
            assertHeapPropertyHolds(heap);
        }

        while (heap.size() > 0) {
            heap.extractMin();
            assertHeapPropertyHolds(heap);
        }
    }

    @Test
    public void testSortedOutput() {
        Random rand = new Random(42);
        int n = 1000;

        for (int i = 0; i < n; i++) {
            heap.insert(rand.nextInt(10000));
        }

        int prev = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int curr = heap.extractMin();
            assertTrue(curr >= prev, "Output must be in non-decreasing order");
            prev = curr;
        }

        assertEquals(0, heap.size());
    }

    @Test
    public void testCorrectnessAgainstPriorityQueue() {
        PriorityQueue<Integer> expected = new PriorityQueue<>();
        Random rand = new Random(42);

        for (int i = 0; i < 500; i++) {
            int val = rand.nextInt(10000);
            heap.insert(val);
            expected.add(val);
        }

        while (!expected.isEmpty()) {
            assertEquals(expected.peek(), heap.peekMin());
            assertEquals(expected.poll(), heap.extractMin());
        }
    }

    private void assertHeapPropertyHolds(MinHeap heapInstance) throws Exception {
        Field arrayField = MinHeap.class.getDeclaredField("heap");
        Field sizeField = MinHeap.class.getDeclaredField("size");

        arrayField.setAccessible(true);
        sizeField.setAccessible(true);

        int[] internalArray = (int[]) arrayField.get(heapInstance);
        int currentSize = (int) sizeField.get(heapInstance);

        for (int child = 1; child < currentSize; child++) {
            int parent = (child - 1) / 2;
            assertTrue(internalArray[parent] <= internalArray[child],
                    String.format("Heap property violated at index %d (parent: %d, value: %d) <= (child: %d, value: %d)",
                            child, parent, internalArray[parent], child, internalArray[child]));
        }
    }
}