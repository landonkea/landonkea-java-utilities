package landonkea;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ArrayUtils — Array manipulation utilities.
 *
 * This class provides common array operations.
 */
public class ArrayUtils {

    /**
     * Remove duplicate elements from an array, preserving first-seen order.
     *
     * HOW: Backs a {@link LinkedHashSet} with the array's elements. A
     * {@code LinkedHashSet} dedupes like any {@code Set} but (unlike
     * {@code HashSet}) also remembers insertion order, so the result order
     * matches the order elements first appeared in {@code arr}.
     *
     * @param arr The input array
     * @return A new list with duplicates removed, in first-seen order
     */
    public static <T> List<T> unique(T[] arr) {
        return new LinkedHashSet<>(Arrays.asList(arr)).stream()
                .collect(Collectors.toList());
    }

    /**
     * Chunk an array into smaller lists of a given size.
     *
     * WHY: The final chunk may be shorter than {@code size} if
     * {@code arr.length} isn't an exact multiple of {@code size} — this is
     * intentional (no padding), matching common chunk() semantics from other
     * languages.
     *
     * @param arr The input array
     * @param size The size of each chunk
     * @return List of chunks; the last chunk may be smaller than size
     */
    public static <T> List<List<T>> chunk(T[] arr, int size) {
        List<List<T>> chunks = new ArrayList<>();

        for (int i = 0; i < arr.length; i += size) {
            int end = Math.min(i + size, arr.length);
            chunks.add(Arrays.asList(Arrays.copyOfRange(arr, i, end)));
        }

        return chunks;
    }

    /**
     * Flatten an arbitrarily nested list into a single flat list.
     *
     * HOW: Recurses whenever an element is itself a {@code List}; otherwise
     * the element is added directly. Recursion depth equals the nesting
     * depth of the input, so it's fine for normal use but could overflow the
     * stack on pathologically deep nesting.
     *
     * WHY unchecked casts: because Java erases generic types at runtime,
     * there's no way to check at runtime whether a non-list element is
     * actually a {@code T} — the caller is trusted to pass a list that only
     * contains {@code T} and nested lists thereof. This is why the casts are
     * suppressed rather than eliminated.
     *
     * @param arr The nested list
     * @return The flattened list
     */
    public static <T> List<T> flatten(List<Object> arr) {
        List<T> result = new ArrayList<>();

        for (Object item : arr) {
            if (item instanceof List) {
                result.addAll(flattenNestedList(item));
            } else {
                result.add(castToT(item));
            }
        }

        return result;
    }

    /**
     * Recurse into a nested list element found by {@link #flatten}.
     *
     * HOW: Isolates the unchecked cast + recursive call in one place so
     * {@link #flatten} itself stays free of casting noise.
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> flattenNestedList(Object item) {
        return flatten((List<Object>) item);
    }

    /**
     * Cast a raw flatten() element to the caller's element type T.
     *
     * See {@link #flatten} for why this cast can't be checked at runtime.
     */
    @SuppressWarnings("unchecked")
    private static <T> T castToT(Object item) {
        return (T) item;
    }

    /**
     * Get the difference between two arrays.
     *
     * HOW: Loads arr2 into a HashSet for O(1) membership checks, then keeps
     * only arr1 elements not present in that set. Relies on equals()/
     * hashCode() of T, so T should implement them meaningfully.
     *
     * @param arr1 The first array
     * @param arr2 The second array
     * @return Elements in arr1 that are not in arr2
     */
    public static <T> List<T> difference(T[] arr1, T[] arr2) {
        Set<T> set2 = new HashSet<>(Arrays.asList(arr2));
        return Arrays.stream(arr1)
                .filter(item -> !set2.contains(item))
                .collect(Collectors.toList());
    }

    /**
     * Get the intersection of two arrays.
     *
     * HOW: Same HashSet-lookup strategy as {@link #difference}, but keeps
     * arr1 elements that ARE present in arr2 instead of excluding them.
     *
     * @param arr1 The first array
     * @param arr2 The second array
     * @return Elements that appear in both arrays
     */
    public static <T> List<T> intersection(T[] arr1, T[] arr2) {
        Set<T> set2 = new HashSet<>(Arrays.asList(arr2));
        return Arrays.stream(arr1)
                .filter(set2::contains)
                .collect(Collectors.toList());
    }

    /**
     * Reverse an array.
     *
     * WHY a copy: {@link Arrays#asList} returns a fixed-size view backed by
     * the original array, so it's wrapped in a new {@code ArrayList} before
     * reversing to avoid mutating the caller's array in place.
     *
     * @param arr The input array
     * @return A new list containing the elements in reverse order
     */
    public static <T> List<T> reverse(T[] arr) {
        List<T> list = new ArrayList<>(Arrays.asList(arr));
        Collections.reverse(list);
        return list;
    }

    /**
     * Find the maximum value in an array.
     *
     * WHY exception on empty: there's no sentinel value that works for an
     * arbitrary Comparable type, so an empty array is treated as a caller
     * error rather than silently returning null.
     *
     * @param arr The input array
     * @return The maximum value
     * @throws IllegalArgumentException if arr is empty
     */
    public static <T extends Comparable<T>> T max(T[] arr) {
        requireNonEmpty(arr);

        T max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareTo(max) > 0) {
                max = arr[i];
            }
        }
        return max;
    }

    /**
     * Find the minimum value in an array.
     *
     * See {@link #max} for why an empty array throws rather than returning
     * null.
     *
     * @param arr The input array
     * @return The minimum value
     * @throws IllegalArgumentException if arr is empty
     */
    public static <T extends Comparable<T>> T min(T[] arr) {
        requireNonEmpty(arr);

        T min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareTo(min) < 0) {
                min = arr[i];
            }
        }
        return min;
    }

    /**
     * Guard shared by {@link #max} and {@link #min} to reject empty arrays
     * with a consistent message.
     */
    private static <T> void requireNonEmpty(T[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }
    }
}
