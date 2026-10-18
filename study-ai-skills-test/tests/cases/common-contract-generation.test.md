# Test Case: common-contract-generation

- **Case ID**: `TC-common-contract-generation`
- **Category**: common
- **Status**: untested
- **Requires external setup**: no

## Description

API contract document generation and management.

## Trigger condition

need API contract / interface contract

## Prerequisites

- An API to contract-ify

## Test prompt (send this to the assistant)

```text
Generate an API contract for POST /api/orders that creates an order (request/response schema).
```

## Expected behavior

Skill loads; produces a contract document with request/response schemas and error cases.

## Assertions (pass criteria)

- [ ] Includes request schema
- [ ] Includes response schema
- [ ] Lists error cases

## Notes / Actual result

_fill in after running_
