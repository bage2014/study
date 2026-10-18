# Test Case: TRAE-security-review

- **Case ID**: `TC-TRAE-security-review`
- **Category**: trae
- **Status**: untested
- **Requires external setup**: no

## Description

Code security scanning / vulnerability audit of MR/PR/commit/branch/diff/uncommitted changes.

## Trigger condition

user requests a security review / vulnerability scan

## Prerequisites

- A code diff or file set to audit

## Test prompt (send this to the assistant)

```text
Security-review this code:
```python
query = f"SELECT * FROM users WHERE name='{name}'"
```
```

## Expected behavior

Skill loads; agent reports demonstrably exploitable, change-introduced risks (here: SQL injection).

## Assertions (pass criteria)

- [ ] Identifies SQL injection risk
- [ ] Risk is tied to the provided code
- [ ] Recommendations are concrete

## Notes / Actual result

_fill in after running_
