import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {

    private MyLinkedList list;

    @BeforeEach
    public void setUp() {
        list = new MyLinkedList();
    }

    @Test
    public void testEmptyAndSingleElement() {
        assertEquals(0, list.size());

        list.add(99);
        assertEquals(1, list.size());
        assertEquals(99, list.get(0));
        assertTrue(list.contains(99));

        int removed = list.remove(0);
        assertEquals(99, removed);
        assertEquals(0, list.size());
        assertFalse(list.contains(99));
    }

    @Test
    public void testFirstAndLastIndexOperations() {
        list.add(10);
        list.add(20);

        list.add(0, 5);
        assertEquals(5, list.get(0));

        list.add(list.size(), 30);
        assertEquals(30, list.get(list.size() - 1));

        assertEquals(5, list.remove(0));
        assertEquals(30, list.remove(list.size() - 1));
        assertEquals(2, list.size());
    }

    @Test
    public void testDuplicateValues() {
        list.add(15);
        list.add(15);
        list.add(15);

        assertEquals(3, list.size());
        assertTrue(list.contains(15));

        list.remove(0);
        assertEquals(2, list.size());
        assertEquals(15, list.get(0));
    }

    @Test
    public void testInvalidIndexExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(5, 10));

        list.add(50);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    public void testCorrectnessAgainstLinkedList() {
        LinkedList<Integer> expected = new LinkedList<>();
        Random rand = new Random(42);

        for (int i = 0; i < 1000; i++) {
            int val = rand.nextInt(5000);
            list.add(val);
            expected.add(val);
        }

        assertEquals(expected.size(), list.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), list.get(i));
        }

        for (int i = 0; i < 200; i++) {
            int removeIdx = rand.nextInt(list.size());
            assertEquals((int) expected.remove(removeIdx), list.remove(removeIdx));
        }

        assertEquals(expected.size(), list.size());
    }
}