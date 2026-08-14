# Feature ideas

Concrete additions that fit what this library already does: small,
dependency-free `String`/`Array` helpers, plus a couple of things about the
project itself now that it has release channels. Not a roadmap, just a
list to pull from.

## StringUtils

1. **`padStart(str, length, padChar)` / `padEnd(str, length, padChar)`**
   Left/right-pad a string to a fixed width. Natural neighbor to
   `truncate`, same "fit a string to a target length" idea in reverse.

2. **`isBlank(str)` / `isNotBlank(str)`**
   Null-or-whitespace-only check. `capitalize` already handles null and
   empty explicitly; a string of just spaces slips through both `isEmpty`
   checks and every other method right now.

3. **`titleCase(str)`**
   Capitalize every word, not just the first character. `capitalize`
   already does the single-word version; this is the sentence-level one.

4. **`slugify(str)`**
   Lowercase, strip anything that isn't alphanumeric, collapse whitespace
   into hyphens. `camelCase` and `snakeCase` already cover two case
   conventions; slugs (`my-blog-post`) are the third one people reach for.

5. **`reverse(str)`**
   `isPalindrome` already builds a reversed copy internally
   (`new StringBuilder(processed).reverse()`) and throws it away. Worth
   exposing as its own method since reversing a string is a common enough
   ask on its own.

6. **`levenshteinDistance(a, b)`**
   Edit distance between two strings. Pairs naturally with
   `isPalindrome`/`countOccurrences` as a third "compare two strings"
   utility, and it's the kind of thing worth having tested once rather
   than reimplemented per project.

7. **`mask(str, visibleChars, maskChar)`**
   Replace all but the last N characters with a mask character, e.g.
   `mask("4111111111111111", 4, '*')` for the kind of partial-redaction
   display you see for card numbers or account IDs.

8. **`stripAccents(str)`**
   Normalize `"café"` to `"cafe"`. Useful on its own and as a preprocessing
   step before `slugify` or a case-insensitive search.

## ArrayUtils

9. **`frequency(arr)`**
   Return a `Map<T, Integer>` counting how many times each element
   appears. `countOccurrences` already does this for substrings in
   `StringUtils`; this is the array-of-elements version.

10. **`groupBy(arr, keyFn)`**
    Return `Map<K, List<T>>` by applying a key function to each element.
    One of the more commonly reached-for array operations that isn't
    covered by `unique`/`chunk`/`difference` yet.

11. **`partition(arr, predicate)`**
    Split into two lists, matches and non-matches, in one pass. Sits next
    to `difference`/`intersection`, which already do the "split by
    membership in another array" version of this.

12. **`rotate(arr, n)`**
    Rotate elements left or right by `n` positions. Direct sibling of
    `reverse`, same "rearrange without dropping anything" shape.

13. **`window(arr, size)`**
    Sliding windows of a given size (overlapping), as opposed to `chunk`,
    which produces disjoint groups. Common in anything that looks at
    consecutive elements, e.g. computing a moving average.

14. **`zip(arr1, arr2)`**
    Pair up elements from two arrays by index into a `List<Map.Entry<T, U>>`.
    `difference`/`intersection` already work across two arrays; `zip` is
    the "combine" counterpart to those "compare" operations.

15. **`shuffle(arr)`**
    Fisher-Yates shuffle returning a new list, leaving the input array
    untouched, consistent with how `reverse` already avoids mutating the
    caller's array.

16. **`sum(arr)` / `average(arr)`**
    For arrays of `Number` subtypes. `max`/`min` already exist for any
    `Comparable`; `sum`/`average` are the numeric-specific pair most
    projects end up writing by hand otherwise.

## Project-level

17. **Publish the jar somewhere consumable**
    The release workflows now build a versioned jar and attach it to each
    GitHub Release. Actually publishing it, GitHub Packages is the
    lowest-friction option, Maven Central the more standard one, would let
    another project depend on a version number instead of vendoring the
    source.

18. **Generate and publish Javadoc**
    Every public method already has real Javadoc (see `StringUtils.java`,
    `ArrayUtils.java`). Running `javadoc` and publishing the output to
    GitHub Pages alongside `docs/DESIGN.md` would make that documentation
    browsable without cloning the repo.

19. **`module-info.java`**
    A Java Platform Module System descriptor so consumers on Java 9+ can
    `requires landonkea.utilities` instead of relying on the classpath.
    Low effort given the library is already two classes with no external
    dependencies, and `build.sh`/CI already pin JDK 21.

20. **Null-safety annotations**
    Several methods already document null handling in prose (`capitalize`,
    `truncate`, `snakeCase` all say what happens on `null` input). Adding
    `@Nullable`/`@NonNull` annotations (JSpecify is the current standard)
    would make that contract something a caller's IDE or static analysis
    tool can actually check, instead of relying on someone reading the
    Javadoc.
