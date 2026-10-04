public class MinHeap {
    private int[] heap;
    private int size;

    public MinHeap() {
        this.heap = new int[10];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void insert(int x) {
        if (size == heap.length) {
            resize(heap.length * 2);
        }
        Metrics.steps++;
        Metrics.moves++;
        heap[size] = x;
        size++;
        siftUp(size - 1);
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        Metrics.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        Metrics.steps++;
        int minVal = heap[0];
        Metrics.steps++;
        Metrics.moves++;
        heap[0] = heap[size - 1];
        size--;
        if (size > 0) {
            siftDown(0);
        }
        return minVal;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            Metrics.steps += 2;
            Metrics.comparisons++;
            if (heap[index] < heap[parent]) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        while (2 * index + 1 < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = left;

            if (right < size) {
                Metrics.steps += 2;
                Metrics.comparisons++;
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            Metrics.steps += 2;
            Metrics.comparisons++;
            if (heap[index] > heap[smallest]) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Metrics.steps += 2;
        Metrics.moves += 2;
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void resize(int newCapacity) {
        int[] newArray = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            Metrics.steps++;
            Metrics.moves++;
            newArray[i] = heap[i];
        }
        heap = newArray;
    }
}