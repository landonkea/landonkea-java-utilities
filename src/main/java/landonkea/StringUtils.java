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
     * @return The string with the first letter capitalized
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
     * @param str The input string
     * @return The camelCase version
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
     * @param str The input string
     * @return The snake_case version
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
     * Truncate a string to a maximum length with ellipsis.
     *
     * @param str The input string
     * @param maxLength Maximum length (including ellipsis)
     * @param suffix The suffix to add (default: "...")
     * @return The truncated string
     */
    public static String truncate(String str, int maxLength, String suffix) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - suffix.length()) + suffix;
    }

    /**
     * Truncate a string to a maximum length with "...".
     *
     * @param str The input string
     * @param maxLength Maximum length
     * @return The truncated string
     */
    public static String truncate(String str, int maxLength) {
        return truncate(str, maxLength, "...");
    }

    /**
     * Check if a string is a palindrome.
     *
     * @param str The input string
     * @param caseSensitive Whether to consider case
     * @return True if the string is a palindrome
     */
    public static boolean isPalindrome(String str, boolean caseSensitive) {
        String processed = caseSensitive ? str : str.toLowerCase();
        String reversed = new StringBuilder(processed).reverse().toString();
        return processed.equals(reversed);
    }

    /**
     * Check if a string is a palindrome (case-insensitive).
     *
     * @param str The input string
     * @return True if the string is a palindrome
     */
    public static boolean isPalindrome(String str) {
        return isPalindrome(str, false);
    }

    /**
     * Count occurrences of a substring in a string.
     *
     * @param str The input string
     * @param substring The substring to count
     * @return The number of occurrences
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
