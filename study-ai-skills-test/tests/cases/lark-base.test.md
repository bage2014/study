# Test Case: lark-base

- **Case ID**: `TC-lark-base`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu Base (bitable): tables, fields, records, views, forms, dashboards, workspace, roles, templates.

## Trigger condition

Base/bitable operations, /base/ or /app/ links

## Prerequisites

- Lark authorization
- A Base app token

## Test prompt (send this to the assistant)

```text
List all tables in my Feishu Base at https://.../base/xxxx.
```

## Expected behavior

Skill loads; lists tables in the specified Base.

## Assertions (pass criteria)

- [ ] Routes to lark-base
- [ ] Lists tables
- [ ] Uses base token from URL

## Notes / Actual result

_fill in after running_
