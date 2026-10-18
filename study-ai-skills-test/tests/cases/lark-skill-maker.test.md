# Test Case: lark-skill-maker

- **Case ID**: `TC-lark-skill-maker`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Create custom skills for lark-cli wrapping atomic APIs or multi-step flows.

## Trigger condition

create a custom lark-cli skill

## Prerequisites

- Lark authorization
- A skill spec

## Test prompt (send this to the assistant)

```text
Create a custom lark-cli skill that sends a message and then pins it.
```

## Expected behavior

Skill loads; produces a custom skill definition wrapping APIs.

## Assertions (pass criteria)

- [ ] Routes to lark-skill-maker
- [ ] Produces a custom skill
- [ ] Wraps message + pin flow

## Notes / Actual result

_fill in after running_
