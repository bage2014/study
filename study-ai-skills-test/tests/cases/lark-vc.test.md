# Test Case: lark-vc

- **Case ID**: `TC-lark-vc`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Use only when explicitly specified; delegates to lark-meeting.

## Trigger condition

explicitly requests lark-vc

## Prerequisites

- Lark authorization
- A meeting id

## Test prompt (send this to the assistant)

```text
Use lark-vc to query an ongoing meeting by id 'xxxx'.
```

## Expected behavior

Skill loads; delegates to lark-meeting.

## Assertions (pass criteria)

- [ ] Routes via lark-vc to lark-meeting
- [ ] Queries meeting
- [ ] Uses meeting id

## Notes / Actual result

_fill in after running_
