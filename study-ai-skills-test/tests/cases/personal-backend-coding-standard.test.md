# Test Case: personal-backend-coding-standard

- **Case ID**: `TC-personal-backend-coding-standard`
- **Category**: personal
- **Status**: untested
- **Requires external setup**: no

## Description

Backend coding standards and best practices guidance.

## Trigger condition

need backend coding standards

## Prerequisites

- Backend code to standardize

## Test prompt (send this to the assistant)

```text
Review this Java controller against backend coding standards:
```java
@GetMapping("/list")
public List list(){ return service.findAll(); }
```
```

## Expected behavior

Skill loads; checks against backend standards (DTO use, generics, exception handling, logging).

## Assertions (pass criteria)

- [ ] Mentions backend standards
- [ ] Points out missing generics/DTO/exception handling
- [ ] Gives concrete fixes

## Notes / Actual result

_fill in after running_
