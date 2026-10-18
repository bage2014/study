# Test Case: common-code-review

- **Case ID**: `TC-common-code-review`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Code review: code quality checks and improvement suggestions.

## Trigger condition

need code review / quality check

## Prerequisites

- A code snippet or file

## Test prompt (send this to the assistant)

```text
Review this Java method for quality:
```java
public int add(int a,int b){return a+b;}
```
```

## Expected behavior

Skill loads; returns quality findings (readability, conventions) and improvement suggestions.

## Assertions (pass criteria)

- [ ] Provides quality findings
- [ ] Suggests improvements
- [ ] References the provided code

## Notes / Actual result

_fill in after running_
