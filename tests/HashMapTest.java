package tests;

import org.junit.jupiter.api.Test;
import tp2.HashMap;
import edu.princeton.cs.algs4.LinearRegression;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HashMapTest {

    @Test
    void putReturnsNullIfNoOldValue() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        Integer value = map.put("myKey", 1);

        assertNull(value);
    }

    @Test
    void get() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        boolean nullWithoutValue = map.get("myKey") == null;

        Integer value = 1;
        map.put("myKey", value);
        Integer valueInMap = map.get("myKey");

        assertEquals(value, valueInMap);
        assertTrue(nullWithoutValue);
    }

    @Test
    void putReturnsOldValue() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        Integer oldValue = 1;
        map.put("myKey", oldValue);

        Integer returnedOldValue = map.put("myKey", 2);

        assertEquals(oldValue, returnedOldValue);
    }

    @Test
    void putReplacesValue() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        Integer value = 2;
        map.put("myKey", 1);
        map.put("myKey", value);
        Integer valueInMap = map.get("myKey");

        assertEquals(value, valueInMap);
        assertEquals(map.size(), 1);
    }

    @Test
    void removeReturnsRemovedValue() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        Integer value = 1;
        map.put("myKey", value);
        Integer removedValue = map.remove("myKey");

        assertEquals(value, removedValue);
    }

    @Test
    void removeReturnsNullIfNotExist() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        Integer removedValue = map.remove("myKey");

        assertNull(removedValue);
    }

    @Test
    void sizeIncrementsAndDecrements() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        boolean isProperSizeInitially = map.isEmpty();

        map.put("myKey", 1);

        boolean isProperSizeAfterAdding = map.size() == 1;

        map.remove("myKey");

        boolean isProperSizeAfterRemoving = map.isEmpty();

        assertTrue(isProperSizeInitially);
        assertTrue(isProperSizeAfterAdding);
        assertTrue(isProperSizeAfterRemoving);
    }

    @Test
    void collisionsAreHandled() {
        HashMap<HashMapTest.KeyMock, Integer> map = new HashMap<HashMapTest.KeyMock, Integer>(5);
        int n = 9;

        for (int i = 0; i < n; ++i) {
            map.put(new HashMapTest.KeyMock(i), i);
        }

        boolean canGetAllValues = true;
        for (int i = 0; i < n && canGetAllValues; ++i) {
            Integer value = map.get(new HashMapTest.KeyMock(i));
            canGetAllValues = value != null && value.equals(i);
        }

        boolean canDetectAllKeys = true;
        for (int i = 0; i < n && canGetAllValues && canDetectAllKeys; ++i) {
            canDetectAllKeys = map.containsKey(new HashMapTest.KeyMock(i));
        }

        // Did not want tests to have any randomness, so the array is manually created
        // Does not dynamically change with n
        int[] staticRandomOrderNums = {0, 4, 2, 6, 3, 1, 7, 8, 5};
        boolean canRemoveAllValues = true;
        for (int i = 0; i < n && canGetAllValues && canDetectAllKeys && canRemoveAllValues; ++i) {
            Integer removedValue = map.remove(new HashMapTest.KeyMock(staticRandomOrderNums[i]));
            canRemoveAllValues = removedValue != null && removedValue.equals(staticRandomOrderNums[i]);
        }

        assertTrue(canGetAllValues);
        assertTrue(canDetectAllKeys);
        assertTrue(canRemoveAllValues);
    }

    @Test
    void clear() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        int n = 9;

        for (int i = 0; i < n; ++i) {
            String index = String.valueOf(i);
            map.put("myKey" + index, i);
        }

        boolean canGetAllValues = true;
        for (int i = 0; i < n && canGetAllValues; ++i) {
            String index = String.valueOf(i);
            Integer value = map.get("myKey" + index);
            canGetAllValues = value != null && value.equals(i);
        }

        map.clear();

        boolean valuesCleared = canGetAllValues && map.isEmpty();
        for (int i = 0; i < n && valuesCleared; ++i) {
            String index = String.valueOf(i);
            valuesCleared = map.get("myKey" + index) == null;
        }

        assertTrue(valuesCleared);
    }

    @Test
    void containsKey() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        map.put("myKey", 1);

        assertTrue(map.containsKey("myKey"));
        assertFalse(map.containsKey("unknownKey"));
    }

    @Test
    void isEmpty() {
        double total = 0.0;

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        boolean isEmptyOnEmptyMap = map.isEmpty();

        map.put("myKey", 1);

        boolean isEmptyOnFilledMap = map.isEmpty();

        map.remove("myKey");

        boolean isEmptyOnEmptiedMap = map.isEmpty();

        assertTrue(isEmptyOnEmptyMap);
        assertFalse(isEmptyOnFilledMap);
        assertTrue(isEmptyOnEmptiedMap);
    }

    @Test
    void capacityIncreasesWithLoadFactor() {
        HashMap<String, Integer> map = new HashMap<String, Integer>(10);

        int n = 6;
        for (int i = 0; i < n; ++i) {
            String index = String.valueOf(i);
            map.put("myKey" + index, i);
        }

        // Clear should not put capacity back to its initial value
        map.clear();

        assertEquals(map.capacity(), 23);
    }

    @Test
    void rehashWorksProperly() {
        HashMap<String, Integer> map = new HashMap<String, Integer>(10);
        int n = 15;

        for (int i = 0; i < n; ++i) {
            String index = String.valueOf(i);
            map.put("myKey" + index, i);
        }

        boolean isValidRehash = map.size() == n;
        for (int i = 0; i < n && isValidRehash; ++i) {
            String index = String.valueOf(i);
            Integer value = map.get("myKey" + index);
            isValidRehash = value != null && map.get("myKey" + index) == i;
        }

        assertTrue(isValidRehash);
    }

    // We verify that the insert/put functions are O(1), we test by adding N items and making sure
    // that the number of the most common operation (barometer) follows a trend that is linear.
    @Test
    void testComplexityWithBarometer() {
        HashMap<KeyMock, Integer> map = new HashMap<>(100000000);
        int increaseRate = 10;
        int previousTotalBarometer = 1;
        double totalBarometerRate = 0.0;
        int maxSize = 1000000;
        int totalLoops = 0;
        for (int listSize = increaseRate; listSize < maxSize; listSize *= increaseRate) {
            ++totalLoops;
            // Insert.
            for (int i = 0; i < listSize; ++i) {
                map.put(new KeyMock(i, false), i);
            }
            // Get.
            for (int i = 0; i < listSize; ++i) {
                assertTrue(map.containsKey(new KeyMock(i, false)));
            }
            // Count the barometer operation for complexity.
            int totalBarometer = 0;
            for (KeyMock key : map) {
                totalBarometer += key.getBarometerCounter();
            }
            totalBarometerRate += (double) totalBarometer / previousTotalBarometer;
            previousTotalBarometer = totalBarometer;
        }
        // The rate should be around the increaseRate because the complexity is O(n).
        assertEquals(totalBarometerRate / totalLoops, increaseRate, 1.0);
    }

    @Test
    void testComplexityWithTime() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            HashMap<KeyMock, Integer> map = new HashMap<>(100000000);
            int increaseRate = 400000;
            int maxSize = 8000000;
            ArrayList<Double> Xs = new ArrayList<>();
            ArrayList<Double> Ys = new ArrayList<>();
            for (int listSize = increaseRate; listSize < maxSize; listSize += increaseRate) {
                long startTime = System.nanoTime();
                // Insert.
                for (int i = 0; i < listSize; ++i) {
                    map.put(new KeyMock(i, false), i);
                }
                // Get.
                for (int i = 0; i < listSize; ++i) {
                    assertTrue(map.containsKey(new KeyMock(i, false)));
                }
                long endTime = System.nanoTime();
                long totalBarometer = endTime - startTime;
                Xs.add((double) listSize);
                Ys.add((double) totalBarometer);
            }
            LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
            // The trend should be linear between input size and time => R2 ~= 1 => O(n).
            System.out.println(regression.R2());
            regression.plot("Interview");
            assertEquals(regression.R2(), 1.0, 0.1);
        }, "Votre algorithme n'est probablement pas en O(n)");
    }

    static class KeyMock {
        private int barometerCounter;
        private final Integer key;
        private final boolean forceHashCode;

        public KeyMock(Integer key) {
            this(key, true);
        }

        public KeyMock(Integer key, boolean forceHashCode) {
            this.key = key;
            this.forceHashCode = forceHashCode;
        }

        public int getBarometerCounter() {
            return barometerCounter;
        }

        @Override
        public int hashCode() {
            return forceHashCode ? 1 : key.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            ++barometerCounter;

            if (other instanceof HashMapTest.KeyMock) {
                HashMapTest.KeyMock otherKey = (HashMapTest.KeyMock) other;
                return key.equals(otherKey.key);
            }

            return false;
        }
    }
}
