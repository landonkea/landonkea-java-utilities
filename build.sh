#!/bin/bash
# build.sh — Compile and run Java tests

echo "=== Compiling Java files ==="
mkdir -p build/classes

# Compile main sources
javac -d build/classes src/main/java/landonkea/*.java

echo "=== Running tests ==="
echo ""

# Simple test runner (no JUnit needed for basic tests)
java -cp build/classes landonkea.SimpleTest

echo ""
echo "=== Done ==="
