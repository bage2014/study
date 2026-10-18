# Test Case: lark-contact

- **Case ID**: `TC-lark-contact`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu contacts: resolve name/email to open_id or reverse; search bots/agents.

## Trigger condition

resolve a name/email to open_id or look up contact info

## Prerequisites

- Lark authorization
- A name/email or open_id

## Test prompt (send this to the assistant)

```text
Resolve the open_id for user 'zhangsan' in Feishu.
```

## Expected behavior

Skill loads; resolves name to open_id.

## Assertions (pass criteria)

- [ ] Routes to lark-contact
- [ ] Returns open_id
- [ ] Resolves by name

## Notes / Actual result

_fill in after running_
