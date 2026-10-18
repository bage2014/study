# Test Case: lark-mail

- **Case ID**: `TC-lark-mail`
- **Category**: lark
- **Status**: untested
- **Requires external setup**: yes

## Description

Feishu Mail: draft, send, reply, forward, search, folders, labels, contacts, rules.

## Trigger condition

email intent: draft/send/search mail

## Prerequisites

- Lark authorization

## Test prompt (send this to the assistant)

```text
Draft an email to test@example.com with subject 'Hello'.
```

## Expected behavior

Skill loads; drafts an email.

## Assertions (pass criteria)

- [ ] Routes to lark-mail
- [ ] Drafts email
- [ ] Correct recipient/subject

## Notes / Actual result

_fill in after running_
