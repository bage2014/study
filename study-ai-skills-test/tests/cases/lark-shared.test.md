# Test Case: lark-shared

- **Case ID**: `TC-lark-shared`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

lark-cli setup/auth: login/status/logout, identity, domain permissions, missing scopes, _notice handling.

## Trigger condition

lark-cli auth setup or credential issues

## Prerequisites

- lark-cli installed

## Test prompt (send this to the assistant)

```text
Check my Feishu CLI auth status.
```

## Expected behavior

Skill loads; checks lark-cli auth status.

## Assertions (pass criteria)

- [ ] Routes to lark-shared
- [ ] Returns auth status
- [ ] Mentions login/logout if needed

## Notes / Actual result

_fill in after running_
