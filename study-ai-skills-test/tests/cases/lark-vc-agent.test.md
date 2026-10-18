# Test Case: lark-vc-agent

- **Case ID**: `TC-lark-vc-agent`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Use only when explicitly specified; delegates to lark-meeting.

## Trigger condition

explicitly requests lark-vc-agent

## Prerequisites

- Lark authorization
- A meeting context

## Test prompt (send this to the assistant)

```text
Use lark-vc-agent to join a meeting as a bot.
```

## Expected behavior

Skill loads; delegates to lark-meeting bot participation.

## Assertions (pass criteria)

- [ ] Routes via lark-vc-agent to lark-meeting
- [ ] Bot participation
- [ ] Uses meeting context

## Notes / Actual result

_fill in after running_
