public class MyLinkedList {
    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        Node newNode = new Node(x);
        Metrics.moves++;
        if (head == null) {
            head = newNode;
            tail = newNode;
            Metrics.moves += 2;
        } else {
            tail.next = newNode;
            tail = newNode;
            Metrics.moves += 2;
        }
        size++;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node newNode = new Node(x);
        Metrics.moves++;
        if (index == 0) {
            newNode.next = head;
            head = newNode;
            Metrics.moves += 2;
        } else {
            Node curr = head;
            Metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
                Metrics.steps++;
            }
            newNode.next = curr.next;
            curr.next = newNode;
            Metrics.moves += 2;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        int removedVal;
        if (index == 0) {
            Metrics.steps++;
            removedVal = head.val;
            head = head.next;
            Metrics.moves++;
            if (head == null) {
                tail = null;
                Metrics.moves++;
            }
        } else {
            Node curr = head;
            Metrics.steps++;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
                Metrics.steps++;
            }
            Metrics.steps++;
            removedVal = curr.next.val;
            if (curr.next == tail) {
                tail = curr;
                Metrics.moves++;
            }
            curr.next = curr.next.next;
            Metrics.moves++;
        }
        size--;
        return removedVal;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Node curr = head;
        Metrics.steps++;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
            Metrics.steps++;
        }
        return curr.val;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            Metrics.steps++;
            Metrics.comparisons++;
            if (curr.val == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }
}