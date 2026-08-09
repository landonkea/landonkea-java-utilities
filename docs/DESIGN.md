# landonkea-java-utilities — Design & Workflow

## High-Level Overview

```mermaid
graph TB
    subgraph "landonkea-java-utilities"
        A[build.sh] --> B[javac compile]
        A --> C[java SimpleTest]
    end

    subgraph "src/main/java"
        D[StringUtils.java]
        E[ArrayUtils.java]
        F[SimpleTest.java]
    end

    subgraph "src/test/java"
        G[UtilsTest.java - JUnit 5]
    end

    B --> D
    B --> E
    B --> F
    C --> F
```

## Class Structure

```mermaid
graph LR
    subgraph "StringUtils"
        A[capitalize] --> B[camelCase]
        B --> C[snakeCase]
        C --> D[truncate]
        D --> E[isPalindrome]
        E --> F[countOccurrences]
    end

    subgraph "ArrayUtils"
        G[unique] --> H[chunk]
        H --> I[flatten]
        I --> J[difference]
        J --> K[intersection]
        K --> L[reverse]
        L --> M[max/min]
    end
```

## Build Workflow

```mermaid
flowchart TD
    A[User runs build.sh] --> B[javac src/main/java/*.java]
    B --> C[java -cp build/classes SimpleTest]
    C --> D[Print pass/fail per test]
    D --> E{Any failures?}
    E -->|Yes| F[Exit non-zero]
    E -->|No| G[Exit zero]
```

## File Relationships

| File | Purpose | Used By |
|------|---------|---------|
| `build.sh` | Compile + test | User |
| `src/main/java/StringUtils.java` | String helpers | Tests |
| `src/main/java/ArrayUtils.java` | Array helpers | Tests |
| `src/main/java/SimpleTest.java` | Test runner | `build.sh` |
| `src/test/java/UtilsTest.java` | JUnit 5 tests | IDE/CI |

## draw.io

[Open in draw.io](https://app.diagrams.net/#RJava%20utility%20library)
