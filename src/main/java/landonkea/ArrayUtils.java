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
     * Remove duplicate elements from an array.
     *
     * @param arr The input array
     * @return A new array with duplicates removed
     */
    public static <T> List<T> unique(T[] arr) {
        return new LinkedHashSet<>(Arrays.asList(arr)).stream()
                .collect(Collectors.toList());
    }

    /**
     * Chunk an array into smaller arrays of a given size.
     *
     * @param arr The input array
     * @param size The size of each chunk
     * @return List of chunks
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
     * Flatten a nested list.
     *
     * @param arr The nested list
     * @return The flattened list
     */
    public static <T> List<T> flatten(List<Object> arr) {
        List<T> result = new ArrayList<>();

        for (Object item : arr) {
            if (item instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> nested = (List<Object>) item;
                result.addAll(flatten(nested));
            } else {
                @SuppressWarnings("unchecked")
                T typedItem = (T) item;
                result.add(typedItem);
            }
        }

        return result;
    }

    /**
     * Get the difference between two arrays.
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
     * @param arr The input array
     * @return The reversed array
     */
    public static <T> List<T> reverse(T[] arr) {
        List<T> list = new ArrayList<>(Arrays.asList(arr));
        Collections.reverse(list);
        return list;
    }

    /**
     * Find the maximum value in an array.
     *
     * @param arr The input array
     * @return The maximum value
     */
    public static <T extends Comparable<T>> T max(T[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }

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
     * @param arr The input array
     * @return The minimum value
     */
    public static <T extends Comparable<T>> T min(T[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("Array must not be empty");
        }

        T min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].compareTo(min) < 0) {
                min = arr[i];
            }
        }
        return min;
    }
}
