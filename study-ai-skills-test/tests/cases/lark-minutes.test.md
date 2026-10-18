# Test Case: lark-minutes

- **Case ID**: `TC-lark-minutes`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Use only when explicitly specified; delegates to lark-meeting.

## Trigger condition

explicitly requests lark-minutes

## Prerequisites

- Lark authorization
- A minute token

## Test prompt (send this to the assistant)

```text
Use lark-minutes to get the transcript for minute token 'xxxx'.
```

## Expected behavior

Skill loads; delegates to lark-meeting to retrieve transcript.

## Assertions (pass criteria)

- [ ] Routes via lark-minutes to lark-meeting
- [ ] Returns transcript
- [ ] Uses minute token

## Notes / Actual result

_fill in after running_
