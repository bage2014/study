# Test Case: lark-openapi-explorer

- **Case ID**: `TC-lark-openapi-explorer`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Discover native Feishu OpenAPI endpoints not wrapped by lark-cli.

## Trigger condition

need a native OpenAPI not covered by existing lark skills

## Prerequisites

- Lark authorization
- An API need

## Test prompt (send this to the assistant)

```text
Find a native Feishu OpenAPI to get a user's department by open_id.
```

## Expected behavior

Skill loads; finds and documents the native endpoint.

## Assertions (pass criteria)

- [ ] Routes to lark-openapi-explorer
- [ ] Finds a native endpoint
- [ ] Documents it

## Notes / Actual result

_fill in after running_
