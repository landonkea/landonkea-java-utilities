# Releasing

This is a small utility library, not a service you deploy somewhere. It
doesn't have a "prod" server to push to, so the usual dev/staging/prod
ladder doesn't map cleanly here. What it does have is versions people might
depend on, and the risk that a broken one gets published. This doc covers
how versions get cut and how a bad one gets caught before it's called
stable.

## Branching

`main` is the only long-lived branch. It should always build and pass
`build.sh`. Everything else, a new `StringUtils` method, a bug fix, a docs
tweak, happens on a short-lived branch and merges back into `main` through
a pull request once CI is green.

There's no permanent `dev` or `staging` branch. A branch that just sits
there accumulating unreleased work is exactly what tends to drift out of
sync and rot (this repo already has one `dev` branch left over from an
earlier setup that's several commits behind `main` — it isn't part of this
workflow and can be deleted). The two channels described below are defined
by git tags instead, not by which branch a commit happens to live on.

## Versioning

The `VERSION` file at the repo root holds one line: the version currently
being worked on, in [SemVer](https://semver.org/) form (`MAJOR.MINOR.PATCH`,
e.g. `0.1.0`). Bump it on `main` as part of whatever PR starts work toward
the next version.

Pre-release builds append an `-rc.N` suffix to that same version, the way
Maven and Gradle projects commonly mark builds that aren't final yet
(`-SNAPSHOT`, `-rc1`, and similar). This repo doesn't use Maven or Gradle
as build tools, `build.sh` plus plain `javac` is a deliberate choice
(see the main README), but the versioning convention those tools made
standard is worth borrowing anyway.

## Two channels, both driven by tags

### Release candidates (pre-release channel)

When you want to test a version before calling it final:

1. Make sure `VERSION` on `main` matches the version you're about to cut
   (e.g. `0.2.0`).
2. Tag the commit and push the tag:
   ```bash
   git tag v0.2.0-rc.1
   git push origin v0.2.0-rc.1
   ```
3. `.github/workflows/release-candidate.yml` picks up any tag matching
   `v*-rc*`. It compiles the library, runs `build.sh`'s test suite, checks
   that the tag's version actually matches the `VERSION` file (so you can't
   tag the wrong commit by accident), packages a jar, and publishes it as a
   GitHub **pre-release**.

Found a problem? Fix it on `main`, then tag `v0.2.0-rc.2`, and so on. RCs
are disposable; there's no limit on how many you cut for one version.

### Stable releases

Once an RC has been good enough to trust:

1. Tag the same commit (or a later one on `main`) with the plain version,
   no `-rc` suffix:
   ```bash
   git tag v0.2.0
   git push origin v0.2.0
   ```
2. `.github/workflows/release.yml` picks up tags matching `v*` that
   *aren't* release candidates. It refuses to run unless the tag actually
   points at a commit on `main` (stable releases only ever come from
   `main`, never a feature branch), checks the version against `VERSION`,
   runs the build and tests again, packages the jar, and publishes a real
   GitHub Release.

## Why both workflows re-run the build

Tagging a commit that already passed CI on its PR might feel redundant,
but a lot can happen between "this branch passed CI" and "someone tags a
release": the tag might land on the wrong commit, a dependency in the CI
image might have moved, or the tag itself could just be a typo. Re-running
`build.sh` at release time means the artifact people actually download was
verified at the moment it was built, not three weeks earlier on a
different commit.
