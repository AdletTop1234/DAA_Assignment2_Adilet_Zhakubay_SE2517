public class DynamicArray {
    private int[] data;
    private int size;

    public DynamicArray() {
        this.data = new int[10];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        Metrics.steps++;
        Metrics.moves++;
        data[size++] = x;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (size == data.length) {
            resize(data.length * 2);
        }
        for (int i = size; i > index; i--) {
            Metrics.steps++;
            Metrics.moves++;
            data[i] = data[i - 1];
        }
        Metrics.steps++;
        Metrics.moves++;
        data[index] = x;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Metrics.steps++;
        int removedValue = data[index];
        for (int i = index; i < size - 1; i++) {
            Metrics.steps++;
            Metrics.moves++;
            data[i] = data[i + 1];
        }
        size--;
        return removedValue;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Metrics.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            Metrics.steps++;
            Metrics.comparisons++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    private void resize(int newCapacity) {
        int[] newArray = new int[newCapacity];
        for (int i = 0; i < size; i++) {
            Metrics.steps++;
            Metrics.moves++;
            newArray[i] = data[i];
        }
        data = newArray;
    }
}