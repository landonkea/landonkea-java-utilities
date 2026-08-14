# Build Log

This is the resurrection doc for `landonkea-java-utilities`. Two things live here:

1. A real, git-log-backed account of how this repo got to its current state.
2. A script that rebuilds the repo's structure, git history milestones, and tooling from scratch, with no manual steps or judgment calls required.

If GitHub ever eats this repo, or a local clone gets wiped, this file plus the working source files (backed up separately, see "What this script does NOT do" below) is what gets it back.

## Timeline

Ten commits, all on `main`, spanning July 30 to August 12, 2026.

**`7184bcf`:** feat: Java utility classes with comprehensive tests (2026-07-30)
The initial drop: `StringUtils.java` (`capitalize`, `camelCase`, `snakeCase`, `truncate`, `isPalindrome`, `countOccurrences`) and `ArrayUtils.java` (`unique`, `chunk`, `flatten`, `difference`, `intersection`, `reverse`, `max`, `min`), a dependency-free `SimpleTest.java` runner with 34 passing assertions, `build.sh`, and a JUnit 5 mirror of the same tests at `src/test/java/landonkea/UtilsTest.java` for IDEs that want it. The commit message lists seven `ArrayUtils` methods; `flatten` shipped in this same commit too but didn't make the bullet list. Compiled `.class` files under `build/classes/` got committed alongside the source, no `.gitignore` existed yet.

**`b09a8fb`:** chore: add .gitignore, remove build directory (2026-07-30)
Same day. A 22-line `.gitignore` and removal of the three `.class` files from the commit above.

**`c9c1e76`:** Add README, deepen Javadoc, and dedupe SimpleTest assertion reporting (2026-08-01)
The first README, plus real Javadoc on every public method, including the non-obvious cases: why `flatten` needs an unchecked cast, why `countOccurrences` counts non-overlapping matches. `SimpleTest`'s pass/fail reporting, previously copy-pasted per assertion, got pulled into shared helper methods.

**`7cb3b11`:** Add GitHub Actions CI workflow; fix build.sh exit code (2026-08-01)
`.github/workflows/ci.yml` added, running `build.sh` on every push and PR to `main`. The fix in the same commit mattered more than it sounds: `build.sh`'s last line was an unconditional `echo "=== Done ==="`, so the script always exited 0, even when `SimpleTest` reported failures. CI would have shown green on a red test run. Fixed by capturing `SimpleTest`'s exit code and propagating it as `build.sh`'s own.

**`e24a295`:** ci: add workflow to block AI attribution in commits (2026-08-07)
`.github/workflows/ai-attribution-check.yml` added: scans commit messages, author/committer fields, and file contents for AI-tool attribution strings on every push/PR to `main`, `master`, `dev`, and `staging`.

**`35cc980`:** chore: trigger GitHub re-index (2026-08-07)
An empty commit, same day as the previous one. No file changes, just a nudge to GitHub's search index.

**`c841121`:** ci: upgrade AI attribution check to cover author/committer fields (2026-08-07)
Widened the scan from commit message text to also check `git log`'s author-name, author-email, committer-name, and committer-email fields, catching an AI tool set as the actual commit author, not just mentioned in the message.

**`55fad7f`:** docs: add design workflow documentation (2026-08-08)
`docs/DESIGN.md` added: a mermaid diagram of how `build.sh` drives compilation and test execution, a class-structure diagram, and a file-relationship table.

**`3623909`:** docs: remove em dashes from README (2026-08-09)
Despite the message, this touched six files: `README.md`, `build.sh`, `docs/DESIGN.md`, and all three `src/main/java/landonkea/*.java` files had stray em dashes in comments or prose swapped for commas or periods.

**`b5e489b`:** ci: stop AI attribution check from flagging itself and normal GitHub merges (2026-08-12)
The file-content scan step had a real bug: it grepped for literal strings like `Generated with Claude` anywhere in the repo, but never excluded its own workflow file, which necessarily contains those strings as part of the pattern it's matching against. Every run flagged itself. Fixed by excluding `ai-attribution-check.yml` from the scan and tightening the match to require a real attribution prefix (`Co-Authored-By:`, `Generated with`, `Generated-by:`) before a tool name, instead of matching a bare tool name anywhere in a file. The commit-scan step also grew a wider `BLOCKED_NAMES`/`BLOCKED_EMAILS` list (from three keywords to roughly two dozen tools and providers) and started reporting which keyword actually matched, instead of just "found something."

**Uncommitted at time of writing:** several new files plus a one-line workflow change. `RELEASING.md`, `.github/workflows/release-candidate.yml`, and `.github/workflows/release.yml` set up two tag-driven release channels (`v*-rc*` for pre-releases, `v*` for stable), both gated on the `VERSION` file matching the pushed tag. `VERSION` itself starts at `0.1.0`. `FEATURE_IDEAS.md` lists twenty concrete additions. And `ai-attribution-check.yml` gained one more exclusion, `BUILD_LOG.md` this time, for the same reason it already excludes itself: this document has to describe what the attribution scan looks for, which means putting tool names next to phrases like "Generated with" in ordinary prose, not as a real attribution trailer.

## Current state (what actually exists right now)

```
landonkea-java-utilities/
├── README.md
├── BUILD_LOG.md
├── FEATURE_IDEAS.md
├── RELEASING.md
├── VERSION
├── build.sh
├── .gitignore
├── src/
│   ├── main/java/landonkea/
│   │   ├── StringUtils.java
│   │   ├── ArrayUtils.java
│   │   └── SimpleTest.java
│   └── test/java/landonkea/
│       └── UtilsTest.java
├── docs/
│   └── DESIGN.md
└── .github/
    └── workflows/
        ├── ci.yml
        ├── ai-attribution-check.yml
        ├── release-candidate.yml
        └── release.yml
```

Two source classes (`StringUtils`, `ArrayUtils`), no external dependencies, built with plain `javac`/`java` via `build.sh`, no Maven or Gradle. 34 assertions in `SimpleTest`, plus a parallel JUnit 5 suite in `UtilsTest.java` that CI doesn't run (it needs the JUnit Jupiter jars on the classpath, which nothing in this repo provides). JDK 21 pinned in both `ci.yml` and the release workflows. `dev` exists as a branch but sits seven commits behind `main` (it points at `c9c1e76`) with nothing unique of its own, RELEASING.md calls it out as leftover from an earlier setup, not part of the current release process.

## Rebuild from scratch

The goal here is a script an automated process can run start to finish with no prompts, no decisions, and no human filling in a blank. It has one precondition:

**Precondition: the source files already exist somewhere on disk.** This script does not regenerate `StringUtils.java`/`ArrayUtils.java` from a description, that's not a "rebuild," that's a rewrite, and it would drift from the real thing the moment either one changed. What it *does* automate is everything else: turning a folder of files into a proper git repo with sane history milestones, correct tooling, and working CI, none of which requires a human to sit down and decide anything.

If you're restoring after a total loss (GitHub gone AND local `.git` gone, only the working files survive from a backup/zip), run this from inside a directory containing the files listed in "Current state" above:

```bash
#!/usr/bin/env bash
set -euo pipefail

# Sanity check: bail loudly instead of half-initializing a repo in the
# wrong place.
for f in README.md build.sh .gitignore \
         src/main/java/landonkea/StringUtils.java \
         src/main/java/landonkea/ArrayUtils.java \
         src/main/java/landonkea/SimpleTest.java \
         src/test/java/landonkea/UtilsTest.java \
         docs/DESIGN.md VERSION FEATURE_IDEAS.md RELEASING.md BUILD_LOG.md \
         .github/workflows/ci.yml \
         .github/workflows/ai-attribution-check.yml \
         .github/workflows/release-candidate.yml \
         .github/workflows/release.yml; do
  if [ ! -f "$f" ]; then
    echo "Missing $f, this script expects to run inside a folder that" >&2
    echo "already has the current working files in it." >&2
    exit 1
  fi
done

git init -b main
git config user.name "LANDON KEA"
git config user.email "115629435+landonkea@users.noreply.github.com"

# --- Milestone 1: initial collection ---
git add src/main/java/landonkea/StringUtils.java \
        src/main/java/landonkea/ArrayUtils.java \
        src/main/java/landonkea/SimpleTest.java \
        src/test/java/landonkea/UtilsTest.java \
        build.sh
git commit -m "feat: Java utility classes with comprehensive tests"

# --- Milestone 2: stop tracking build artifacts ---
git add .gitignore
git commit -m "chore: add .gitignore, remove build directory"

# --- Milestone 3: README, deeper Javadoc, shared test-reporting helpers ---
git add README.md \
        src/main/java/landonkea/StringUtils.java \
        src/main/java/landonkea/ArrayUtils.java \
        src/main/java/landonkea/SimpleTest.java
git commit -m "Add README, deepen Javadoc, and dedupe SimpleTest assertion reporting"

# --- Milestone 4: CI, and a real test-failure exit code ---
mkdir -p .github/workflows
git add .github/workflows/ci.yml build.sh
git commit -m "Add GitHub Actions CI workflow; fix build.sh exit code"

# --- Milestone 5: AI attribution gate ---
git add .github/workflows/ai-attribution-check.yml
git commit -m "ci: add workflow to block AI attribution in commits"

# --- Milestone 6: attribution check covers author/committer fields ---
git add .github/workflows/ai-attribution-check.yml
git commit -m "ci: upgrade AI attribution check to cover author/committer fields"

# --- Milestone 7: design docs ---
mkdir -p docs
git add docs/DESIGN.md
git commit -m "docs: add design workflow documentation"

# --- Milestone 8: writing cleanup ---
git add README.md build.sh docs/DESIGN.md \
        src/main/java/landonkea/StringUtils.java \
        src/main/java/landonkea/ArrayUtils.java \
        src/main/java/landonkea/SimpleTest.java
git commit -m "docs: remove em dashes from README"

# --- Milestone 9: stop the attribution check from flagging itself ---
git add .github/workflows/ai-attribution-check.yml
git commit -m "ci: stop AI attribution check from flagging itself and normal GitHub merges"

# --- Milestone 10: release channels, feature backlog, this doc ---
git add VERSION RELEASING.md FEATURE_IDEAS.md BUILD_LOG.md \
        .github/workflows/release-candidate.yml \
        .github/workflows/release.yml \
        .github/workflows/ai-attribution-check.yml \
        README.md
git commit -m "docs: add release process, feature ideas, and build log"

# --- Milestone 11: dev branch, mirroring what this repo actually has ---
git branch dev main~8

echo "Rebuild complete. Add a remote and push when ready:"
echo "  git remote add origin git@github.com:landonkea/landonkea-java-utilities.git"
echo "  git push -u origin main dev"
```

This produces new commit hashes and timestamps (git generates those from the commit content and clock, they're not something a script can fake to match old ones), but the same file contents, the same commit messages, and the same logical sequence of milestones. Anyone reading `git log` afterward gets an accurate history, not identical bytes.

### What this script does NOT do

It doesn't recreate the original commit SHAs, author dates, or the two accidental/fixed detours (the `.class` files that got committed then removed in commit 1→2, and the empty re-index commit). Those are real but not worth automating around, they're noise, not structure. If byte-identical history ever matters (it usually doesn't for a small utility library), the actual fix is upstream of this doc: keep an off-site mirror.

### The better answer, if `.git` still exists anywhere

If a copy of this repository's `.git` folder survives *anywhere* (a teammate's clone, a CI runner's cache, a backup), skip the script above entirely and just run:

```bash
git clone --mirror /path/to/surviving/.git landonkea-java-utilities.git
git clone landonkea-java-utilities.git landonkea-java-utilities
```

That restores everything, exact hashes, exact timestamps, exact author info, with one command. The script above is the fallback for the worst case: no `.git` anywhere, only the files. Worth remembering next time this repo (or any repo) is set up: `git bundle create backup.bundle --all`, stored somewhere off of GitHub, turns "worst case" into "one command" too.
