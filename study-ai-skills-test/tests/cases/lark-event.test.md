# Test Case: lark-event

- **Case ID**: `TC-lark-event`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Lark real-time event listening/subscribing/consuming via lark-cli event consume.

## Trigger condition

need to listen to Lark real-time events

## Prerequisites

- Lark authorization
- An event key

## Test prompt (send this to the assistant)

```text
Subscribe to Feishu IM message events and print them as they arrive.
```

## Expected behavior

Skill loads; streams events via lark-cli event consume.

## Assertions (pass criteria)

- [ ] Routes to lark-event
- [ ] Uses event consume mechanism
- [ ] Streams events

## Notes / Actual result

_fill in after running_
