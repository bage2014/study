# Test Case: common-coding

- **Case ID**: `TC-common-coding`
- **Category**: common
- **Status**: pass
- **Requires external setup**: no

## Description

Coding guidance following standards and best practices.

## Trigger condition

user needs to write code

## Prerequisites

- A coding requirement

## Test prompt (send this to the assistant)

```text
Write a Java method to reverse a string, following best practices and include a brief explanation.
```

## Expected behavior

Skill loads; generates code with explanation following standards (null-safety, readability).

## Assertions (pass criteria)

- [ ] Produces working Java code
- [ ] Includes explanation
- [ ] Handles null/empty gracefully

## Notes / Actual result

Produced Java StringUtils.reverse() using StringBuilder.reverse(), null/empty-safe, with private constructor on utility class and explanation. All assertions met.
