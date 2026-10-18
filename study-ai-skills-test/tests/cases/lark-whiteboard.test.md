# Test Case: lark-whiteboard

- **Case ID**: `TC-lark-whiteboard`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu whiteboards in docs: export preview, export node structure, update content.

## Trigger condition

view/export/edit a whiteboard in a doc

## Prerequisites

- Lark authorization
- A whiteboard token

## Test prompt (send this to the assistant)

```text
Export my Feishu whiteboard 'xxxx' as a preview image.
```

## Expected behavior

Skill loads; exports whiteboard preview.

## Assertions (pass criteria)

- [ ] Routes to lark-whiteboard
- [ ] Exports preview
- [ ] Uses whiteboard token

## Notes / Actual result

_fill in after running_
