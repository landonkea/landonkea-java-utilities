package landonkea;

/**
 * SimpleTest, Basic tests without JUnit dependency.
 *
 * WHY no JUnit: this repo has no build tool (no Maven/Gradle), so there's
 * no dependency manager to pull JUnit in for a plain `javac`/`java`
 * workflow. build.sh runs this class directly with `java -cp
 * build/classes landonkea.SimpleTest` for exactly that reason. The JUnit
 * tests under src/test still exist for IDEs/CI that do have JUnit on the
 * classpath, but they are not what build.sh runs.
 */
public class SimpleTest {

    private static int passed = 0;
    private static int failed = 0;

    /**
     * Entry point: runs every StringUtils and ArrayUtils test, prints a
     * pass/fail summary, and exits with status 1 if anything failed (so
     * build.sh / CI can detect failure from the process exit code).
     */
    public static void main(String[] args) {
        System.out.println("=== StringUtils tests ===\n");

        testCapitalize();
        testCamelCase();
        testSnakeCase();
        testTruncate();
        testPalindrome();
        testCountOccurrences();

        System.out.println("\n=== ArrayUtils tests ===\n");

        testUnique();
        testChunk();
        testDifference();
        testIntersection();
        testReverse();
        testMax();
        testMin();

        printSummary();

        if (failed > 0) {
            System.exit(1);
        }
    }

    /** Prints the final passed/failed/total tally in a bordered block. */
    private static void printSummary() {
        System.out.println("\n========================================");
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, passed + failed);
        System.out.println("========================================");
    }

    /**
     * Records a pass or fail for one assertion and prints a checkmark/cross
     * line. Shared by assertEquals/assertTrue/assertFalse below so the
     * pass/fail bookkeeping and print formatting live in one place.
     *
     * @param condition Whether the assertion held
     * @param testName Human-readable label for the test
     * @param expectedDescription What was expected, for the failure message
     * @param actualDescription What was actually observed, for the failure message
     */
    private static void report(boolean condition, String testName, String expectedDescription, String actualDescription) {
        if (condition) {
            System.out.printf("  ✓ %s%n", testName);
            passed++;
        } else {
            System.out.printf("  ✗ %s: expected '%s', got '%s'%n", testName, expectedDescription, actualDescription);
            failed++;
        }
    }

    static void assertEquals(Object expected, Object actual, String testName) {
        report(expected.equals(actual), testName, String.valueOf(expected), String.valueOf(actual));
    }

    static void assertTrue(boolean condition, String testName) {
        report(condition, testName, "true", "false");
    }

    static void assertFalse(boolean condition, String testName) {
        report(!condition, testName, "false", "true");
    }

    // ========== StringUtils tests ==========

    static void testCapitalize() {
        assertEquals("Hello", StringUtils.capitalize("hello"), "capitalize: lowercase");
        assertEquals("Hello", StringUtils.capitalize("Hello"), "capitalize: already capitalized");
        assertEquals("", StringUtils.capitalize(""), "capitalize: empty string");
    }

    static void testCamelCase() {
        assertEquals("helloWorld", StringUtils.camelCase("hello world"), "camelCase: space separated");
        assertEquals("fooBarBaz", StringUtils.camelCase("foo-bar-baz"), "camelCase: hyphen separated");
        assertEquals("helloWorld", StringUtils.camelCase("hello_world"), "camelCase: underscore separated");
    }

    static void testSnakeCase() {
        assertEquals("hello_world", StringUtils.snakeCase("helloWorld"), "snakeCase: camelCase");
        assertEquals("foo_bar_baz", StringUtils.snakeCase("fooBarBaz"), "snakeCase: camelCase");
        assertEquals("hello_world", StringUtils.snakeCase("hello-world"), "snakeCase: hyphen");
    }

    static void testTruncate() {
        assertEquals("Hello, ...", StringUtils.truncate("Hello, World!", 10), "truncate: with ellipsis");
        assertEquals("Hi", StringUtils.truncate("Hi", 10), "truncate: no truncation needed");
        assertEquals("Hello", StringUtils.truncate("Hello", 8, "..."), "truncate: exact length");
    }

    static void testPalindrome() {
        assertTrue(StringUtils.isPalindrome("racecar"), "palindrome: racecar");
        assertFalse(StringUtils.isPalindrome("Hello"), "palindrome: Hello");
        assertTrue(StringUtils.isPalindrome("Racecar"), "palindrome: case insensitive");
    }

    static void testCountOccurrences() {
        assertEquals(3, StringUtils.countOccurrences("hello world", "l"), "countOccurrences: 3 l");
        assertEquals(3, StringUtils.countOccurrences("aaa", "a"), "countOccurrences: 3 a");
        assertEquals(0, StringUtils.countOccurrences("hello", "x"), "countOccurrences: 0 x");
    }

    // ========== ArrayUtils tests ==========

    static void testUnique() {
        Integer[] arr = {1, 2, 2, 3, 3, 3};
        assertEquals(3, ArrayUtils.unique(arr).size(), "unique: 3 unique numbers");

        String[] arr2 = {"a", "b", "a"};
        assertEquals(2, ArrayUtils.unique(arr2).size(), "unique: 2 unique strings");
    }

    static void testChunk() {
        Integer[] arr = {1, 2, 3, 4, 5};
        var chunks = ArrayUtils.chunk(arr, 2);
        assertEquals(3, chunks.size(), "chunk: 3 chunks");
        assertEquals(2, chunks.get(0).size(), "chunk: first chunk size 2");
        assertEquals(1, chunks.get(2).size(), "chunk: last chunk size 1");
    }

    static void testDifference() {
        Integer[] arr1 = {1, 2, 3, 4};
        Integer[] arr2 = {2, 4};
        var diff = ArrayUtils.difference(arr1, arr2);
        assertEquals(2, diff.size(), "difference: 2 elements");
        assertTrue(diff.contains(1), "difference: contains 1");
        assertTrue(diff.contains(3), "difference: contains 3");
    }

    static void testIntersection() {
        Integer[] arr1 = {1, 2, 3};
        Integer[] arr2 = {2, 3, 4};
        var inter = ArrayUtils.intersection(arr1, arr2);
        assertEquals(2, inter.size(), "intersection: 2 elements");
        assertTrue(inter.contains(2), "intersection: contains 2");
        assertTrue(inter.contains(3), "intersection: contains 3");
    }

    static void testReverse() {
        Integer[] arr = {1, 2, 3};
        var reversed = ArrayUtils.reverse(arr);
        assertEquals(3, reversed.get(0), "reverse: first is 3");
        assertEquals(2, reversed.get(1), "reverse: second is 2");
        assertEquals(1, reversed.get(2), "reverse: third is 1");
    }

    static void testMax() {
        Integer[] arr = {1, 5, 3, 2, 4};
        assertEquals(5, ArrayUtils.max(arr), "max: 5");
    }

    static void testMin() {
        Integer[] arr = {1, 5, 3, 2, 4};
        assertEquals(1, ArrayUtils.min(arr), "min: 1");
    }
}
