package tests;

import edu.princeton.cs.algs4.LinearRegression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tp2.Interview;
import tp2.Main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InterviewTest {
    static String longFlippedNumber;

    @BeforeAll
    static void setup() throws IOException, URISyntaxException {
        URL path = Main.class.getResource("longFlippedNumber.txt");
        longFlippedNumber = new String(Files.readAllBytes(Paths.get(path.toURI())), StandardCharsets.UTF_8);
    }

    @Test
    void isValidFlippedSimple() {
        assertTrue(Interview.isValidFlipped(""));
        for (Integer i : List.of(0, 1, 2, 5, 8)) {
            assertTrue(Interview.isValidFlipped(i.toString()));
        }
        for (Integer i : List.of(3, 4, 6, 7, 9)) {
            assertFalse(Interview.isValidFlipped(i.toString()));
        }
    }

    @Test
    void isValidFlippedHard() {
        assertTrue(Interview.isValidFlipped("12521"));
        assertTrue(Interview.isValidFlipped("126921"));
        assertTrue(Interview.isValidFlipped("912216"));
        assertFalse(Interview.isValidFlipped("123"));
    }

    @Test
    void isValidFlippedWithPermutationSimple() {
        assertTrue(Interview.isValidFlippedWithPermutation(""));
        for (Integer i : List.of(0, 1, 2, 5, 8)) {
            assertTrue(Interview.isValidFlippedWithPermutation(i.toString()));
        }
        for (Integer i : List.of(3, 4, 6, 7, 9)) {
            assertFalse(Interview.isValidFlippedWithPermutation(i.toString()));
        }
    }

    @Test
    void isValidFlippedWithPermutationHard() {
        assertTrue(Interview.isValidFlippedWithPermutation("111155888"));
        assertTrue(Interview.isValidFlippedWithPermutation("1111558888"));
        assertTrue(Interview.isValidFlippedWithPermutation("111558888"));
        assertTrue(Interview.isValidFlippedWithPermutation("6699111558888"));
        assertFalse(Interview.isValidFlippedWithPermutation("66991115588889"));
    }

    @Test
    void testComplexityWithTime() {
        assertTimeoutPreemptively(Duration.ofSeconds(30), () -> {
            int increaseRate = 100;
            int maxSize = 2000;
            ArrayList<Double> Xs = new ArrayList<>();
            ArrayList<Double> Ys = new ArrayList<>();
            assertTrue(Interview.isValidFlippedWithPermutation(""));
            for (int listSize = increaseRate; listSize < maxSize; listSize += increaseRate) {
                long startTime = System.nanoTime();
                assertTrue(Interview.isValidFlippedWithPermutation(
                        String.join("", Collections.nCopies(listSize, longFlippedNumber))));
                long endTime = System.nanoTime();
                long totalBarometer = endTime - startTime;
                Xs.add((double) listSize);
                Ys.add((double) totalBarometer);
            }
            LinearRegression regression = new LinearRegression(Xs.toArray(new Double[0]), Ys.toArray(new Double[0]));
            System.out.println(regression.R2());
            // The trend should be linear between input size and time => R2 ~= 1 => O(n).
            assertEquals(regression.R2(), 1.0, 0.05);
        }, "Votre algorithme n'est probablement pas en O(n)");
    }
}
