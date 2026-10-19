# GitHub Process

GitHub is used throughout CSC 326 for version control and teaming. This document covers the branching, pull request, and issue conventions expected on every CoffeeMaker repository (individual and team). **Note: this course uses GitHub Enterprise Cloud for submission repos.**

## Branching, Merging, and Pull Requests

Branches provide a place to experiment and work without breaking `main`. `main` represents a working, deployable product; branches represent development and bug-fixing work in progress.

**The CSC326 branch model:**
- All development happens on branches other than `main`.
- From `main`, create a `development` branch.
- From `development`, each team member creates their own branches, named `unityID/branchName` (e.g., `jdoe/recipe_not_created`) so ownership is easy to track.
- Individual branches merge into `development`; `development` merges into `main`.

**Rules for `main`:**
- `main` should always remain green — the project builds, passes all tests, and has sufficient coverage.
- `main` represents the deliverable version of the project and should be deployable at all times.
- All commits reach `main` via pull request. **No direct commits to `main` are allowed**, even on an individual repository.

**Rules for Pull Requests:**
- A PR is opened from a branch when it's ready for review, and should be merged into `development` (not directly into `main`) unless it's the final PR delivering `development` → `main`.
- A PR should confirm: the architecture is maintained, style guidelines are met, tests are high quality, all tests pass, and static analysis issues are resolved. Document any exceptions in the PR itself.
- Another team member should approve a PR before it's merged. On an individual assignment, you may merge your own PR once you've reviewed it yourself.
- Never delete a merged branch — the history is part of what's being evaluated.

## Issues

GitHub Issues record tasks and bugs. An issue can be assigned to a specific developer who owns it, and all team members can contribute to the discussion.

**Bug reports must include:**
- **Reproduce** — the steps to reproduce the bug.
- **Isolate** — the minimal steps that still reveal the bug.
- **Generalize** — whether the same bug shows up in other similar situations.

**Process:**
- Every issue gets a meaningful name and, at minimum, an assigned owner.
- Document all enhancements and bugs as issues; bugs need a full bug report.
- Every commit should reference its issue via `#` mention in the commit message (e.g., `Fixes #4 — recipe id was never persisted`). Using phrasing like `Fixes #4` in a PR merged into `main` will auto-close the issue.

## Onboarding Project Expectations

As part of the Onboarding Project, every repository is expected to:
1. Push a building CoffeeMaker project to the `main` branch of the assigned repository for grading.
2. Create a `development` branch to help merging feature/bug branches.
3. additional branches per issue/feature are required.
4. Test the project locally with black-box tests and additional unit tests.
5. File issues for bugs and enhancements, each assigned to an owner.
6. Each commit should be tied to an issues.
7. One branch per issue/feature.
8. Close issues as you PR into `development`.
