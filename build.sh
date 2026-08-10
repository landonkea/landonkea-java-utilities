#!/bin/bash
# build.sh, Compile and run Java tests
set -e

echo "=== Compiling Java files ==="
mkdir -p build/classes

# Compile main sources
javac -d build/classes src/main/java/landonkea/*.java

echo "=== Running tests ==="
echo ""

# Simple test runner (no JUnit needed for basic tests).
# Run outside of `set -e` so we can capture the exit code, report it, and
# propagate it as build.sh's own exit code, without this, a failing
# SimpleTest run would be masked by the later `echo "=== Done ==="`
# succeeding, and build.sh would always exit 0.
set +e
java -cp build/classes landonkea.SimpleTest
test_status=$?
set -e

echo ""
echo "=== Done ==="

exit $test_status
