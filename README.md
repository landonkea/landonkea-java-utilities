# landonkea-java-utilities

Small collection of generic String and Array utility functions for Java, written
while learning the language. No external dependencies for the main library.

## What's here

- `StringUtils`: `capitalize`, `camelCase`, `snakeCase`, `truncate`,
  `isPalindrome`, `countOccurrences`.
- `ArrayUtils`: `unique`, `chunk`, `flatten`, `difference`, `intersection`,
  `reverse`, `max`, `min`.

## Build & test

There's no Maven/Gradle here. Just plain `javac`/`java`. Run:

```bash
./build.sh
```

This compiles everything in `src/main/java` into `build/classes` and runs
`SimpleTest`, a dependency-free test runner (see `SimpleTest.java`) that
prints a ✓/✗ per assertion and exits non-zero if anything fails.

`src/test/java/landonkea/UtilsTest.java` is a JUnit 5 version of the same
tests, kept for IDEs/CI that have JUnit on the classpath. It isn't run by
`build.sh` and needs the JUnit Jupiter jars on the classpath to compile/run.
