# Test Case: lark-note

- **Case ID**: `TC-lark-note`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Use only when explicitly specified; delegates to lark-meeting.

## Trigger condition

explicitly requests lark-note

## Prerequisites

- Lark authorization
- A note id

## Test prompt (send this to the assistant)

```text
Use lark-note to fetch note 'xxxx'.
```

## Expected behavior

Skill loads; delegates to lark-meeting.

## Assertions (pass criteria)

- [ ] Routes via lark-note to lark-meeting
- [ ] Returns note content
- [ ] Uses note id

## Notes / Actual result

_fill in after running_
