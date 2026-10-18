# Test Case: TRAE-code-review

- **Case ID**: `TC-TRAE-code-review`
- **Category**: trae
- **Status**: untested
- **Requires external setup**: no

## Description

General code review of an MR/PR, commit, branch, or code diff (quality/correctness/maintainability/perf/best-practices).

## Trigger condition

user explicitly asks for a general code review of MR/PR/commit/branch/diff

## Prerequisites

- A code diff or MR/PR to review

## Test prompt (send this to the assistant)

```text
Review this diff for quality issues:
```diff
- if (x == null) return null;
+ if (x == null) {
+   return null;
+ }
```
```

## Expected behavior

Skill loads; agent returns structured findings covering correctness, maintainability, and best practices; does not treat as security audit.

## Assertions (pass criteria)

- [ ] Output contains review findings
- [ ] Findings reference the provided diff
- [ ] No security-audit framing

## Notes / Actual result

_fill in after running_
