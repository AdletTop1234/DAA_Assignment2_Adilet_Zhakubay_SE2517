import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {

    private DynamicArray array;

    @BeforeEach
    public void setUp() {
        array = new DynamicArray();
    }

    @Test
    public void testEmptyAndSingleElement() {
        assertEquals(0, array.size());

        array.add(42);
        assertEquals(1, array.size());
        assertEquals(42, array.get(0));
        assertTrue(array.contains(42));

        int removed = array.remove(0);
        assertEquals(42, removed);
        assertEquals(0, array.size());
        assertFalse(array.contains(42));
    }

    @Test
    public void testFirstAndLastIndexOperations() {
        array.add(10);
        array.add(20);
        array.add(30);

        array.add(0, 5);
        assertEquals(5, array.get(0));
        assertEquals(4, array.size());

        array.add(array.size(), 40);
        assertEquals(40, array.get(array.size() - 1));
        assertEquals(5, array.size());

        int lastValue = array.remove(array.size() - 1);
        assertEquals(40, lastValue);

        int firstValue = array.remove(0);
        assertEquals(5, firstValue);
    }

    @Test
    public void testDuplicateValues() {
        array.add(7);
        array.add(7);
        array.add(7);

        assertEquals(3, array.size());
        assertTrue(array.contains(7));

        array.remove(1);
        assertEquals(2, array.size());
        assertEquals(7, array.get(0));
        assertEquals(7, array.get(1));
    }

    @Test
    public void testInvalidIndexExceptions() {
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(-1, 10));
        assertThrows(IndexOutOfBoundsException.class, () -> array.add(1, 10));

        array.add(100);
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(1));
    }

    @Test
    public void testCorrectnessAgainstArrayList() {
        ArrayList<Integer> expected = new ArrayList<>();
        Random rand = new Random(42);

        for (int i = 0; i < 1000; i++) {
            int val = rand.nextInt(5000);
            array.add(val);
            expected.add(val);
        }

        assertEquals(expected.size(), array.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), array.get(i));
        }

        for (int i = 0; i < 200; i++) {
            int removeIdx = rand.nextInt(array.size());
            int actualRemoved = array.remove(removeIdx);
            int expectedRemoved = expected.remove(removeIdx);
            assertEquals(expectedRemoved, actualRemoved);
        }

        assertEquals(expected.size(), array.size());
    }
}