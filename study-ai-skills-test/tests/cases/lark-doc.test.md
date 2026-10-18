# Test Case: lark-doc

- **Case ID**: `TC-lark-doc`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu cloud docs (Docx/Wiki): read, create, edit, insert/download images, mind notes.

## Trigger condition

operate on a doc/wiki (URL/token)

## Prerequisites

- Lark authorization
- A doc URL/token

## Test prompt (send this to the assistant)

```text
Read the content of this Feishu doc: https://.../docx/xxxx.
```

## Expected behavior

Skill loads; reads doc content by token.

## Assertions (pass criteria)

- [ ] Routes to lark-doc
- [ ] Reads doc content
- [ ] Uses doc token

## Notes / Actual result

_fill in after running_
