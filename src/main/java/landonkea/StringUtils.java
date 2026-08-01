package landonkea;

/**
 * StringUtils — String manipulation utilities.
 *
 * This class provides common string operations.
 */
public class StringUtils {

    /**
     * Capitalize the first letter of a string.
     *
     * @param str The input string
     * @return The string with the first letter capitalized, or str
     *         unchanged if it is null or empty
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Convert a string to camelCase.
     *
     * HOW: Scans character by character. Spaces, hyphens, and underscores
     * are treated as word separators — each one is dropped and flags the
     * next character to be upper-cased. Every other character is lower-cased
     * by default so mixed-case input (e.g. "FOO-bar") normalizes correctly.
     *
     * @param str The input string
     * @return The camelCase version, or str unchanged if it is null or empty
     */
    public static String camelCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (char c : str.toCharArray()) {
            if (c == ' ' || c == '-' || c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

    /**
     * Convert a string to snake_case.
     *
     * HOW: Inserts an underscore before each uppercase letter (except at
     * position 0, so "Foo" becomes "foo" not "_foo") and lower-cases it.
     * Existing hyphens/spaces are also converted to underscores so mixed
     * separators normalize to one style.
     *
     * @param str The input string
     * @return The snake_case version, or str unchanged if it is null or empty
     */
    public static String snakeCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append('_');
                }
                result.append(Character.toLowerCase(c));
            } else if (c == '-' || c == ' ') {
                result.append('_');
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Truncate a string to a maximum length, appending a suffix if it was
     * shortened.
     *
     * WHY maxLength includes the suffix: this lets callers set a hard cap
     * on the total rendered length (e.g. for a fixed-width UI column)
     * without doing their own arithmetic for the suffix length.
     *
     * @param str The input string
     * @param maxLength Maximum length of the result, including the suffix
     * @param suffix The suffix to add when truncation happens
     * @return str unchanged if it already fits (or is null); otherwise the
     *         truncated string with suffix appended
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - suffix.length()) + suffix;
    }

    /**
     * Truncate a string to a maximum length, appending "..." if it was
     * shortened.
     *
     * @param str The input string
     * @param maxLength Maximum length of the result, including "..."
     * @return The truncated string; see {@link #truncate(String, int, String)}
     */
    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    /**
     * Check if a string is a palindrome.
     *
     * @param str The input string (must not be null)
     * @param caseSensitive Whether to consider case when comparing
     * @return True if the string reads the same forwards and backwards
     */
    public static boolean isPalindrome(String str, boolean caseSensitive) {
        String processed = caseSensitive ? str : str.toLowerCase();
        String reversed = new StringBuilder(processed).reverse().toString();
        return processed.equals(reversed);
    }

    /**
     * Check if a string is a palindrome, ignoring case.
     *
     * @param str The input string (must not be null)
     * @return True if the string reads the same forwards and backwards
     */
    public static boolean isPalindrome(String str) {
        return isPalindrome(str, false);
    }

    /**
     * Count non-overlapping occurrences of a substring in a string.
     *
     * WHY non-overlapping: after each match, the search resumes past the
     * end of that match (index += substring.length()) rather than just past
     * its start. E.g. countOccurrences("aaa", "aa") returns 1, not 2,
     * because the second "aa" would overlap the first.
     *
     * @param str The input string
     * @param substring The substring to count
     * @return The number of non-overlapping occurrences
     */
    public static int countOccurrences(String str, String substring) {
        int count = 0;
        int index = 0;

        while ((index = str.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }

        return count;
    }
}
