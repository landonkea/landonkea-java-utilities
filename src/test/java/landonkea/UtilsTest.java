package landonkea;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for StringUtils and ArrayUtils.
 */
class UtilsTest {

    private int passed = 0;
    private int failed = 0;

    // ========== StringUtils tests ==========

    @Test
    void testCapitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"), "capitalize: lowercase");
        assertEquals("Hello", StringUtils.capitalize("Hello"), "capitalize: already capitalized");
        assertEquals("", StringUtils.capitalize(""), "capitalize: empty string");
        assertNull(StringUtils.capitalize(null), "capitalize: null");
        passed++;
    }

    @Test
    void testCamelCase() {
        assertEquals("helloWorld", StringUtils.camelCase("hello world"), "camelCase: space separated");
        assertEquals("fooBarBaz", StringUtils.camelCase("foo-bar-baz"), "camelCase: hyphen separated");
        assertEquals("helloWorld", StringUtils.camelCase("hello_world"), "camelCase: underscore separated");
        passed++;
    }

    @Test
    void testSnakeCase() {
        assertEquals("hello_world", StringUtils.snakeCase("helloWorld"), "snakeCase: camelCase");
        assertEquals("foo_bar_baz", StringUtils.snakeCase("fooBarBaz"), "snakeCase: camelCase");
        assertEquals("hello_world", StringUtils.snakeCase("hello-world"), "snakeCase: hyphen");
        passed++;
    }

    @Test
    void testTruncate() {
        assertEquals("Hello, ...", StringUtils.truncate("Hello, World!", 10), "truncate: with ellipsis");
        assertEquals("Hi", StringUtils.truncate("Hi", 10), "truncate: no truncation needed");
        assertEquals("Hello", StringUtils.truncate("Hello", 8, "..."), "truncate: exact length");

        // Regression: maxLength < suffix.length() used to throw
        // StringIndexOutOfBoundsException (negative substring start index).
        assertEquals("He", StringUtils.truncate("Hello, World!", 2, "..."), "truncate: maxLength(2) < suffix.length(3), no suffix fits");
        assertEquals("H", StringUtils.truncate("Hello, World!", 1, "..."), "truncate: maxLength(1) < suffix.length(3)");
        assertEquals("", StringUtils.truncate("Hello, World!", 0, "..."), "truncate: maxLength 0 returns empty string");
        assertEquals("", StringUtils.truncate("Hello, World!", -5, "..."), "truncate: negative maxLength returns empty string");
        assertEquals("Hel", StringUtils.truncate("Hello, World!", 3, "[...]"), "truncate: maxLength(3) < custom suffix.length(5)");
        passed++;
    }

    @Test
    void testPalindrome() {
        assertTrue(StringUtils.isPalindrome("racecar"), "palindrome: racecar");
        assertFalse(StringUtils.isPalindrome("Hello"), "palindrome: Hello");
        assertTrue(StringUtils.isPalindrome("Racecar"), "palindrome: case insensitive");
        passed++;
    }

    @Test
    void testCountOccurrences() {
        assertEquals(3, StringUtils.countOccurrences("hello world", "l"), "countOccurrences: 3 l");
        assertEquals(3, StringUtils.countOccurrences("aaa", "a"), "countOccurrences: 3 a");
        assertEquals(0, StringUtils.countOccurrences("hello", "x"), "countOccurrences: 0 x");

        // Regression: an empty substring used to hang forever (index never advanced).
        assertEquals(0, StringUtils.countOccurrences("hello", ""), "countOccurrences: empty substring returns 0, doesn't hang");
        assertEquals(0, StringUtils.countOccurrences(null, "l"), "countOccurrences: null str returns 0");
        passed++;
    }

    // ========== ArrayUtils tests ==========

    @Test
    void testUnique() {
        Integer[] arr = {1, 2, 2, 3, 3, 3};
        assertEquals(3, ArrayUtils.unique(arr).size(), "unique: 3 unique numbers");

        String[] arr2 = {"a", "b", "a"};
        assertEquals(2, ArrayUtils.unique(arr2).size(), "unique: 2 unique strings");
        passed++;
    }

    @Test
    void testChunk() {
        Integer[] arr = {1, 2, 3, 4, 5};
        var chunks = ArrayUtils.chunk(arr, 2);
        assertEquals(3, chunks.size(), "chunk: 3 chunks");
        assertEquals(2, chunks.get(0).size(), "chunk: first chunk size 2");
        assertEquals(1, chunks.get(2).size(), "chunk: last chunk size 1");

        // Regression: size 0 used to hang forever (the loop counter never advanced).
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.chunk(arr, 0),
                "chunk: size 0 throws IllegalArgumentException instead of hanging");
        passed++;
    }

    @Test
    void testDifference() {
        Integer[] arr1 = {1, 2, 3, 4};
        Integer[] arr2 = {2, 4};
        var diff = ArrayUtils.difference(arr1, arr2);
        assertEquals(2, diff.size(), "difference: 2 elements");
        assertTrue(diff.contains(1), "difference: contains 1");
        assertTrue(diff.contains(3), "difference: contains 3");
        passed++;
    }

    @Test
    void testIntersection() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {2, 3, 4};
        var inter = ArrayUtils.intersection(arr1, arr2);
        assertEquals(2, inter.size(), "intersection: 2 elements");
        assertTrue(inter.contains(2), "intersection: contains 2");
        assertTrue(inter.contains(3), "intersection: contains 3");
        passed++;
    }

    @Test
    void testReverse() {
        Integer[] arr = {1, 2, 3};
        var reversed = ArrayUtils.reverse(arr);
        assertEquals(3, reversed.get(0), "reverse: first is 3");
        assertEquals(2, reversed.get(1), "reverse: second is 2");
        assertEquals(1, reversed.get(2), "reverse: third is 1");
        passed++;
    }

    @Test
    void testMax() {
        Integer[] arr = {1, 5, 3, 2, 4};
        assertEquals(5, ArrayUtils.max(arr), "max: 5");
        passed++;
    }

    @Test
    void testMin() {
        Integer[] arr = {1, 5, 3, 2, 4};
        assertEquals(1, ArrayUtils.min(arr), "min: 1");
        passed++;
    }
}
