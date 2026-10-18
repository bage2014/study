# Test Case: common-backend-unit-test

- **Case ID**: `TC-common-backend-unit-test`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

Backend unit test guidance and generation; JUnit 5, Mockito.

## Trigger condition

need backend unit tests (Java/JUnit/Mockito)

## Prerequisites

- A Java class/method to test

## Test prompt (send this to the assistant)

```text
Write JUnit 5 + Mockito unit tests for a UserService.register(User) method that calls userRepository.save.
```

## Expected behavior

Skill loads; produces JUnit 5 test using Mockito to mock repository, with assertions and verify.

## Assertions (pass criteria)

- [ ] Uses @Test (JUnit 5)
- [ ] Uses Mockito (@Mock / when / verify)
- [ ] Asserts behavior of register

## Notes / Actual result

_fill in after running_
